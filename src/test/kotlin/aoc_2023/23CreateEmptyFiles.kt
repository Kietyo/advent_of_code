package aoc_2023

import java.io.File
import kotlin.test.Test

internal class `23CreateEmptyFiles` {
    @Test
    fun createFiles() {
        val folder = File("src\\test\\resources\\aoc_2023")

        for (i in 3..25) {
            val newFile = File("src\\test\\resources\\aoc_2023\\day${i}.txt")
            newFile.createNewFile()
        }
    }
}