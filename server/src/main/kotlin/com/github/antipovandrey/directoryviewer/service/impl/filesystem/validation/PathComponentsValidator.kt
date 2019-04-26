package com.github.antipovandrey.directoryviewer.service.impl.filesystem.validation

import com.github.antipovandrey.directoryviewer.service.exception.IllegalPathException

interface PathComponentsValidator {

    /**
     *  Assures that [pathComponents] list doesn't contain vulnerable components
     *
     *  @param pathComponents components list to check
     *  @throws [IllegalPathException] if path is not valid or vulnerable
     */
    fun check(pathComponents: List<String>)
}