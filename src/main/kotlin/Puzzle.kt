import strategies.ComputeSolutionStrategy
import strategies.ParseInputStrategy
import strategies.ReadInputStrategy
import java.io.File


class Puzzle<Input, Data, Result>(
    private val number: Number,
    firstPart: PartProperties<Input, Data, Result>,
    secondPart: PartProperties<Input, Data, Result>
) {
    private val inputFile: File = getFile("input")

    private fun getExampleFile(suffix: ExampleSuffix) = getFile("example", suffix)

    private fun getFile(extension: String, suffix: ExampleSuffix = ExampleSuffix.None) =
        File("src/main/resources", "%d/%02d%s.%s".format(number.year, number.day, suffix.asString, extension))

    private val parts = listOf(firstPart, secondPart).map(::Part)

    init {
        run()
    }

    private fun run() = parts.forEach { part -> println(part.run()) }

    private inner class Part<Input, Data, Result>(
        properties: PartProperties<Input, Data, Result>
    ) : ReadInputStrategy<Input> by properties.readStrategy,
        ParseInputStrategy<Input, Data> by properties.parseStrategy,
        ComputeSolutionStrategy<Data, Result> by properties.computeStrategy {

        private val tests = properties.tests

        fun run(): Result {
            performTests()
            return solve(inputFile)
        }

        private fun performTests() {
            tests.forEach(::performTest)
        }

        private fun performTest(suffix: ExampleSuffix, expectedResult: Result) {
            val result = solve(getExampleFile(suffix))
            check(expectedResult == result) { "Expected $expectedResult, got $result" }
        }

        private fun solve(file: File) = file.let(::read).let(::parse).let(::compute)
    }



    data class Number(val year: Int, val day: Int)
}

enum class ExampleSuffix(val asString: String) {
    A("a"),
    B("b"),
    C("c"),
    None("")
}

data class PartProperties<Input, Data, Result>(
    val readStrategy: ReadInputStrategy<Input>,
    val parseStrategy: ParseInputStrategy<Input, Data>,
    val computeStrategy: ComputeSolutionStrategy<Data, Result>,
    val tests: Map<ExampleSuffix, Result>
) {
    constructor(
        readStrategy: ReadInputStrategy<Input>,
        parseStrategy: ParseInputStrategy<Input, Data>,
        computeStrategy: ComputeSolutionStrategy<Data, Result>,
        test: Result
    ) : this(readStrategy, parseStrategy, computeStrategy, mapOf( ExampleSuffix.None to test))
}
