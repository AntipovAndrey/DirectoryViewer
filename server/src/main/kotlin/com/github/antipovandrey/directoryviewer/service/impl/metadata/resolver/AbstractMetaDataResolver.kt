package com.github.antipovandrey.directoryviewer.service.impl.metadata.resolver

import com.github.antipovandrey.directoryviewer.model.FileMetaData
import java.io.File
import java.io.IOException

abstract class AbstractMetaDataResolver(
        private val supportedExtensions: Set<String>
) : MetaDataResolver {

    final override fun resolveMetaData(path: File): FileMetaData? {
        try {
            if (!isSupported(path)) {
                return null
            }
            return resolve(path)
        } catch (ex: IOException) {
            // todo: remove tracing, add log
            ex.printStackTrace()
        }
        return null
    }

    /**
     * Denotes if this path could be handled with this resolver
     *
     * @param path absolute path to create metadata
     * @return true if this resolver can handle the path
     */
    protected fun isSupported(path: File): Boolean {
        return isExtensionSupported(path)
    }

    protected fun isExtensionSupported(path: File): Boolean {
        return path.extension in supportedExtensions
    }

    /**
     *  Performs actual resolving file to metadata.
     *  The method will not be called if [isSupported] if false.
     *
     *  @param path absolute path to create metadata
     *  @return formed [FileMetaData] for this path
     */
    protected abstract fun resolve(path: File): FileMetaData
}