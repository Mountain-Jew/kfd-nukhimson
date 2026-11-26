package kfd

data class FileStat
    (
    val filePath: String,
    val stringCounter:Int = 0,
    val extensionName:String = ""

    )

data class FileByExtensionStat
    (
    val extensionName:String = "",
    val fileCounter: Int = 0,
    val stringCounter:Int = 0,
    )
/*
data class CatalogueStat(
    val extensionStats: List<FileByExtensionStat>,
    val largestFiles: List<FileStat>
)
*/