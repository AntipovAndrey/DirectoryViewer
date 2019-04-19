package com.github.antipovandrey.directoryviewer.service.impl.metadata

import com.github.antipovandrey.directoryviewer.model.FileMetaData
import com.github.antipovandrey.directoryviewer.service.MetaDataService
import org.springframework.stereotype.Service
import java.io.File

@Service
class MetaDataServiceImpl : MetaDataService {

    override fun getMetaData(file: File): FileMetaData {
        return FileMetaData(emptyList(), "") // todo
    }
}