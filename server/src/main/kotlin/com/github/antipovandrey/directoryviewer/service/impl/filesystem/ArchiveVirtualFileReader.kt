package com.github.antipovandrey.directoryviewer.service.impl.filesystem

import com.github.antipovandrey.directoryviewer.model.VirtualFile
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.io.File
import java.io.IOException
import java.util.zip.ZipFile

@Component
class ArchiveVirtualFileReader {

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
        val (closestArchive, archivePathComponents) = findTopmostArchive(path, fileThreshold) ?: return null

        val pathRelativeToZip = archivePathComponents
                .joinToString(separator = "") { pathComponent -> "$pathComponent$zipFilesDelimiter" }

        return try {
            readArchive(closestArchive, pathRelativeToZip)
        } catch (ex: IOException) {
            log.warn("IOException during zip expanding", ex)
            null
        }
    }

    private fun readArchive(closestArchive: File, pathRelativeToZip: String): List<VirtualFile> {
        return ZipFile(closestArchive.absolutePath).use { zip ->
            zip.entries().toList()
                    .filter { it.name.startsWith(pathRelativeToZip) }
                    .mapNotNull { it.name.removePrefix(pathRelativeToZip).split(zipFilesDelimiter).firstOrNull() }
                    .filter { it.isNotEmpty() }
                    .distinct()
                    .map { pathRelativeToZip + it }
                    .map { pathInZip ->
                        val entry = zip.getEntry(pathInZip)
                        val archivedFile = closestArchive.resolve(pathInZip)
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

    private fun findTopmostArchive(path: File, fileThreshold: File): Pair<File, List<String>>? {
        var closestArchive: File? = null
        var archivePathComponents: List<String>? = null
        val archivePathComponentsAcc = mutableListOf<String>()

        var currentPath: File? = path
        while (currentPath != null && currentPath != fileThreshold) {
            if (currentPath.extension == zipExtension) {
                closestArchive = currentPath
                archivePathComponents = archivePathComponentsAcc
            } else {
                archivePathComponentsAcc.add(currentPath.name)
            }
            currentPath = currentPath.parentFile
        }
        if (closestArchive == null || archivePathComponents == null) {
            return null
        }
        return Pair(closestArchive, archivePathComponents.reversed())
    }
}
