package solutions.puzzle_2022_06

import ExampleSuffix
import ParseInputStrategy
import Puzzle
import ReadInputStrategy

fun main() {
    puzzle.testFirst(mapOf(
        ExampleSuffix.A to 7,
        ExampleSuffix.B to 5,
        ExampleSuffix.C to 6,
        ExampleSuffix.D to 10,
        ExampleSuffix.E to 11,
    ))
    puzzle.solveFirst()

    puzzle.testSecond(mapOf(
        ExampleSuffix.A to 19,
        ExampleSuffix.B to 23,
        ExampleSuffix.C to 23,
        ExampleSuffix.D to 29,
        ExampleSuffix.E to 26,
    ))
    puzzle.solveSecond()
}

private val puzzle = Puzzle(
    number = Puzzle.Number(2022, 6),
    firstPart = Puzzle.Part(
        ReadInputStrategy.readRaw,
        ParseInputStrategy.leaveAsString,
        ::computePartOne
    ),
    secondPart = Puzzle.Part(
        ReadInputStrategy.readRaw,
        ParseInputStrategy.leaveAsString,
        ::computePartTwo
    )
)

private fun computePartOne(stream: String) = findMarkerPosition(stream, 4)

private fun computePartTwo(stream: String) = findMarkerPosition(stream, 14)

private fun findMarkerPosition(stream: String, size: Int): Int {
    val windowIndex = stream.windowed(size).map { it.toSet() }.indexOfFirst { it.size == size }
    check(windowIndex >= 0)

    return windowIndex + size
}