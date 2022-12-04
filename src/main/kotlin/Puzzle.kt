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

    private fun getFile(extension: String) =
        File("src/main/resources", "%d/%02d.%s".format(number.year, number.day, extension))

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

        fun run(): Result {
            return solve(inputFile)
        }

        private fun solve(file: File) = file.let(::read).let(::parse).let(::compute)
    }

    data class Number(val year: Int, val day: Int)
}

data class PartProperties<A, B, C>(
    val readStrategy: ReadInputStrategy<A>,
    val parseStrategy: ParseInputStrategy<A, B>,
    val computeStrategy: ComputeSolutionStrategy<B, C>
)
