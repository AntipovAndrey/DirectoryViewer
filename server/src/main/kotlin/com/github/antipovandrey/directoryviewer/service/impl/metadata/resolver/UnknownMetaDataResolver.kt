package com.github.antipovandrey.directoryviewer.service.impl.metadata.resolver

import com.github.antipovandrey.directoryviewer.model.FileMetaData
import com.github.antipovandrey.directoryviewer.model.FileType
import com.github.antipovandrey.directoryviewer.model.VirtualFile
import org.springframework.stereotype.Component

@Component
class UnknownMetaDataResolver : AbstractMetaDataResolver(emptySet()) {

    override fun isSupported(virtualFile: VirtualFile): Boolean = true

    override fun resolve(virtualFile: VirtualFile): FileMetaData {
        return FileMetaData(type = FileType.Unknown)
    }
}
