package solutions.puzzle_2022_05

import Puzzle
import ReadInputStrategy

fun main() {
    puzzle.testFirst("CMZ")
    puzzle.solveFirst()

    puzzle.testSecond("MCD")
    puzzle.solveSecond()
}

private val puzzle = Puzzle(
    number = Puzzle.Number(2022, 5),
    firstPart = Puzzle.Part(
        ReadInputStrategy.readGroups,
        ::parseDrawingAndCommands,
        ::getTopCratesAfterRearrangementUsingCrateMover9000
    ),
    secondPart = Puzzle.Part(
        ReadInputStrategy.readGroups,
        ::parseDrawingAndCommands,
        ::getTopCratesAfterRearrangementUsingCrateMover9001
    )
)

private fun parseDrawingAndCommands(groups: List<List<String>>) =
    groups.let { (drawing, commands) -> Procedure(parseDrawing(drawing), parseCommands(commands)) }

private fun parseDrawing(drawing: List<String>): List<ArrayDeque<Char>> {
    val gap = " ".repeat(3)
    val count = drawing.last().trim().split(gap).size

    val stacks = List(count) { ArrayDeque<Char>() }

    val cellPattern = "[?] "
    val itemPosition = cellPattern.indexOf("?")
    val emptyCell = ' '
    val rows = drawing.dropLast(1)
    rows.asReversed().forEach { row ->
        row.chunked(cellPattern.length).forEachIndexed { index, crate ->
            val item = crate[itemPosition].takeIf { it != emptyCell }
            item?.let { stacks[index].addLast(it) }
        }
    }

    return stacks
}

private fun parseCommands(commands: List<String>) = commands.map(Command.Companion::fromString)

private fun getTopCratesAfterRearrangementUsingCrateMover9000(procedure: Procedure) =
    getTopCratesAfterRearrangement(procedure, ::rearrangeByCrateMover9000)

private fun rearrangeByCrateMover9000(stacks: List<ArrayDeque<Char>>, count: Int, from: Int, to: Int) {
    repeat(count) {
        val item = stacks[from].removeLast()
        stacks[to].addLast(item)
    }
}

private fun getTopCratesAfterRearrangementUsingCrateMover9001(procedure: Procedure) =
    getTopCratesAfterRearrangement(procedure, ::rearrangeByCrateMover9001)

private fun rearrangeByCrateMover9001(stacks: List<ArrayDeque<Char>>, count: Int, from: Int, to: Int) {
    stacks[to].addAll(stacks[from].takeLast(count))
    repeat(count) { stacks[from].removeLast() }
}

private fun getTopCratesAfterRearrangement(
    procedure: Procedure,
    rearrangeMethod: (List<ArrayDeque<Char>>, Int, Int, Int) -> Unit
) = procedure.let { (stacks, commands) ->
    commands.forEach { (count, from, to) ->
        rearrangeMethod(stacks, count, from, to)
    }

    stacks.map { it.last() }.joinToString("")
}

private data class Procedure(
    val stacks: List<ArrayDeque<Char>>,
    val commands: List<Command>
)

private data class Command(val count: Int, val from: Int, val to: Int) {
    companion object {
        private val regex = """move (\d+) from (\d+) to (\d+)""".toRegex()
        fun fromString(string: String) =
            regex.find(string)!!.destructured.let { (count, from, to) ->
                Command(count.toInt(), from.toInt() - 1, to.toInt() - 1)
            }
    }
}
