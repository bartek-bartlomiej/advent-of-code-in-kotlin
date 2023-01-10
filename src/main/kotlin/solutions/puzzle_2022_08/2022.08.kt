package solutions.puzzle_2022_08

import Puzzle
import ReadInputStrategy

fun main() {
    puzzle.testFirst(21)
    puzzle.solveFirst()

    puzzle.testSecond(8)
    puzzle.solveSecond()
}

private val puzzle = Puzzle(
    number = Puzzle.Number(2022, 8),
    firstPart = Puzzle.Part(
        ReadInputStrategy.readLines,
        ::parseTreesVisibility,
        ::countVisibleTrees
    ),
    secondPart = Puzzle.Part(
        ReadInputStrategy.readLines,
        ::parseTreesVisibility,
        ::computeMaximumScore
    )
)

private fun parseTreesVisibility(image: List<String>): Board<Int> {
    val height = image.size
    val width = image.first().length

    return List(height) { j -> List(width) { i -> image[j][i] - '0' } }
}

private fun countVisibleTrees(trees: Board<Int>): Int {
    val height = trees.size
    val width = trees.first().size

    val treesVisibility = Array(height) { Array(width) { false } }

    repeat(height) { rowIndex ->
        treesVisibility[rowIndex][0] = true
        treesVisibility[rowIndex][width - 1] = true
    }

    repeat(width) { columnIndex ->
        treesVisibility[0][columnIndex] = true
        treesVisibility[height - 1][columnIndex] = true
    }

    fun updateVisibilityWithTrackingHighest(
        rowIndex: Int,
        columnIndex: Int,
        height: Int,
        maximumHeight: Int
    ) =
        if (height > maximumHeight) {
            treesVisibility[rowIndex][columnIndex] = true
            height
        } else {
            maximumHeight
        }

    (0 until height).forEach { rowIndex ->
        val row = trees[rowIndex]
        row.reduceIndexed { columnIndex, maximumHeight, height ->
            updateVisibilityWithTrackingHighest(rowIndex, columnIndex, height, maximumHeight)
        }
        row.reduceRightIndexed { columnIndex, height, maximumHeight ->
            updateVisibilityWithTrackingHighest(rowIndex, columnIndex, height, maximumHeight)
        }
    }

    (0 until width).forEach { columnIndex ->
        val column = trees.map { it[columnIndex] }
        column.reduceIndexed { rowIndex, maximumHeight, height ->
            updateVisibilityWithTrackingHighest(rowIndex, columnIndex, height, maximumHeight)
        }
        column.reduceRightIndexed { rowIndex, height, maximumHeight ->
            updateVisibilityWithTrackingHighest(rowIndex, columnIndex, height, maximumHeight)
        }
    }

    return treesVisibility.flatten().count { it }
}

private fun computeMaximumScore(trees: Board<Int>): Int {
    val height = trees.size
    val width = trees.first().size

    var maximumScore = 0
    repeat(height) { rowIndex ->
        repeat(width) { heightIndex ->
            val treeHeight = trees[rowIndex][heightIndex]

            val leftCount = ((rowIndex - 1) downTo 0).withIndex().run {
                firstOrNull() { (_, rowIndex) -> trees[rowIndex][heightIndex] >= treeHeight }
                    ?.let { it.index + 1 }
                    ?: count()
            }

            val rightCount = ((rowIndex + 1) until width).withIndex().run {
                firstOrNull() { (_, rowIndex) -> trees[rowIndex][heightIndex] >= treeHeight }
                    ?.let { it.index + 1 }
                    ?: count()
            }

            val topCount = ((heightIndex - 1) downTo 0).withIndex().run {
                firstOrNull() { (_, heightIndex) -> trees[rowIndex][heightIndex] >= treeHeight }
                    ?.let { it.index + 1 }
                    ?: count()
            }

            val bottomCount = ((heightIndex + 1) until height).withIndex().run {
                firstOrNull() { (_, heightIndex) -> trees[rowIndex][heightIndex] >= treeHeight }
                    ?.let { it.index + 1 }
                    ?: count()
            }

            val score = leftCount * topCount * rightCount * bottomCount
            if (score > maximumScore) maximumScore = score
        }
    }

    return maximumScore
}

private typealias Board<T> = List<List<T>>