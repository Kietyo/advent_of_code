package aoc_2023

import com.kietyo.ktruth.assertThat
import kotlin.test.Test

internal class `23day18` {
    private val fileName = "day18"
    private val testFileName = "${fileName}_test"

    private fun List<String>.convertToDataObjectList() = run {
        this
    }

    data class Instruction(
        val dir: Char,
        val num: Int,
        val hex: String
    )

    private fun part1Calculation(input: List<String>): Int {
        val converted = input.convertToDataObjectList()

        println(converted)
        val regex = Regex("(\\w) (\\d+) \\((.+)\\)")
        val instructions = converted.map {
            val match = regex.matchEntire(it)
            val (dir, numString, hex) = match!!.destructured
            Instruction(dir.first(), numString.toInt(), hex)
        }
        println(instructions)

        return 0
    }

    private fun part2Calculation(input: List<String>): Int {
        val converted = input.convertToDataObjectList()
        println(converted)

        return 0
    }

    @Test
    fun part1Test() {
        val input = readInput(testFileName)
        assertThat(part1Calculation(input)).isEqualTo(0)
    }

    @Test
    fun part1() {
        val input = readInput(fileName)
        assertThat(part1Calculation(input)).isEqualTo(0)
    }

    @Test
    fun part2Test() {
        val input = readInput(testFileName)
        assertThat(part2Calculation(input)).isEqualTo(0)
    }

    @Test
    fun part2() {
        val input = readInput(fileName)
        assertThat(part2Calculation(input)).isEqualTo(0)
    }
}