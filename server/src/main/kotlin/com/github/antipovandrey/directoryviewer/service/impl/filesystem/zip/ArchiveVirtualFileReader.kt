package com.github.antipovandrey.directoryviewer.service.impl.filesystem.zip

import com.github.antipovandrey.directoryviewer.model.VirtualFile
import com.github.antipovandrey.directoryviewer.service.impl.filesystem.PathComponentsResolver
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.io.File
import java.io.IOException
import java.util.zip.ZipFile

@Component
class ArchiveVirtualFileReader(
        private val pathComponentsResolver: PathComponentsResolver
) {

    private val zipExtension = "zip"
    private val zipFilesDelimiter = "/"

    private val log = LoggerFactory.getLogger(ArchiveVirtualFileReader::class.java)

    /**
     * Returns a list of virtual files by path which contains archives.
     * Only .zip is supported.
     *
     * @param path path to create virtual files
     * @param fileThreshold a threshold to stop propagation when looking for a zip in path
     * @return list of virtual files, or `null` if not succeed
     */
    fun readInsideOfArchive(path: File, fileThreshold: File): List<VirtualFile>? {
        return try {
            val zipPath = formZipPath(path, fileThreshold)
            log.info("Read zip path {}, {}", path, zipPath)
            readArchive(zipPath)
        } catch (ex: IOException) {
            log.warn("IOException during zip expanding", ex)
            null
        }
    }

    /**
     *  Forms a [ZipPath] by given [path] and [fileThreshold].
     *  Suppose [path] is /root/dir/arch.zip/another/dir/arch2.zip/file and [fileThreshold] is /root
     *  Formed [ZipPath] would consist of:
     *          root: /root/dir
     *          components: [
     *               (name: arch.zip, pathComponents: [another, dir]),
     *               (name: arch2.zip, pathComponents: [file])
     *          ]
     *
     *  @param path path to form a [ZipPath]
     *  @param fileThreshold beginning part of the [path] where searching will be stopped
     *  @return formed [ZipPath]
     *  @throws IOException
     */
    private fun formZipPath(path: File, fileThreshold: File): ZipPath {
        val zipPathComponents = mutableListOf<ZipPathComponent>()

        val archivePathComponentsAccStack = mutableListOf<String>()
        var currentPath: File? = path
        while (currentPath != null && currentPath != fileThreshold) {
            if (currentPath.extension == zipExtension) {
                zipPathComponents.add(ZipPathComponent(currentPath.name, archivePathComponentsAccStack.reversed()))
                archivePathComponentsAccStack.clear()
            } else {
                archivePathComponentsAccStack.add(currentPath.name)
            }
            currentPath = currentPath.parentFile
        }

        currentPath = currentPath
                ?: throw IOException("Unexpected absence of parent dir. Check fileThreshold: $fileThreshold")

        val topmostZipFile = pathComponentsResolver.resolve(currentPath, archivePathComponentsAccStack.reversed())
        return ZipPath(topmostZipFile, zipPathComponents.reversed())
    }

    private fun readArchive(zipPath: ZipPath): List<VirtualFile> {
        val (zipRoot, zipComponents) = zipPath
        if (zipComponents.isEmpty()) throw Exception()
        if (zipComponents.size != 1) TODO("handle nested zip")

        val (zipName, archivePathComponents) = zipComponents.first()

        val pathRelativeToZip = archivePathComponents
                .joinToString(separator = "") { pathComponent -> "$pathComponent$zipFilesDelimiter" }

        val archiveFile = zipRoot.resolve(zipName)

        return ZipFile(archiveFile).use { zip ->
            zip.entries().toList()
                    .filter { it.name.startsWith(pathRelativeToZip) }
                    .mapNotNull { it.name.removePrefix(pathRelativeToZip).split(zipFilesDelimiter).firstOrNull() }
                    .filter { it.isNotEmpty() }
                    .distinct()
                    .map { pathRelativeToZip + it }
                    .map { pathInZip ->
                        val entry = zip.getEntry(pathInZip)
                        val archivedFile = archiveFile.resolve(pathInZip)
                        VirtualFile(
                                archivedFile.absolutePath,
                                archivedFile.name,
                                archivedFile.extension,
                                entry.isDirectory,
                                isReadable = true
                        )
                    }
        }
    }
}
