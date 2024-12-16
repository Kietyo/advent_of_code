package aoc_2024

import com.kietyo.ktruth.assertThat
import java.util.regex.MatchResult
import java.util.regex.Pattern
import kotlin.test.Test


internal class `24day3` {
    private val fileName = "day3"
    private val testFileName = "${fileName}_test"

    private fun List<String>.convertToDataObjectList() = run {
        this
    }

    private fun part1Calculation(input: List<String>): Long {
        val converted = input.convertToDataObjectList()
        println(converted)

        val line = converted.first().lowercase()

        val regex = "mul\\((\\d{1,3}),(\\d{1,3})\\)".toRegex()
        val regex2 = """mul\((\d+),(\d+)\)|do\(\)|don't\(\)"""

        val matches = regex.findAll(line)

        println(matches)

        val sum = matches.sumOf {
            val (_, left, right) = it.groupValues
            println("left: $left, right: $right")
            left.toLong() * right.toLong()
        }

        return converted.sumOf {
            regex.findAll(it).sumOf {
                val (_, left, right) = it.groupValues
                println("left: $left, right: $right")
                left.toLong() * right.toLong()
            }
        }
    }

    private fun part2Calculation(input: List<String>): Long {
        val converted = input.convertToDataObjectList()
        println(converted)

        val line = converted.first().lowercase()

        val regex = """mul\((\d+),(\d+)\)|do\(\)|don't\(\)""".toRegex()

        val matches = regex.findAll(line)

        println(matches)

        var enabled = true
        val sum = converted.sumOf { line ->
            regex.findAll(line).sumOf { match ->
                if (match.value.contains("'")) {
                    // don't()
                    enabled = false
                    0
                } else if (match.value == "do()") {
                    enabled = true
                    0
                } else if (enabled) {
                    val (_, left, right) = match.groupValues
                    println("left: $left, right: $right")
                    left.toLong() * right.toLong()
                } else {
                    0
                }
            }
        }

        return sum

//        return converted.sumOf {
//            regex.findAll(it).sumOf {
//                val (_, left, right) = it.groupValues
//                println("left: $left, right: $right")
//                left.toLong() * right.toLong()
//            }
//        }
    }

    @Test
    fun part1Test() {
        val input = readInput(testFileName)
        assertThat(part1Calculation(input)).isEqualTo(161)
    }

    @Test
    fun part1() {
        val input = readInput(fileName)
        // 28716464, wrong, too low
        assertThat(part1Calculation(input)).isEqualTo(161289189)
    }

    @Test
    fun part2Test() {
        val input = readInput("day3_test2")
        assertThat(part2Calculation(input)).isEqualTo(48)
    }

    @Test
    fun part2() {
        val input = readInput(fileName)
        assertThat(part2Calculation(input)).isEqualTo(83595109)
    }
}