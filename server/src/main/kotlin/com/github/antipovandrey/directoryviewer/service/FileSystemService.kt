package com.github.antipovandrey.directoryviewer.service

import com.github.antipovandrey.directoryviewer.model.FileMetaData

interface FileSystemService {

    /**
     *  Returns a list of all files and directories that
     *  are descendants of a root.
     *  The root of filesystem may vary dependant an implementation
     *  of the interface.
     *
     *  @return metadata for root descendants
     */
    fun getRootDescendants(): List<FileMetaData>

    /**
     *  Returns a list of all files and directories that
     *  are descendants of a given [pathComponents] in the filesystem root.
     *
     *  @param pathComponents list of path components that forms a relative path to collect metadata.
     *  @return metadata about for [pathComponents] descendants.
     *  @throws IllegalArgumentException if resolved path could not be expanded
     */
    fun getDescendantsFor(pathComponents: List<String>): List<FileMetaData>
}