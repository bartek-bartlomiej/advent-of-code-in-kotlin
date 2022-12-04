package solutions.puzzle_2022_03

import ParseInputStrategy
import Puzzle
import ReadInputStrategy

fun main() {
    puzzle.testFirst(157)
    puzzle.solveFirst()

    puzzle.testSecond(0)
    puzzle.solveSecond()
}

val puzzle = Puzzle(
    number = Puzzle.Number(2022, 3),
    firstPart = Puzzle.Part(
        ReadInputStrategy.readLines,
        ParseInputStrategy.parseLines(::parseSingleRucksackDescription),
        ::sumPriorities
    ),
    secondPart = Puzzle.Part(
        ReadInputStrategy.readLines,
        ParseInputStrategy.parseLines { it },
        ::computePartTwo
    )
)

fun parseSingleRucksackDescription(rucksack: String): Int {
    val (first, second) = rucksack.chunked(rucksack.length / 2).map(String::toSet)
    val sharedItem = (first.intersect(second)).single()
    return priorities[sharedItem]!!
}

fun sumPriorities(priorities: List<Int>) = priorities.sum()

fun computePartTwo(lines: List<String>) = 0

const val alphabet = "abcdefghijklmnopqrstuvwxyz"
val priorities = (alphabet + alphabet.uppercase()).mapIndexed { index, letter -> letter to index + 1 }.toMap()
