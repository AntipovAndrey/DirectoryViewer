package com.github.antipovandrey.directoryviewer.config

import com.github.antipovandrey.directoryviewer.service.impl.filesystem.validation.PathComponentsValidator
import com.github.antipovandrey.directoryviewer.service.impl.filesystem.validation.UnixPathComponentsValidator
import com.github.antipovandrey.directoryviewer.service.impl.filesystem.validation.UnknownSystemPathComponentsValidator
import com.github.antipovandrey.directoryviewer.service.impl.filesystem.validation.WindowsPathComponentsValidator
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PathComponentsValidatorConfig {

    private val os = System.getProperty("os.name").toLowerCase()

    private val log = LoggerFactory.getLogger(PathComponentsValidatorConfig::class.java)

    @Bean
    fun getPathComponentsValidator(): PathComponentsValidator {
        log.info("Operation system is $os")
        return when {
            isWindows() -> WindowsPathComponentsValidator()
            isUnix() || isMac() -> UnixPathComponentsValidator()
            else -> UnknownSystemPathComponentsValidator()
        }
    }

    private fun isWindows(): Boolean {
        return os.contains("win")
    }

    private fun isMac(): Boolean {
        return os.contains("mac")
    }

    private fun isUnix(): Boolean {
        return os.contains("nix") || os.contains("nux") || os.contains("aix")
    }
}