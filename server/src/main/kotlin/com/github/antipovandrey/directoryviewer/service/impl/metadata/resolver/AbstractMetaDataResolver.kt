package com.github.antipovandrey.directoryviewer.service.impl.metadata.resolver

import com.github.antipovandrey.directoryviewer.model.FileMetaData
import com.github.antipovandrey.directoryviewer.model.VirtualFile
import org.slf4j.LoggerFactory
import java.io.IOException

abstract class AbstractMetaDataResolver(
        private val supportedExtensions: Set<String>
) : MetaDataResolver {

    private val log = LoggerFactory.getLogger(AbstractMetaDataResolver::class.java)

    final override fun resolveMetaData(virtualFile: VirtualFile): FileMetaData? {
        try {
            if (!isSupported(virtualFile)) {
                return null
            }
            return resolve(virtualFile)
        } catch (ex: IOException) {
            log.warn("IOException during metadata collecting", ex)
        }
        return null
    }

    /**
     * Denotes if this file could be handled with this resolver
     *
     * @param virtualFile virtual file to create metadata
     * @return true if this resolver can handle the path
     */
    protected fun isSupported(virtualFile: VirtualFile): Boolean {
        return isExtensionSupported(virtualFile)
    }

    protected fun isExtensionSupported(virtualFile: VirtualFile): Boolean {
        return virtualFile.extension in supportedExtensions
    }

    /**
     *  Performs actual resolving file to metadata.
     *  The method will not be called if [isSupported] if false.
     *
     *  @param virtualFile virtual file to create metadata
     *  @return formed [FileMetaData] for this path
     */
    protected abstract fun resolve(virtualFile: VirtualFile): FileMetaData
}