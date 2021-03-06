package com.github.antipovandrey.directoryviewer.controller

import com.github.antipovandrey.directoryviewer.model.FileInfo
import com.github.antipovandrey.directoryviewer.service.FileSystemService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.HandlerMapping
import java.net.URLDecoder
import javax.servlet.http.HttpServletRequest

@RequestMapping
@RestController
class DirectoryViewerController(
        private val fileSystemService: FileSystemService
) {

    companion object {

        private const val PATH_COMPONENTS_SEPARATOR = "/"
        private const val WILDCARD_PATTERN = "**"
    }

    private val log = LoggerFactory.getLogger(DirectoryViewerController::class.java)

    @GetMapping("root")
    fun getRootInfo(): FileInfo {
        return fileSystemService.getRootInfo()
    }

    @GetMapping("view/$WILDCARD_PATTERN")
    fun getDescendantsForPath(request: HttpServletRequest): List<FileInfo> {
        val mappingPathComponentsCount = request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE)
                .toString()
                .split(PATH_COMPONENTS_SEPARATOR)
                .dropLast(1)
                .size
        val uriPathComponents = request.requestURI.split(PATH_COMPONENTS_SEPARATOR)
                .drop(mappingPathComponentsCount)
                .map { URLDecoder.decode(it, "UTF-8"); }
        log.info("Requested descendant for path: {} original request path: {}", uriPathComponents, request.requestURI)
        return fileSystemService.getDescendantsFor(uriPathComponents)
    }
}
