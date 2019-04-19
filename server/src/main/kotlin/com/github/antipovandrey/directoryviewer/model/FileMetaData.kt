package com.github.antipovandrey.directoryviewer.model

/**
 *  Represents a file with metadata such as path, type and etc.
 */
data class FileMetaData(
        val directoryPathComponents: List<String>,
        val name: String,
        val type: FileType,
        val extension: String?,
        val expandable: Boolean = false,
        val expandSupported: Boolean = false)