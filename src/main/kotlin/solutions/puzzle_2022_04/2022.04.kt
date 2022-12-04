package solutions.puzzle_2022_04

import ParseInputStrategy
import Puzzle
import ReadInputStrategy

fun main() {
    puzzle.testFirst(2)
    puzzle.solveFirst()

    puzzle.testSecond(4)
    puzzle.solveSecond()
}

private val puzzle = Puzzle(
    number = Puzzle.Number(2022, 4),
    firstPart = Puzzle.Part(
        ReadInputStrategy.readLines,
        ParseInputStrategy.parseLines(::parseRanges),
        ::countFullyContainedRanges
    ),
    secondPart = Puzzle.Part(
        ReadInputStrategy.readLines,
        ParseInputStrategy.parseLines(::parseRanges),
        ::countOverlappingRanges
    )
)

private fun parseRanges(line: String): Pair<IntRange, IntRange> {
    val (firstBounds, secondBounds) = line.split(",").map { it.split("-").map(String::toInt) }
    return Pair(
        firstBounds.first()..firstBounds.last(),
        secondBounds.first()..secondBounds.last()
    )
}

private fun countFullyContainedRanges(pairs: List<Pair<IntRange, IntRange>>): Int =
    pairs.count { (first, second) -> first.isFullyContainedIn(second) || second.isFullyContainedIn(first) }

private fun IntRange.isFullyContainedIn(other: IntRange) = first in other && last in other

private fun countOverlappingRanges(pairs: List<Pair<IntRange, IntRange>>): Int =
    pairs.count { (first, second) -> first.isContainedIn(second) || second.isContainedIn(first) }

private fun IntRange.isContainedIn(other: IntRange) = first in other || last in other
