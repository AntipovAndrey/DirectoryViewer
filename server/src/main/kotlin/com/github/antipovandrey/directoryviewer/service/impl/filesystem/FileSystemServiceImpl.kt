package com.github.antipovandrey.directoryviewer.service.impl.filesystem

import com.github.antipovandrey.directoryviewer.model.FileMetaData
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

    override fun getRootDescendants(): List<FileMetaData> {
        return collectMetaData(rootFile)
    }

    override fun getDescendantsFor(pathComponents: List<String>): List<FileMetaData> {
        return collectMetaData(pathComponentsResolver.resolve(rootFile, pathComponents))
    }

    private fun collectMetaData(path: File): List<FileMetaData> {
        if (!path.isDirectory) throw IllegalArgumentException("File is not expandable: $path")

        val pathList = path.listFiles()?.asList()
                ?: throw IllegalArgumentException("Cannot expand directory $path")

        return pathList.map { metaDataService.getMetaData(it) }
    }
}
