package com.github.antipovandrey.directoryviewer.service

import com.github.antipovandrey.directoryviewer.model.FileInfo
import com.github.antipovandrey.directoryviewer.model.FileMetaData
import com.github.antipovandrey.directoryviewer.model.FileType
import com.github.antipovandrey.directoryviewer.service.impl.filesystem.FileSystemServiceImpl
import com.github.antipovandrey.directoryviewer.service.impl.filesystem.PathComponentsResolver
import com.github.antipovandrey.directoryviewer.service.impl.filesystem.zip.ArchiveVirtualFileReader
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.util.ResourceUtils

/**
 *  See resources/testdata/structure.md
 */
@SpringBootTest
class FileSystemServiceTest {

    @Autowired
    lateinit var pathComponentsResolver: PathComponentsResolver

    @Autowired
    lateinit var archiveVirtualFileReader: ArchiveVirtualFileReader

    @Autowired
    lateinit var metaDataService: MetaDataService

    lateinit var fileSystemServiceImpl: FileSystemServiceImpl

    @BeforeEach
    fun setUp() {
        fileSystemServiceImpl = FileSystemServiceImpl(
                ResourceUtils.getFile(javaClass.classLoader.getResource("testdata")),
                pathComponentsResolver, archiveVirtualFileReader, metaDataService)
    }

    @Test
    fun `test first layer FileInfo`() {
        val pathComponents = emptyList<String>()
        val expectedInfo = arrayOf(
                FileInfo(pathComponents, "archives", metaData = FileMetaData(FileType.Directory, expandable = true, expandSupported = true)),
                FileInfo(pathComponents, "dir", metaData = FileMetaData(FileType.Directory, expandable = true, expandSupported = true)),
                FileInfo(pathComponents, "movie.avi", metaData = FileMetaData(FileType.Video)),
                FileInfo(pathComponents, "picture.jpg", metaData = FileMetaData(FileType.Image)),
                FileInfo(pathComponents, "structure.md", metaData = FileMetaData(FileType.Unknown))
        )

        val firstLayerDescendants = fileSystemServiceImpl.getDescendantsFor(pathComponents)
        assertThat(firstLayerDescendants, containsInAnyOrder(*expectedInfo))
    }

    @Test
    fun `test nested layer FileInfo`() {
        val pathComponents = listOf("dir")
        val expectedInfo = arrayOf(
                FileInfo(pathComponents, "nested_dir", metaData = FileMetaData(FileType.Directory, expandable = true, expandSupported = true)),
                FileInfo(pathComponents, "nested_source.ts", metaData = FileMetaData(FileType.SourceCode))
        )

        val firstLayerDescendants = fileSystemServiceImpl.getDescendantsFor(pathComponents)
        assertThat(firstLayerDescendants, containsInAnyOrder(*expectedInfo))
    }


    @Test
    fun `test deeply nested layer FileInfo`() {
        val pathComponents = listOf("dir", "nested_dir")
        val expectedInfo = arrayOf(
                FileInfo(pathComponents, "deep_nested_file", metaData = FileMetaData(FileType.Unknown))
        )

        val firstLayerDescendants = fileSystemServiceImpl.getDescendantsFor(pathComponents)
        assertThat(firstLayerDescendants, containsInAnyOrder(*expectedInfo))
    }

    @Test
    fun `test archive detection`() {
        val pathComponents = listOf("archives")
        val expectedInfo = arrayOf(
                FileInfo(pathComponents, "nested", metaData = FileMetaData(FileType.Directory, expandable = true, expandSupported = true)),
                FileInfo(pathComponents, "ziparchive.zip", metaData = FileMetaData(FileType.Archive, expandable = true, expandSupported = true)),
                FileInfo(pathComponents, "unsupported.rar", metaData = FileMetaData(FileType.Archive, expandable = true, expandSupported = false))
        )

        val firstLayerDescendants = fileSystemServiceImpl.getDescendantsFor(pathComponents)
        assertThat(firstLayerDescendants, containsInAnyOrder(*expectedInfo))
    }

    @Test
    fun `test FileInfo inside of zip`() {
        val pathComponents = listOf("archives", "ziparchive.zip")
        val expectedInfo = arrayOf(
                FileInfo(pathComponents, "dir", metaData = FileMetaData(FileType.Directory, expandable = true, expandSupported = true)),
                FileInfo(pathComponents, "movie.avi", metaData = FileMetaData(FileType.Video)),
                FileInfo(pathComponents, "picture.jpg", metaData = FileMetaData(FileType.Image)),
                FileInfo(pathComponents, "structure.md", metaData = FileMetaData(FileType.Unknown))
        )

        val firstLayerDescendants = fileSystemServiceImpl.getDescendantsFor(pathComponents)
        assertThat(firstLayerDescendants, containsInAnyOrder(*expectedInfo))
    }

    @Test
    fun `test FileInfo inside of zip in directory`() {
        val pathComponents = listOf("archives", "ziparchive.zip", "dir")
        val expectedInfo = arrayOf(
                FileInfo(pathComponents, "nested_dir", metaData = FileMetaData(FileType.Directory, expandable = true, expandSupported = true)),
                FileInfo(pathComponents, "nested_source.ts", metaData = FileMetaData(FileType.SourceCode))
        )

        val firstLayerDescendants = fileSystemServiceImpl.getDescendantsFor(pathComponents)
        assertThat(firstLayerDescendants, containsInAnyOrder(*expectedInfo))
    }

    @Test
    fun `test FileInfo inside of a very deeply nested zip`() {
        val pathComponents = listOf("archives", "nested", "lvl1.zip", "lvl2.zip", "lvl2", "lvl3.zip", "lvl3", "dir1")
        val expectedInfo = arrayOf(
                FileInfo(pathComponents, "dir2", metaData = FileMetaData(FileType.Directory, expandable = true, expandSupported = true))
        )

        val firstLayerDescendants = fileSystemServiceImpl.getDescendantsFor(pathComponents)
        assertThat(firstLayerDescendants, containsInAnyOrder(*expectedInfo))
    }
}
