package solutions

import PartProperties
import Puzzle
import strategies.ParseInputStrategy
import strategies.ReadInputStrategy

fun computePartOne(inventories: List<List<Int>>): Int = inventories.maxOf(List<Int>::sum)

fun computePartTwo(inventories: List<List<Int>>): Int = inventories.map(List<Int>::sum).sorted().takeLast(3).sum()

fun main() {
    Puzzle(
        number = Puzzle.Number(2022, 1),
        firstPart = PartProperties(
            ReadInputStrategy.readRaw,
            ParseInputStrategy.parseGroups(ParseInputStrategy.parseToInts::parse),
            ::computePartOne,
            24000
        ),
        secondPart = PartProperties(
            ReadInputStrategy.readRaw,
            ParseInputStrategy.parseGroups(ParseInputStrategy.parseToInts::parse),
            ::computePartTwo,
            45000
        )
    )
}
