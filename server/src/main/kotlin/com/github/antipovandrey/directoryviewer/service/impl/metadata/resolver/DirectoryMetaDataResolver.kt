package com.github.antipovandrey.directoryviewer.service.impl.metadata.resolver

import com.github.antipovandrey.directoryviewer.model.FileMetaData
import com.github.antipovandrey.directoryviewer.model.FileType
import com.github.antipovandrey.directoryviewer.model.VirtualFile
import org.springframework.stereotype.Component

@Component
class DirectoryMetaDataResolver : AbstractMetaDataResolver(emptySet()) {

    override fun isSupported(virtualFile: VirtualFile): Boolean {
        return virtualFile.isDirectory
    }

    override fun resolve(virtualFile: VirtualFile): FileMetaData {
        return FileMetaData(
                type = FileType.Directory,
                expandable = true,
                expandSupported = virtualFile.isReadable
        )
    }
}
