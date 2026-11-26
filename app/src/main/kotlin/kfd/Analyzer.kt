package kfd
import java.io.File
import java.nio.file.Files

fun analyzeCatalogue(path: String):List<FileStat>
{

    val root = File(path)
    require(root.isDirectory) { "Указанный путь не является каталогом: $path" }
    val excluded = setOf(".git", ".gradle", "build")
    val extensions = setOf("kt", "kts", "md")
    val makeStat:(File) -> FileStat =
        { file -> FileStat(
            file.relativeTo(root).invariantSeparatorsPath,
            file.useLines { lines -> lines.count() },
            file.extension)
        }

    return root.walkTopDown()
        .onEnter { directory ->
            directory.name !in excluded && !Files.isSymbolicLink(directory.toPath())
        }
        .filter { file ->
            file.isFile && file.extension in extensions &&
                    !Files.isSymbolicLink(file.toPath())
        }
        .map(makeStat)
        .toList()
}

fun makeByExtensionStat(files: List<FileStat>): List<FileByExtensionStat> {
    val grouped = files.groupBy { it.extensionName }

    return grouped.map { (extension, group) ->
        FileByExtensionStat(
            extension,
            group.size,
            group.sumOf { it.stringCounter }
        )
    }.sortedBy { it.extensionName }
}

fun findThreeLargest(fileList: List<FileStat>): List<FileStat> {
    val resultList = fileList.sortedWith(compareByDescending<FileStat>
    { it.stringCounter }.thenBy { it.filePath })
    return resultList.take(3)
}




