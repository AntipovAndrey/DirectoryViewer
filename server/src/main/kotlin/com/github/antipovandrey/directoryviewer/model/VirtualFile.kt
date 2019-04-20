package com.github.antipovandrey.directoryviewer.model

data class VirtualFile(
        val path: String,
        val name: String,
        val extension: String?,
        val isDirectory: Boolean,
        val isReadable: Boolean
)