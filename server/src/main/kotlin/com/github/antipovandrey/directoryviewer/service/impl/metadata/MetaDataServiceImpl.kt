package com.github.antipovandrey.directoryviewer.service.impl.metadata

import com.github.antipovandrey.directoryviewer.model.FileMetaData
import com.github.antipovandrey.directoryviewer.service.MetaDataService
import com.github.antipovandrey.directoryviewer.service.exception.MetaDataResolvingException
import com.github.antipovandrey.directoryviewer.service.impl.metadata.resolver.MetaDataResolverBuilder
import org.springframework.stereotype.Service
import java.io.File

@Service
class MetaDataServiceImpl(
        resolverBuilder: MetaDataResolverBuilder
) : MetaDataService {

    private val resolvers = resolverBuilder.buildMetaDataResolverChain()

    override fun getMetaData(file: File): FileMetaData {
        for (resolver in resolvers) {
            val metaData = resolver.resolveMetaData(file)
            if (metaData != null) {
                return metaData
            }
        }
        throw throw MetaDataResolvingException("Can not resolve metadata for $file")
    }
}
