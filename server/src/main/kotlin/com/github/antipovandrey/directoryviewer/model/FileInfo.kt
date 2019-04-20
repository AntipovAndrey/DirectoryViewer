package com.github.antipovandrey.directoryviewer.model

/**
 *  Represents information about file with additional metadata.
 */
data class FileInfo(
        val directoryPathComponents: List<String>,
        val name: String,
        val metaData: FileMetaData)
