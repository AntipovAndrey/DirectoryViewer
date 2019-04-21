package com.github.antipovandrey.directoryviewer.controller

import com.github.antipovandrey.directoryviewer.service.FileSystemService
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.setup.MockMvcBuilders


class DirectoryViewerControllerTest {

    lateinit var mvc: MockMvc

    @MockK
    lateinit var fileSystemService: FileSystemService

    @InjectMockKs
    lateinit var controller: DirectoryViewerController

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        mvc = MockMvcBuilders.standaloneSetup(controller).build()

        every { fileSystemService.getDescendantsFor(any()) } returns emptyList()
    }

    @Test
    fun `tes root endpoint called`() {
        mvc.perform(get("/view"))

        verify { fileSystemService.getDescendantsFor(emptyList()) }
    }

    @Test
    fun `test endpoint called with path`() {
        mvc.perform(get("/view/com/example/test/file"))

        verify { fileSystemService.getDescendantsFor(listOf("com", "example", "test", "file")) }
    }

    @Test
    fun `test endpoint called with file extension`() {
        mvc.perform(get("/view/com/file.zip"))

        verify { fileSystemService.getDescendantsFor(listOf("com", "file.zip")) }
    }

    @Test
    fun `test endpoint called with encoded path`() {
        mvc.perform(get("/view/co m/file!"))

        verify { fileSystemService.getDescendantsFor(listOf("co m", "file!")) }
    }
}