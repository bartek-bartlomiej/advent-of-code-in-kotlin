package solutions.puzzle_2022_07

import Puzzle
import ReadInputStrategy

fun main() {
    puzzle.testFirst(95437)
    puzzle.solveFirst()

    puzzle.testSecond(24933642)
    puzzle.solveSecond()
}

private val puzzle = Puzzle(
    number = Puzzle.Number(2022, 7),
    firstPart = Puzzle.Part(
        ReadInputStrategy.readRaw,
        ::parseTerminalOutput,
        ::findTotalSizeOfDirectoriesWithSizeAtMost100000
    ),
    secondPart = Puzzle.Part(
        ReadInputStrategy.readRaw,
        ::parseTerminalOutput,
        ::findSizeOfDirectoryToDelete
    )
)

private fun findTotalSizeOfDirectoriesWithSizeAtMost100000(tree: Pair<Directory, List<Directory>>) =
    tree.let { (_, directories) -> directories.filter { it.totalSize < 100000 }.sumOf { it.totalSize } }

private fun findSizeOfDirectoryToDelete(tree: Pair<Directory, List<Directory>>) =
    tree.let { (root, directories) ->
        val unusedSpace = 70000000 - root.totalSize
        val requiredFreeSpace = 30000000 - unusedSpace

        directories.map(Directory::totalSize).filter { it > requiredFreeSpace }.min()
    }

private fun parseTerminalOutput(output: String): Pair<Directory, List<Directory>> {
    val commands = output.split("$ ").drop(1).map {
        val commandAndOutput = it.trim().split("\n")
        val command = commandAndOutput.first().split(" ")
        val commandOutput = commandAndOutput.drop(1)

        when (command.first()) {
            "cd" -> ChangeDirectoryCommand(name = command.last())
            else -> {
                val contents = commandOutput.map { it.split(" ") }.map { (sizeOrType, name) ->
                    if (sizeOrType == "dir") Directory(name)
                    else File(name, sizeOrType.toInt())
                }
                ListCommand(contents)
            }
        }
    }

    val root = Directory("/")
    val directories = mutableListOf(root)
    var currentWorkingDirectory = root

    commands.drop(1).forEach {
        when (it) {
            is ChangeDirectoryCommand -> {
                currentWorkingDirectory = when (val where = it.name) {
                    ".." -> currentWorkingDirectory.parent
                    else -> currentWorkingDirectory.directories[where]!!
                }
            }
            is ListCommand -> {
                it.entries.forEach { entry ->
                    when (entry) {
                        is File -> currentWorkingDirectory.addFile(entry)
                        is Directory -> {
                            directories += entry
                            currentWorkingDirectory.addDirectory(entry)
                        }
                    }
                }
            }
        }
    }

    return Pair(root, directories)
}

private sealed interface Command
private data class ChangeDirectoryCommand(val name: String) : Command
private data class ListCommand(val entries: List<Entry>) : Command

private data class File(
    override val name: String,
    val size: Int
) : Entry

private class Directory(
    override val name: String
) : Entry {
    private val mutableFiles: MutableSet<File> = mutableSetOf()
    val files: Set<File> = mutableFiles

    private val mutableDirectories: MutableMap<String, Directory> = mutableMapOf()
    val directories: Map<String, Directory> = mutableDirectories

    var parent: Directory = this
        private set

    fun addFile(file: File) {
        mutableFiles += file
    }

    fun addDirectory(directory: Directory) {
        require(this != directory)
        require(!mutableDirectories.contains(directory.name))

        mutableDirectories[directory.name] = directory
        directory.parent = this
    }

    val totalSize: Int by lazy {
        directories.values.sumOf(Directory::totalSize) + files.sumOf(File::size)
    }
}

private sealed interface Entry {
    val name: String
}
