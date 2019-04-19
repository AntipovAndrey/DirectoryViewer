package com.github.antipovandrey.directoryviewer.service.impl.filesystem

import org.springframework.stereotype.Component
import java.io.File

@Component
class PathComponentsResolver {

    /**
     *  Resolves given path
     *
     *  @param rootFile base path to resolve components with
     *  @param pathComponents list of path components that forms a path
     *  @return resolved [File]
     */
    fun resolve(rootFile: File, pathComponents: List<String>): File {
        return pathComponents.map { File(it) }
                .fold(rootFile) { acc, next -> acc.resolve(next) }
    }
}
