package solutions.puzzle_2022_02

import ParseInputStrategy
import Puzzle
import ReadInputStrategy

fun main() {
    puzzle.testFirst(15)
    puzzle.solveFirst()

    puzzle.testSecond(12)
    puzzle.solveSecond()
}

private val puzzle = Puzzle(
    number = Puzzle.Number(2022, 2),
    firstPart = Puzzle.Part(
        ReadInputStrategy.readLines,
        ParseInputStrategy.parseLines(::parseStrategyGuide),
        ::computeTotalScoreUsingStrategy
    ),
    secondPart = Puzzle.Part(
        ReadInputStrategy.readLines,
        ParseInputStrategy.parseLines(::parseTopSecretStrategyGuide),
        ::computeTotalScoreUsingTopSecretStrategy
    )
)

private fun parseStrategyGuide(line: String): Pair<Choice, Choice> {
    val (opponentSymbol, playerSymbol) = parseLine(line)
    return Pair(
        opponentChoicesMapping[opponentSymbol]!!,
        playerChoicesMapping[playerSymbol]!!
    )
}

private fun parseTopSecretStrategyGuide(line: String): Pair<Choice, Outcome> {
    val (opponentSymbol, outcomeSymbol) = parseLine(line)
    return Pair(
        opponentChoicesMapping[opponentSymbol]!!,
        outcomesMapping[outcomeSymbol]!!
    )
}

private fun parseLine(line: String) = line.split(" ").map { it[0] }

private fun computeTotalScoreUsingStrategy(roundInfos: List<Pair<Choice, Choice>>): Int =
    roundInfos.sumOf { (opponentChoice, yourChoice) ->
        yourChoice.score + determineOutcomeForYou(opponentChoice, yourChoice).score
    }

private fun determineOutcomeForYou(opponentChoice: Choice, playerChoice: Choice) =
    when {
        playerChoice.defeating == opponentChoice -> Outcome.Win
        playerChoice == opponentChoice -> Outcome.Draw
        else -> Outcome.Loss
    }

private fun computeTotalScoreUsingTopSecretStrategy(rounds: List<Pair<Choice, Outcome>>): Int =
    rounds.sumOf { (opponentChoice, outcomeForPlayer) ->
        determinePlayerChoice(opponentChoice, outcomeForPlayer).score + outcomeForPlayer.score
    }

private fun determinePlayerChoice(opponentChoice: Choice, outcomeForPlayer: Outcome) =
    when (outcomeForPlayer) {
        Outcome.Loss -> opponentChoice.defeating
        Outcome.Draw -> opponentChoice
        Outcome.Win -> opponentChoice.defeatedBy
    }

private val opponentChoicesMapping = mapOf(
    'A' to Choice.Rock,
    'B' to Choice.Paper,
    'C' to Choice.Scissors
)

private val playerChoicesMapping = mapOf(
    'X' to Choice.Rock,
    'Y' to Choice.Paper,
    'Z' to Choice.Scissors
)

private val outcomesMapping = mapOf(
    'X' to Outcome.Loss,
    'Y' to Outcome.Draw,
    'Z' to Outcome.Win
)

private enum class Choice(val score: Int) {
    Rock(1) {
        override val defeating: Choice get() = Scissors
        override val defeatedBy: Choice get() = Paper
    },
    Paper(2) {
        override val defeating: Choice get() = Rock
        override val defeatedBy: Choice get() = Scissors
    },
    Scissors(3) {
        override val defeating: Choice get() = Paper
        override val defeatedBy: Choice get() = Rock
    };

    abstract val defeating: Choice
    abstract val defeatedBy: Choice
}

private enum class Outcome(val score: Int) {
    Loss(0), Draw(3), Win(6)
}
