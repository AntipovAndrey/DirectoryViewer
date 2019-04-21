package com.github.antipovandrey.directoryviewer.service.impl.filesystem.zip

import java.io.File

data class ZipPath(
        val root: File,
        val components: List<ZipPathComponent>)
