package com.github.antipovandrey.directoryviewer.service.impl.metadata.resolver

import com.github.antipovandrey.directoryviewer.model.FileMetaData
import java.io.File

class ArchiveMetaDataResolver : MetaDataResolver {

    override fun resolveMetaData(path: File): FileMetaData? {
        return null
    }
}
