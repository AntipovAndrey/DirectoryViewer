package com.github.antipovandrey.directoryviewer.service

import com.github.antipovandrey.directoryviewer.model.FileInfo

interface FileSystemService {

    /**
     *  Returns a list of all files and directories that
     *  are descendants of given [pathComponents] in the filesystem root.
     *
     *  @param pathComponents list of path components that forms a relative path to collect metadata.
     *  @return file info for [pathComponents] descendants.
     *  @throws IllegalArgumentException if resolved path could not be expanded
     */
    fun getDescendantsFor(pathComponents: List<String>): List<FileInfo>

    /**
     *  Returns info about a filesystem root
     *
     *  @return file info for filesystem root
     */
    fun getRootInfo(): FileInfo
}