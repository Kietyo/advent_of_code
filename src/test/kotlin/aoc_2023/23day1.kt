package aoc_2023

import com.kietyo.ktruth.assertThat
import kotlin.test.Test

internal class `23day1` {
    private val fileName = "day1"
    private val testFileName = "${fileName}_test"

    private fun List<String>.convertToDataObjectList() = run {
        this
    }

    private fun part1Calculation(input: List<String>): Int {
        val converted = input.convertToDataObjectList()
        println(converted)

        return converted.sumOf {
            it.first { it.isDigit() }.digitToInt() * 10 +
                    it.last { it.isDigit() }.digitToInt()
        }
    }

    private fun part2Calculation(input: List<String>): Int {
        val converted = input.convertToDataObjectList()
        val numNames = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
        println(converted)

        return converted.sumOf { str ->
            var firstNum: Int? = null
            var secondNum: Int? = null
            for (i in str.indices) {
                if (str[i].isDigit()) {
                    if (firstNum == null) {
                        firstNum = str[i].digitToInt()
                    } else {
                        secondNum = str[i].digitToInt()
                    }
                } else {
                    val remainingLength = str.length - i
                    val indexOfMatchingNumName = numNames.indexOfFirst {
                        if (it.length <= remainingLength) {
                            str.substring(i, i + it.length) == it
                        } else {
                            false
                        }
                    }
                    if (indexOfMatchingNumName != -1) {
                        if (firstNum == null) {
                            firstNum = indexOfMatchingNumName + 1
                        } else {
                            secondNum = indexOfMatchingNumName + 1
                        }
                    }
                }
            }

            //            println(firstNum)
            //            println(secondNum)

            if (secondNum == null) {
                firstNum!! * 10 + firstNum
            } else {
                firstNum!! * 10 + secondNum
            }
        }
    }

    @Test
    fun part1Test() {
        val input = readInput(testFileName)
        assertThat(part1Calculation(input)).isEqualTo(142)
    }

    @Test
    fun part1() {
        val input = readInput(fileName)
        assertThat(part1Calculation(input)).isEqualTo(55971)
    }

    @Test
    fun part2Test() {
        val input = readInput("day1_test2")
        part2Calculation(input)
        assertThat(part2Calculation(input)).isEqualTo(281)
    }

    @Test
    fun part2() {
        val input = readInput(fileName)
        assertThat(part2Calculation(input)).isEqualTo(54719)
    }
}