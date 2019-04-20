package com.github.antipovandrey.directoryviewer.service.impl.filesystem

import com.github.antipovandrey.directoryviewer.ext.toVirtualFile
import com.github.antipovandrey.directoryviewer.model.FileInfo
import com.github.antipovandrey.directoryviewer.model.VirtualFile
import com.github.antipovandrey.directoryviewer.service.FileSystemService
import com.github.antipovandrey.directoryviewer.service.MetaDataService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.File

@Service
class FileSystemServiceImpl(
        @Value("\${directoryviewer.fsroot}") private val rootFile: File,
        private val pathComponentsResolver: PathComponentsResolver,
        private val archiveVirtualFileReader: ArchiveVirtualFileReader,
        private val metaDataService: MetaDataService
) : FileSystemService {

    override fun getDescendantsFor(pathComponents: List<String>): List<FileInfo> {
        return collectMetaData(pathComponents)
    }

    override fun getRootInfo(): FileInfo {
        return getFileInfo(rootFile.toVirtualFile(), emptyList()).copy(name = "")
    }

    private fun collectMetaData(pathComponents: List<String>): List<FileInfo> {
        val path: File = pathComponentsResolver.resolve(rootFile, pathComponents)

        val virtualFiles = if (path.isDirectory) {
            path.listFiles()?.asList()?.map { file -> file.toVirtualFile() }
        } else {
            archiveVirtualFileReader.readInsideOfArchive(path, rootFile)
        }

        return virtualFiles?.map { virtualFile ->
            getFileInfo(virtualFile, pathComponents)
        } ?: throw IllegalArgumentException("Cannot expand directory $path")
    }

    private fun getFileInfo(virtualFile: VirtualFile, pathComponents: List<String>): FileInfo {
        return FileInfo(
                directoryPathComponents = pathComponents,
                name = virtualFile.name,
                metaData = metaDataService.getMetaData(virtualFile)
        )
    }
}
