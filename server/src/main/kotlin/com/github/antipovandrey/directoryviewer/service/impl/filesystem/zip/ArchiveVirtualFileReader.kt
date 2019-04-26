package com.github.antipovandrey.directoryviewer.service.impl.filesystem.zip

import com.github.antipovandrey.directoryviewer.model.VirtualFile
import com.github.antipovandrey.directoryviewer.service.impl.filesystem.PathComponentsResolver
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.nio.file.Files
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

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
        while (currentPath != null && !isSameFiles(currentPath, fileThreshold)) {
            if (currentPath.extension == zipExtension) {
                zipPathComponents.add(ZipPathComponent(currentPath.name, archivePathComponentsAccStack.reversed()))
                archivePathComponentsAccStack.clear()
            } else {
                archivePathComponentsAccStack.add(currentPath.name)
            }
            currentPath = currentPath.parentFile
        }

        val topmostZipFile = pathComponentsResolver.resolve(fileThreshold, archivePathComponentsAccStack.reversed())
        return ZipPath(topmostZipFile, zipPathComponents.reversed())
    }

    private fun isSameFiles(currentPath: File, fileSystemRoot: File): Boolean {
        if (currentPath == fileSystemRoot) return true
        if (!currentPath.exists() || !fileSystemRoot.exists()) return false
        return (Files.isSameFile(currentPath.toPath(), fileSystemRoot.toPath()))
    }

    private fun readArchive(zipPath: ZipPath): List<VirtualFile> {
        if (zipPath.components.isEmpty()) {
            throw IOException("Cannot read archive: zipPath componets list is empty $zipPath")
        }

        val targetFilePath = getRelativeZipPath(zipPath.components.last().pathComponents)
        val targetEntries = getAllEntries(getTargetZipInputStream(zipPath))

        return getFilesMatchingPath(targetEntries, targetFilePath)
    }

    private fun getTargetZipInputStream(zipPath: ZipPath): ZipInputStream {
        val targetZipName = zipPath.components.first().name

        val zipPathParts = zipPath.components.zipWithNext()
                .map { (prevComponent: ZipPathComponent, nextComponent: ZipPathComponent) ->
                    getRelativeZipPath(prevComponent.pathComponents) + nextComponent.name
                }

        val zipInputStream = ZipInputStream(FileInputStream(zipPath.root.resolve(targetZipName)))
        var targetZipInputStream = zipInputStream
        zipPathParts.forEach { zipPathPart ->
            advanceStreamToEntry(targetZipInputStream, zipPathPart)
            targetZipInputStream = ZipInputStream(targetZipInputStream)
        }
        return targetZipInputStream
    }

    /**
     *  Moves passed [ZipInputStream] to entry by given [name] and return that entry.
     *
     *  @param zipInputStream [ZipInputStream] to advance
     *  @param name name of entry to look for
     *  @return found [ZipEntry]
     *  @throws IOException if entry not found
     */
    private fun advanceStreamToEntry(zipInputStream: ZipInputStream, name: String): ZipEntry {
        while (true) {
            val entry = zipInputStream.nextEntry ?: break
            if (entry.name == name) {
                return entry
            }
        }
        throw IOException("Entry not found : $name")
    }


    /**
     *  Moves passed [ZipInputStream] to collect all zip entries.
     *
     *  @param zipInputStream [ZipInputStream] to collect entries from
     *  @return collected list of [ZipEntry]s
     */
    private fun getAllEntries(zipInputStream: ZipInputStream): List<ZipEntry> {
        val collected = mutableListOf<ZipEntry>()
        while (true) {
            val entry = zipInputStream.nextEntry ?: break
            collected.add(entry)
        }
        return collected
    }

    private fun getFilesMatchingPath(targetEntries: List<ZipEntry>, targetFilePath: String): List<VirtualFile> {
        return targetEntries.asSequence()
                .filter { zipEntry -> zipEntry.name.startsWith(targetFilePath) }
                .map { zipEntry ->
                    zipEntry to zipEntry.name
                            .removePrefix(targetFilePath)
                            .split(zipFilesDelimiter)
                            .filter { it.isNotEmpty() }
                }
                // looking for the entry itself, not path with child
                .filter { (_, relativeToTargetComponents) -> relativeToTargetComponents.size == 1 }
                .map { (zipEntry, relativeToTargetComponents) -> zipEntry to relativeToTargetComponents.first() }
                .map { (zipEntry, fileName) ->
                    VirtualFile(
                            fileName,
                            File(fileName).extension,
                            zipEntry.isDirectory,
                            isReadable = true)
                }.toList()
    }

    private fun getRelativeZipPath(pathComponents: List<String>): String {
        return pathComponents.joinToString(separator = "") { pathComponent -> "$pathComponent$zipFilesDelimiter" }
    }
}
