import java.io.File


class Puzzle<Input, Data, Result>(
    private val number: Number,
    firstPart: Part<Input, Data, Result>,
    secondPart: Part<Input, Data, Result>
) {
    private val parts = mapOf(
        Part.Number.First to firstPart,
        Part.Number.Second to secondPart
    )

    fun solve(partNumber: Part.Number) {
        solvePart(parts[partNumber]!!)
    }

    private fun solvePart(part: Part<Input, Data, Result>) {
        val inputFile = getInputFile()
        println(part.solve(inputFile))
    }

    fun test(partNumber: Part.Number, expected: Result) {
        testPart(parts[partNumber]!!, expected)
    }

    fun test(partNumber: Part.Number, tests: Map<ExampleSuffix, Result>) {
        testPart(parts[partNumber]!!, tests)
    }

    private fun testPart(part: Part<Input, Data, Result>, expected: Result) {
        testPart(part, mapOf(ExampleSuffix.None to expected))
    }

    private fun testPart(part: Part<Input, Data, Result>, tests: Map<ExampleSuffix, Result>) {
        tests.forEach { (suffix, expectedResult) ->
            part.test(getExampleFile(suffix), expectedResult)
        }
    }

    private fun getInputFile() = getFile(FileType.Input)

    private fun getExampleFile(suffix: ExampleSuffix) = getFile(FileType.Example, suffix)

    private fun getFile(type: FileType, suffix: ExampleSuffix = ExampleSuffix.None) =
        File("src/main/resources", "%d/%02d%s.%s".format(number.year, number.day, suffix.pathPart, type.pathPart))

    class Part<Input, Data, Result>(
        private val read: (File) -> Input, private val parse: (Input) -> Data, private val compute: (Data) -> Result
    ) {
        fun solve(file: File) = file.let(read).let(parse).let(compute)

        fun test(file: File, expectedResult: Result) {
            val result = solve(file)
            check(expectedResult == result) { "Expected $expectedResult, got $result" }
        }

        enum class Number {
            First, Second
        }
    }

    data class Number(val year: Int, val day: Int)
}


enum class FileType(val pathPart: String) {
    Input("input"), Example("example")
}

enum class ExampleSuffix(val pathPart: String) {
    A("a"), B("b"), C("c"), None("")
}
