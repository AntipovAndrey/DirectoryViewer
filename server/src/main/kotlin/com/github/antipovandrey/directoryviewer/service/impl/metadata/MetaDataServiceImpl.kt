package com.github.antipovandrey.directoryviewer.service.impl.metadata

import com.github.antipovandrey.directoryviewer.model.FileMetaData
import com.github.antipovandrey.directoryviewer.service.MetaDataService
import com.github.antipovandrey.directoryviewer.service.exception.MetaDataResolvingException
import com.github.antipovandrey.directoryviewer.service.impl.metadata.resolver.MetaDataResolverBuilder
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.File

@Service
class MetaDataServiceImpl(
        resolverBuilder: MetaDataResolverBuilder
) : MetaDataService {

    private val log = LoggerFactory.getLogger(MetaDataServiceImpl::class.java)

    private val resolvers = resolverBuilder.buildMetaDataResolverChain()

    override fun getMetaData(file: File): FileMetaData {
        for (resolver in resolvers) {
            val metaData = resolver.resolveMetaData(file)
            if (metaData != null) {
                log.info("Resolved metadata for {}: {}", file, metaData)
                return metaData
            }
        }
        log.error("No resolver found: {}", resolvers)
        throw throw MetaDataResolvingException("Can not resolve metadata for $file")
    }
}
