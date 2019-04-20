package com.github.antipovandrey.directoryviewer.model

/**
 *  Represents a file metadata.
 */
data class FileMetaData(
        val type: FileType,
        val expandable: Boolean = false,
        val expandSupported: Boolean = false)
