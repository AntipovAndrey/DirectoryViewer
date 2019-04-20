package com.github.antipovandrey.directoryviewer.service.impl.filesystem

import com.github.antipovandrey.directoryviewer.model.FileInfo
import com.github.antipovandrey.directoryviewer.service.FileSystemService
import com.github.antipovandrey.directoryviewer.service.MetaDataService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.File

@Service
class FileSystemServiceImpl(
        @Value("\${directoryviewer.fsroot}") private val rootFile: File,
        private val pathComponentsResolver: PathComponentsResolver,
        private val metaDataService: MetaDataService
) : FileSystemService {

    override fun getDescendantsFor(pathComponents: List<String>): List<FileInfo> {
        val file = pathComponentsResolver.resolve(rootFile, pathComponents)
        return collectMetaData(file, pathComponents)
    }

    override fun getRootInfo(): FileInfo {
        return getFileInfo(rootFile, emptyList()).copy(name = "")
    }

    private fun collectMetaData(path: File, pathComponents: List<String>): List<FileInfo> {
        if (!path.isDirectory) throw IllegalArgumentException("File is not expandable: $path")

        val pathList = path.listFiles()?.asList()
                ?: throw IllegalArgumentException("Cannot expand directory $path")

        return pathList.map { file ->
            getFileInfo(file, pathComponents)
        }
    }

    private fun getFileInfo(file: File, pathComponents: List<String>): FileInfo {
        return FileInfo(
                directoryPathComponents = pathComponents,
                name = file.name,
                metaData = metaDataService.getMetaData(file)
        )
    }
}
