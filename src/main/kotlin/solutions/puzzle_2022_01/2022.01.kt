package solutions.puzzle_2022_01

import Puzzle
import Puzzle.Part.Number

fun computePartOne(inventories: List<List<Int>>): Int = inventories.maxOf(List<Int>::sum)

fun computePartTwo(inventories: List<List<Int>>): Int = getMaxThreeUsingFold(inventories)

fun getMaxThreeUsingSort(inventories: List<List<Int>>): Int =
    inventories.map(List<Int>::sum).sorted().takeLast(3).sum()

fun getMaxThreeUsingFold(inventories: List<List<Int>>): Int =
    inventories.fold(listOf(0, 0, 0)) { podium, inventory ->
        (podium + inventory.sum()).sortedDescending().take(3)
    }.sum()


val puzzle = Puzzle(
    number = Puzzle.Number(2022, 1),
    firstPart = Puzzle.Part(
        ReadInputStrategy.readRaw,
        ParseInputStrategy.parseGroups(ParseInputStrategy.parseToInts),
        ::computePartOne
    ),
    secondPart = Puzzle.Part(
        ReadInputStrategy.readRaw,
        ParseInputStrategy.parseGroups(ParseInputStrategy.parseToInts),
        ::computePartTwo
    )
)

fun main() {
    puzzle.test(Number.First, 24000)
    puzzle.solve(Number.First)

    puzzle.test(Number.Second, 45000)
    puzzle.solve(Number.Second)
}
