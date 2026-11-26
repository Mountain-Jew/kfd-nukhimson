
package kfd

fun main(args: Array<String>)
{
    val path = args[0]

    val files = analyzeCatalogue(path)

    val extensionStats = makeByExtensionStat(files)

    println("=== Статистика по расширениям ===")

    for (result in extensionStats) {
        println(
            "Расширение: ${result.extensionName} | " +
                    "Файлов: ${result.fileCounter} | " +
                    "Строк: ${result.stringCounter}"
        )
    }

    val largestFiles = findThreeLargest(files)

    println("\n=== Три самых длинных файла ===")

    for (file in largestFiles) {
        println("${file.filePath} — ${file.stringCounter} строк")
    }
}
