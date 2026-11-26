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
        directory.resolve("src").toFile().mkdir()
        directory.resolve("src/A.kt").toFile().writeText("package sample\nclass A")
        directory.resolve("src/B.kt").toFile().writeText("package sample\nclass B\n")
        directory.resolve("README.md").toFile().writeText("")
        directory.resolve("notes.txt").toFile().writeText("Ignored text")

        val result = analyzeCatalogue(directory.toString())

        assertEquals(
            listOf(FileByExtensionStat("kt", 2, 4), FileByExtensionStat("md", 1, 0)),
            makeByExtensionStat(result)
        )
        assertEquals(
            listOf(FileStat("src/A.kt", 2, "kt"), FileStat("src/B.kt", 2, "kt"), FileStat("README.md", 0, "md")),
            findThreeLargest(result)
        )
    }

    @Test
    fun returnsEmptyReportsForEmptyDirectory() {
        val result = analyzeCatalogue(directory.toString())
        assertEquals(emptyList<FileByExtensionStat>(), makeByExtensionStat(result))
        assertEquals(emptyList<FileStat>(), findThreeLargest(result))
    }

    @Test
    fun returnsAllFilesWhenThereAreFewerThanThree() {
        directory.resolve("one.kt").toFile().writeText("one")
        assertEquals(listOf(FileStat("one.kt", 1, "kt")), findThreeLargest(analyzeCatalogue(directory.toString())))
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
