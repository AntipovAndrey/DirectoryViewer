package com.github.antipovandrey.directoryviewer.service.impl.filesystem.validation

import com.github.antipovandrey.directoryviewer.service.exception.IllegalPathException

class UnixPathComponentsValidator : PathComponentsValidator {

    private val directoryUp = ".."

    override fun check(pathComponents: List<String>) {
        if (directoryUp in pathComponents) {
            throw IllegalPathException("Path components $pathComponents contains illegal ones: $directoryUp")
        }
    }
}
