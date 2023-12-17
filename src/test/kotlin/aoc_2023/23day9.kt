package aoc_2023

import com.kietyo.ktruth.assertThat
import kotlin.test.Test

internal class `23day9` {
    private val fileName = "day9"
    private val testFileName = "${fileName}_test"

    private fun List<String>.convertToDataObjectList() = run {
        this
    }

    data class Calculator(
        val nums: List<Int>,
        val diffs: List<List<Int>> = buildList {
            var currNums = nums
            println(nums)
            while (true) {
                currNums = currNums.windowed(2).map { it[1] - it[0] }
                println(currNums)
                if (currNums.all { it == 0 }) {
                    break
                }
                add(currNums)
            }
        }
    ) {
        fun calculateLastDigit(): Int {
            val sumDiff = diffs.foldRight(0) { ls, acc ->
                ls.last() + acc
            }
            return nums.last() + sumDiff
        }
    }

    private fun part1Calculation(input: List<String>): Int {
        val converted = input.convertToDataObjectList().map {
            Calculator(it.split(" ").map { it.toInt() })
        }
        println(converted)

        return converted.map { it.calculateLastDigit() }.sum()
    }

    private fun part2Calculation(input: List<String>): Int {
        val converted = input.convertToDataObjectList()
        println(converted)

        return 0
    }

    @Test
    fun part1Test() {
        val input = readInput(testFileName)
        assertThat(part1Calculation(input)).isEqualTo(114)
    }

    @Test
    fun part1() {
        val input = readInput(fileName)
        assertThat(part1Calculation(input)).isEqualTo(1479011877)
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