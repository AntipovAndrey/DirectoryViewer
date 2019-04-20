package com.github.antipovandrey.directoryviewer.service

import com.github.antipovandrey.directoryviewer.model.FileInfo

interface FileSystemService {

    /**
     *  Returns a list of all files and directories that
     *  are descendants of a root.
     *  The root of filesystem may vary dependant an implementation
     *  of the interface.
     *
     *  @return file info for root descendants
     */
    fun getRootDescendants(): List<FileInfo>

    /**
     *  Returns a list of all files and directories that
     *  are descendants of given [pathComponents] in the filesystem root.
     *
     *  @param pathComponents list of path components that forms a relative path to collect metadata.
     *  @return file info for [pathComponents] descendants.
     *  @throws IllegalArgumentException if resolved path could not be expanded
     */
    fun getDescendantsFor(pathComponents: List<String>): List<FileInfo>
}