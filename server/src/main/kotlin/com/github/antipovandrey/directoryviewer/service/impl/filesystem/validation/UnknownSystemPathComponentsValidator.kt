package com.github.antipovandrey.directoryviewer.service.impl.filesystem.validation

import org.slf4j.LoggerFactory

class UnknownSystemPathComponentsValidator : PathComponentsValidator {

    private val log = LoggerFactory.getLogger(UnknownSystemPathComponentsValidator::class.java)

    override fun check(pathComponents: List<String>) {
        log.warn("Check $pathComponents, but OS is unknown")
    }
}