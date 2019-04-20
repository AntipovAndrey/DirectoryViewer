package com.github.antipovandrey.directoryviewer.controller

import com.github.antipovandrey.directoryviewer.model.FileInfo
import com.github.antipovandrey.directoryviewer.service.FileSystemService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.HandlerMapping
import javax.servlet.http.HttpServletRequest

@RequestMapping("view")
@RestController
class DirectoryViewerController(
        private val fileSystemService: FileSystemService
) {

    companion object {

        private const val PATH_COMPONENTS_SEPARATOR = "/"
        private const val WILDCARD_PATTERN = "**"
    }

    @GetMapping
    fun getRootDescendants(): List<FileInfo> {
        return fileSystemService.getRootDescendants()
    }

    @GetMapping(WILDCARD_PATTERN)
    fun getDescendantsForPath(request: HttpServletRequest): List<FileInfo> {
        val mappingPathComponents = request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE)
                .toString()
                .split(PATH_COMPONENTS_SEPARATOR)
                .dropLast(1)
        val wholeUriPathComponents = request.requestURI.split(PATH_COMPONENTS_SEPARATOR)
        val filePathComponents = wholeUriPathComponents - mappingPathComponents
        return fileSystemService.getDescendantsFor(filePathComponents)
    }
}
