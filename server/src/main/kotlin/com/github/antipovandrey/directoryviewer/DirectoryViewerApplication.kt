package com.github.antipovandrey.directoryviewer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DirectoryViewerApplication

fun main(args: Array<String>) {
    runApplication<DirectoryViewerApplication>(*args)
}
