package com.github.antipovandrey.directoryviewer.service.impl.metadata.resolver

import com.github.antipovandrey.directoryviewer.model.FileMetaData
import com.github.antipovandrey.directoryviewer.model.FileType
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.File

@Component
class ArchiveMetaDataResolver(
        @Value("\${directoryviewer.extensions.archive}") extensions: Set<String>,
        @Value("\${directoryviewer.extensions.archive.expandable}") private val expandSupportedExtensions: Set<String>
) : AbstractMetaDataResolver(extensions) {

    override fun resolve(path: File): FileMetaData {
        return FileMetaData(
                type = FileType.Archive,
                expandable = true,
                expandSupported = path.extension in expandSupportedExtensions
        )
    }
}
