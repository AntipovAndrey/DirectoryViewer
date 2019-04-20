package com.github.antipovandrey.directoryviewer.service.impl.filesystem

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.io.File

@Component
class PathComponentsResolver {

    private val log = LoggerFactory.getLogger(PathComponentsResolver::class.java)

    /**
     *  Resolves given path
     *
     *  @param rootFile base path to resolve components with
     *  @param pathComponents list of path components that forms a path
     *  @return resolved [File]
     */
    fun resolve(rootFile: File, pathComponents: List<String>): File {
        val resolvedPath = pathComponents.map { File(it) }
                .fold(rootFile) { acc, next -> acc.resolve(next) }
        log.info("Resolved path: {}", resolvedPath)
        return resolvedPath
    }
}
