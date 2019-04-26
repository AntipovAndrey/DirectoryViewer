package com.github.antipovandrey.directoryviewer.service.impl.filesystem.validation

import com.github.antipovandrey.directoryviewer.service.exception.IllegalPathException

class WindowsPathComponentsValidator : PathComponentsValidator {

    private val directoryUp = ".."
    private val diskColonIdentifier = ":"

    override fun check(pathComponents: List<String>) {
        val vulnerableComponents = pathComponents
                .filter { it == directoryUp || it.contains(diskColonIdentifier) }

        if (vulnerableComponents.isEmpty()) return

        throw IllegalPathException("Path components $pathComponents contains illegal ones: $vulnerableComponents")
    }
}
