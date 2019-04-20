package com.github.antipovandrey.directoryviewer.model

/**
 *  Represents a file in the filesystem.
 */
data class VirtualFile(
        val path: String,
        val name: String,
        val extension: String?,
        val isDirectory: Boolean,
        val isReadable: Boolean)
