package strategies

import java.io.File

fun interface ReadInputStrategy<Input> {
    fun read(file: File): Input

    companion object {
        val readRaw = ReadInputStrategy(File::readText)
        val readLines = ReadInputStrategy(File::readLines)
    }
}