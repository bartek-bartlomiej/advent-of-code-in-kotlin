package strategies

fun interface ParseInputStrategy<Input, Data> {
    fun parse(input: Input): Data

    companion object {
        fun <T> parseGroups(parseRow: ParseInputStrategy<List<String>, List<T>>) =
            ParseInputStrategy { rawInput: String ->
                rawInput
                    .split("\n\n")
                    .map { groupRows -> groupRows.split("\n") }
                    .map(parseRow::parse)
            }

        val parseToInts = ParseInputStrategy { list: List<String> -> list.map(String::toInt) }
    }
}