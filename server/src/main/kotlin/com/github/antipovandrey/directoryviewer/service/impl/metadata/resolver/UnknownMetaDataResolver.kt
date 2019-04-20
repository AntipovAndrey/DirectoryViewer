package com.github.antipovandrey.directoryviewer.service.impl.metadata.resolver

import com.github.antipovandrey.directoryviewer.model.FileMetaData
import com.github.antipovandrey.directoryviewer.model.FileType
import org.springframework.stereotype.Component
import java.io.File

@Component
class UnknownMetaDataResolver : AbstractMetaDataResolver(emptySet()) {

    override fun isSupported(path: File): Boolean = true

    override fun resolve(path: File): FileMetaData {
        return FileMetaData(type = FileType.Unknown)
    }
}
