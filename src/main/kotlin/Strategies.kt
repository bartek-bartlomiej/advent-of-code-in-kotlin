import java.io.File

object ParseInputStrategy {
    fun <T> parseGroups(parseGroup: (List<String>) -> List<T>) = { rawInput: String ->
        rawInput.trimEnd().split("\n\n")
            .map { groupRows -> groupRows.split("\n") }
            .map(parseGroup)
    }

    fun <T> parseLines(parseRow: (String) -> T) = { lines: List<String> -> lines.map(parseRow) }

    val parseToInts = parseLines(String::toInt)
}

object ReadInputStrategy {
    val readRaw = { file: File -> file.readText() }
    val readLines = { file: File -> file.readLines() }
}
