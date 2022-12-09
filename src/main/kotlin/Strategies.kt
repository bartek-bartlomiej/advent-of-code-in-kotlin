import java.io.File

object ParseInputStrategy {
    val leaveAsString: (String) -> String = { it }

    fun <T> parseGroups(parseGroup: (List<String>) -> List<T>) = { groups: List<List<String>> -> groups.map(parseGroup) }

    fun <T> parseLines(parseRow: (String) -> T) = { lines: List<String> -> lines.map(parseRow) }

    val parseToInts = parseLines(String::toInt)
}

object ReadInputStrategy {
    val readRaw = { file: File -> file.readText() }

    val readGroups = { file: File ->
        file.let(readRaw)
            .let { rawInput ->
                rawInput
                    .trimEnd()
                    .split("\n\n")
                    .map { groupRows ->
                        groupRows.split("\n")
                }
            }
    }

    val readLines = { file: File -> file.readLines() }
}
