package com.github.antipovandrey.directoryviewer.service.impl.metadata.resolver

import com.github.antipovandrey.directoryviewer.model.FileMetaData
import com.github.antipovandrey.directoryviewer.model.VirtualFile
import org.springframework.stereotype.Component

@Component
interface MetaDataResolver {

    /**
     * Creates metadata for given file
     *
     * @param virtualFile virtual file to create metadata
     * @return formed [FileMetaData] or `null` if this type of file is not supported by this resolver
     */
    fun resolveMetaData(virtualFile: VirtualFile): FileMetaData?
}
