package com.github.antipovandrey.directoryviewer.service.impl.metadata.resolver

import org.springframework.stereotype.Component

@Component
class MetaDataResolverBuilder(
        private val directoryMetaDataResolver: DirectoryMetaDataResolver,
        private val archiveMetaDataResolver: ArchiveMetaDataResolver,
        private val executableMetaDataResolver: ExecutableMetaDataResolver,
        private val imageMetaDataResolver: ImageMetaDataResolver,
        private val musicMetaDataResolver: MusicMetaDataResolver,
        private val sourceCodeMetaDataResolver: SourceCodeMetaDataResolver,
        private val videoMetaDataResolver: VideoMetaDataResolver,
        private val unknownMetaDataResolver: UnknownMetaDataResolver
) {

    /**
     *  Returns formed list of [MetaDataResolver]s with the correct precedence
     *
     *  @return list of [MetaDataResolver]s
     */
    fun buildMetaDataResolverChain(): List<MetaDataResolver> {
        return listOf(
                directoryMetaDataResolver,
                archiveMetaDataResolver,
                executableMetaDataResolver,
                imageMetaDataResolver,
                musicMetaDataResolver,
                sourceCodeMetaDataResolver,
                videoMetaDataResolver,
                unknownMetaDataResolver // always last
        )
    }
}
