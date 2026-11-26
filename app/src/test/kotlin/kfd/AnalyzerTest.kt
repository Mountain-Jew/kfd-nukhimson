package kfd

import java.nio.file.Path
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir

class AnalyzerTest {
    @TempDir
    lateinit var directory: Path

    @Test
    fun groupsFilesRecursivelyAndRanksIndividualFiles() {
        directory.resolve("nested").toFile().mkdir()
        directory.resolve("nested/a.kt").toFile().writeText("one\n\nthree\n")
        directory.resolve("b.kt").toFile().writeText("one\ntwo")
        directory.resolve("a.txt").toFile().writeText("one\r\ntwo")
        directory.resolve("README").toFile().writeText("")

        val result = analyzeCatalogue(directory.toString())

        assertEquals(
            mapOf(
                "kt" to FileByExtensionStat("kt", 2, 5),
                "txt" to FileByExtensionStat("txt", 1, 2),
                "" to FileByExtensionStat("", 1, 0)
            ),
            result.extensionStats.associateBy { it.extensionName }
        )
        assertEquals(
            listOf(
                FileStat(directory.resolve("nested/a.kt").toString(), 3, "kt"),
                FileStat(directory.resolve("a.txt").toString(), 2, "txt"),
                FileStat(directory.resolve("b.kt").toString(), 2, "kt")
            ),
            result.largestFiles
        )
    }

    @Test
    fun returnsEmptyReportsForEmptyDirectory() {
        assertEquals(CatalogueStat(emptyList(), emptyList()), analyzeCatalogue(directory.toString()))
    }

    @Test
    fun returnsAllFilesWhenThereAreFewerThanThree() {
        val path = directory.resolve("one.txt")
        path.toFile().writeText("one")

        assertEquals(listOf(FileStat(path.toString(), 1, "txt")), analyzeCatalogue(directory.toString()).largestFiles)
    }

    @Test
    fun rejectsMissingPathAndRegularFile() {
        val missing = directory.resolve("missing")
        val file = directory.resolve("file.txt")
        file.toFile().writeText("one")

        assertThrows(IllegalArgumentException::class.java) { analyzeCatalogue(missing.toString()) }
        assertThrows(IllegalArgumentException::class.java) { analyzeCatalogue(file.toString()) }
    }
}
