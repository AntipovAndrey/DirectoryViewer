package com.github.antipovandrey.directoryviewer.service.impl.metadata.resolver

import com.github.antipovandrey.directoryviewer.model.FileMetaData
import com.github.antipovandrey.directoryviewer.model.FileType
import org.springframework.stereotype.Component
import java.io.File

@Component
class DirectoryMetaDataResolver : AbstractMetaDataResolver(emptySet()) {

    override fun isSupported(path: File): Boolean {
        return path.isDirectory
    }

    override fun resolve(path: File): FileMetaData {
        return FileMetaData(
                type = FileType.Directory,
                expandable = true,
                expandSupported = path.canRead()
        )
    }
}
