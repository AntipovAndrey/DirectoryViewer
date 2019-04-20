package com.github.antipovandrey.directoryviewer.ext

import com.github.antipovandrey.directoryviewer.model.VirtualFile
import java.io.File

fun File.toVirtualFile(): VirtualFile {
    return VirtualFile(
            path = absolutePath,
            extension = extension,
            name = name,
            isDirectory = isDirectory,
            isReadable = canRead()
    )
}
