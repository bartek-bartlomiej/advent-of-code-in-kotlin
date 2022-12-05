package solutions.puzzle_2022_05

import Puzzle
import ReadInputStrategy
import java.util.*

fun main() {
    puzzle.testFirst("CMZ")
    puzzle.solveFirst()

    puzzle.testSecond("")
    puzzle.solveSecond()
}

private val puzzle = Puzzle(
    number = Puzzle.Number(2022, 5),
    firstPart = Puzzle.Part(
        ReadInputStrategy.readGroups,
        ::parseDrawingAndCommands,
        ::getTopCratesAfterRearrangement
    ),
    secondPart = Puzzle.Part(
        ReadInputStrategy.readRaw,
        { it: String -> it },
        ::computePartTwo
    )
)

private fun parseDrawingAndCommands(groups: List<List<String>>) =
    groups.let { (drawing, commands) -> Pair(parseDrawing(drawing), parseCommands(commands)) }

private fun parseDrawing(drawing: List<String>): List<Stack<Char>> {
    val gap = " ".repeat(3)
    val count = drawing.last().trim().split(gap).size

    val stacks = List(count) { Stack<Char>() }

    val cellPattern = "[?] "
    val itemPosition = cellPattern.indexOf("?")
    val emptyCell = ' '
    val rows = drawing.dropLast(1)
    rows.asReversed().forEach { row ->
        row.chunked(cellPattern.length).forEachIndexed { index, crate ->
            val item = crate[itemPosition].takeIf { it != emptyCell }
            item?.let { stacks[index].push(it) }
        }
    }

    return stacks
}

private fun parseCommands(commands: List<String>) = commands.map(Command.Companion::fromString)

private data class Command(val count: Int, val from: Int, val to: Int) {
    companion object {
        private val regex = """move (\d+) from (\d+) to (\d+)""".toRegex()
        fun fromString(string: String) =
            regex.find(string)!!.destructured.let { (count, from, to) ->
                Command(count.toInt(), from.toInt() - 1, to.toInt() - 1)
            }
    }
}

private fun getTopCratesAfterRearrangement(groups: Pair<List<Stack<Char>>, List<Command>>) =
    groups.let { (stacks, commands) ->
        commands.forEach { (count, from, to) ->
            repeat(count) {
                val item = stacks[from].pop()
                stacks[to].push(item)
            }
        }

        stacks.map { it.peek() }.joinToString("")
    }

private fun computePartTwo(it: String) = ""
