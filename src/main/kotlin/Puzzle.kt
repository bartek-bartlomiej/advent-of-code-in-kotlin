import java.io.File


class Puzzle<A, B, FirstResult, J, K, SecondResult>(
    private val number: Number,
    private val firstPart: Part<A, B, FirstResult>,
    private val secondPart: Part<J, K, SecondResult>
) {
    fun solveFirst() {
        solvePart(firstPart)
    }

    fun solveSecond() {
        solvePart(secondPart)
    }

    private fun <Input, Data, Result> solvePart(part: Part<Input, Data, Result>) {
        val inputFile = getInputFile()
        println(part.solve(inputFile))
    }

    fun testFirst(expected: FirstResult) {
        testPart(firstPart, expected)
    }

    fun testFirst(tests: Map<ExampleSuffix, FirstResult>) {
        testPart(firstPart, tests)
    }

    fun testSecond(expected: SecondResult) {
        testPart(secondPart, expected)
    }

    fun testSecond(tests: Map<ExampleSuffix, SecondResult>) {
        testPart(secondPart, tests)
    }

    private fun <Input, Data, Result> testPart(part: Part<Input, Data, Result>, expected: Result) {
        testPart(part, mapOf(ExampleSuffix.None to expected))
    }

    private fun <Input, Data, Result> testPart(part: Part<Input, Data, Result>, tests: Map<ExampleSuffix, Result>) {
        tests.forEach { (suffix, expectedResult) ->
            part.test(getExampleFile(suffix), expectedResult)
        }
    }

    private fun getInputFile() = getFile(FileType.Input)

    private fun getExampleFile(suffix: ExampleSuffix) = getFile(FileType.Example, suffix)

    private fun getFile(type: FileType, suffix: ExampleSuffix = ExampleSuffix.None) =
        File("src/main/resources", "%d/%02d%s.%s".format(number.year, number.day, suffix.pathPart, type.pathPart))

    class Part<Input, Data, Result>(
        private val read: (File) -> Input,
        private val parse: (Input) -> Data,
        private val compute: (Data) -> Result
    ) {
        fun solve(file: File) = file.let(read).let(parse).let(compute)

        fun test(file: File, expectedResult: Result) {
            val result = solve(file)
            check(expectedResult == result) { "Expected $expectedResult, got $result" }
        }
    }

    data class Number(val year: Int, val day: Int)
}

enum class FileType(val pathPart: String) {
    Input("input"), Example("example")
}

enum class ExampleSuffix(val pathPart: String) {
    A("a"), B("b"), C("c"), D("d"), E("e"), None("")
}
