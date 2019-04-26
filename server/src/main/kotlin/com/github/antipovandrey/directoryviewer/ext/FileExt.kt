package com.github.antipovandrey.directoryviewer.ext

import com.github.antipovandrey.directoryviewer.model.VirtualFile
import java.io.File
import java.nio.file.Files

fun File.toVirtualFile(): VirtualFile {
    return VirtualFile(
            extension = extension,
            name = name,
            isDirectory = isDirectory,
            isReadable = Files.isReadable(toPath().toRealPath())
    )
}
