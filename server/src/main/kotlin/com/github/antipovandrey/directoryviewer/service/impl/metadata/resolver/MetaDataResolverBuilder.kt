package com.github.antipovandrey.directoryviewer.service.impl.metadata.resolver

import org.springframework.stereotype.Component

@Component
class MetaDataResolverBuilder {

    /**
     *  Returns formed list of [MetaDataResolver]s with the correct precedence
     *
     *  @return list of [MetaDataResolver]s
     */
    fun buildMetaDataResolverChain(): List<MetaDataResolver> {
        return listOf(
                DirectoryMetaDataResolver(),
                ArchiveMetaDataResolver(),
                ExecutableMetaDataResolver(),
                ImageMetaDataResolver(),
                MusicMetaDataResolver(),
                SourceCodeMetaDataResolver(),
                VideoMetaDataResolver(),
                UnknownMetaDataResolver() // always last
        )
    }
}
