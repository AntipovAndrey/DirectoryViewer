package com.github.antipovandrey.directoryviewer.service

import com.github.antipovandrey.directoryviewer.model.FileMetaData
import com.github.antipovandrey.directoryviewer.service.exception.MetaDataResolvingException
import java.io.File

interface MetaDataService {

    /**
     *  Returns formed metadata for a given file
     *
     *  @param file a file to collect metadata
     *  @return metadata for the file
     *  @throws MetaDataResolvingException if file is not valid and metadata could not be collected
     */
    fun getMetaData(file: File): FileMetaData
}