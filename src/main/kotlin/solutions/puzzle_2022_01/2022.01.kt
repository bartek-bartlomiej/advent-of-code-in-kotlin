package solutions.puzzle_2022_01

import Puzzle

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
    puzzle.testFirst(24000)
    puzzle.solveFirst()

    puzzle.testSecond(45000)
    puzzle.solveSecond()
}

fun computePartOne(inventories: List<List<Int>>) = getMaxInventory(inventories)

fun getMaxInventory(inventories: List<List<Int>>): Int = inventories.maxOf(List<Int>::sum)

fun computePartTwo(inventories: List<List<Int>>): Int = sumMaxThreeUsingFold(inventories)

fun sumMaxThreeUsingSort(inventories: List<List<Int>>): Int =
    inventories.map(List<Int>::sum).sorted().takeLast(3).sum()

fun sumMaxThreeUsingFold(inventories: List<List<Int>>): Int =
    inventories.fold(listOf(0, 0, 0)) { podium, inventory ->
        (podium + inventory.sum()).sortedDescending().take(3)
    }.sum()
