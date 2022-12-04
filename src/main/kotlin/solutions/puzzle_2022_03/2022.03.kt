package solutions.puzzle_2022_03

import ParseInputStrategy
import Puzzle
import ReadInputStrategy

fun main() {
    puzzle.testFirst(157)
    puzzle.solveFirst()

    puzzle.testSecond(70)
    puzzle.solveSecond()
}

private val puzzle = Puzzle(
    number = Puzzle.Number(2022, 3),
    firstPart = Puzzle.Part(
        ReadInputStrategy.readLines,
        ParseInputStrategy.parseLines(::parseRucksackDescription),
        ::sumPrioritiesOfSharedItems
    ),
    secondPart = Puzzle.Part(
        ReadInputStrategy.readLines,
        ParseInputStrategy.parseLines(::parseRucksackDescription),
        ::sumPrioritiesOfStickers
    )
)

private fun parseRucksackDescription(rucksack: String): Rucksack {
    val (first, second) = rucksack.chunked(rucksack.length / 2).map(String::toSet)
    return Rucksack(first, second)
}

private fun sumPrioritiesOfSharedItems(rucksacks: List<Rucksack>): Int =
    rucksacks.map(::findSharedItemInRucksack).let(::sumPriorities)

private fun findSharedItemInRucksack(rucksack: Rucksack) =
    rucksack.let { (first, second) -> (first.intersect(second)).single() }

private fun sumPrioritiesOfStickers(rucksacks: List<Rucksack>): Int =
    rucksacks.chunked(3).map(::findSticker).let(::sumPriorities)

private fun findSticker(rucksacks: List<Rucksack>) =
    rucksacks.map { it.firstCompartment + it.secondCompartment }
        .reduce { intersect, items -> intersect.intersect(items) }
        .single()

private fun sumPriorities(items: List<Char>) = items.sumOf { priorities[it]!! }

private data class Rucksack(val firstCompartment: Set<Char>, val secondCompartment: Set<Char>)

private const val ALPHABET = "abcdefghijklmnopqrstuvwxyz"
private val priorities = (ALPHABET + ALPHABET.uppercase()).mapIndexed { index, letter -> letter to index + 1 }.toMap()
