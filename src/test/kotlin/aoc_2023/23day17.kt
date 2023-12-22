package aoc_2023

import com.kietyo.ktruth.assertThat
import kotlin.test.Test

internal class `23day17` {
    private val fileName = "day17"
    private val testFileName = "${fileName}_test"

    private fun List<String>.convertToDataObjectList() = run {
        this
    }

    private fun part1Calculation(input: List<String>): Int {
        val converted = input.convertToDataObjectList()
        println(converted)

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