package com.github.antipovandrey.directoryviewer.service.impl.metadata.resolver

import com.github.antipovandrey.directoryviewer.model.FileMetaData
import org.springframework.stereotype.Component
import java.io.File

@Component
interface MetaDataResolver {

    /**
     * Creates metadata for given file
     *
     * @param path absolute path to create metadata
     * @return formed [FileMetaData] or `null` if this type of file is not supported by this resolver
     */
    fun resolveMetaData(path: File): FileMetaData?
}
