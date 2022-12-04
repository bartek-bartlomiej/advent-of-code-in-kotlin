package solutions.puzzle_2022_02

import ParseInputStrategy
import Puzzle
import Puzzle.Part.Number
import ReadInputStrategy

fun parseRound(line: String): Round {
    val (opponentSymbol, playerSymbol) = line.split(" ").map { it[0] }
    return Round(
        Choice.findByOpponentSymbol(opponentSymbol),
        Choice.findByPlayerSymbol(playerSymbol)
    )
}

data class Round(val opponentChoice: Choice, val playerChoice: Choice) {
    val outcomeForPlayer: Outcome =
        when {
            playerChoice.beatingChoice == opponentChoice -> Outcome.Win
            playerChoice == opponentChoice -> Outcome.Draw
            else -> Outcome.Loss
        }
}

enum class Choice(val score: Int, private val opponentSymbol: Char, private val playerSymbol: Char) {
    Rock(1, 'A', 'X') {
        override val beatingChoice: Choice get() = Scissors
    },
    Paper(2, 'B', 'Y') {
        override val beatingChoice: Choice get() = Rock
    },
    Scissors(3, 'C', 'Z') {
        override val beatingChoice: Choice get() = Paper
    };

    abstract val beatingChoice: Choice

    companion object {
        fun findByOpponentSymbol(symbol: Char) = values().find { it.opponentSymbol == symbol }!!
        fun findByPlayerSymbol(symbol: Char) = values().find { it.playerSymbol == symbol }!!
    }
}

enum class Outcome(val score: Int) {
    Loss(0), Draw(3), Win(6)
}

fun computePartOne(rounds: List<Round>): Int =
    rounds.sumOf { round -> round.playerChoice.score + round.outcomeForPlayer.score }

fun computePartTwo(rounds: List<Round>) = 0

val puzzle = Puzzle(
    number = Puzzle.Number(2022, 2),
    firstPart = Puzzle.Part(
        ReadInputStrategy.readLines,
        ParseInputStrategy.parseLines(::parseRound),
        ::computePartOne
    ),
    secondPart = Puzzle.Part(
        ReadInputStrategy.readLines,
        ParseInputStrategy.parseLines(::parseRound),
        ::computePartTwo
    )
)

fun main() {
    puzzle.test(Number.First, 15)
    puzzle.solve(Number.First)

    puzzle.test(Number.Second, 0)
    puzzle.solve(Number.Second)
}
