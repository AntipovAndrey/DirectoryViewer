package com.github.antipovandrey.directoryviewer.service.impl.metadata.resolver

import com.github.antipovandrey.directoryviewer.model.FileMetaData
import com.github.antipovandrey.directoryviewer.model.FileType
import com.github.antipovandrey.directoryviewer.model.VirtualFile
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class SourceCodeMetaDataResolver(
        @Value("\${directoryviewer.extensions.source}")  extensions: Set<String>
) : AbstractMetaDataResolver(extensions) {

    override fun resolve(virtualFile: VirtualFile): FileMetaData {
        return FileMetaData(
                type = FileType.SourceCode
        )
    }
}