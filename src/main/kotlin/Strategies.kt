import java.io.File

object ParseInputStrategy {
    fun <T> parseGroups(parseRow: (List<String>) -> List<T>) = { rawInput: String ->
        rawInput
            .trimEnd()
            .split("\n\n")
            .map { groupRows -> groupRows.split("\n") }
            .map(parseRow)
    }

    val parseToInts = { list: List<String> ->
        list.map(String::toInt)
    }
}

object ReadInputStrategy {
    val readRaw = { file: File -> file.readText() }
    val readLines = { file: File -> file.readLines() }
}
