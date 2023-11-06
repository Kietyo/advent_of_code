package aoc_2020

import kotlin.test.Test

internal class `20day2` {
    data class Line(
        val low: Int,
        val high: Int,
        val char: Char,
        val password: String
    )
    private val part1TestFileName = "day2_test"
    private val part1FileName = "day2"

    private fun List<String>.convertToDataObjectList() = run {
        val regex = Regex("(\\d+)-(\\d+) (\\w): (\\w+)")
        map { ele ->
            val matchResult = regex.matchEntire(ele)!!
            val (_, low, high, char, pass) = matchResult.groups.map { it!!.value }
            Line(low.toInt(), high.toInt(), char[0], pass)
        }
    }

    private fun part1Calculation(input: List<String>) {
        println(input)
        val lines = input.convertToDataObjectList()
        println(lines)
        val countLines = lines.count {line ->
            val countChar = line.password.count { it == line.char }
            countChar in line.low..line.high
        }
        println(countLines)
    }

    private fun part2Calculation(input: List<String>) {
        println(input)
        val lines = input.convertToDataObjectList()
        val countLines = lines.count {line ->
            val matchFirst = line.password[line.low-1] == line.char
            val matchSecond = line.password[line.high-1] == line.char
            matchFirst.xor(matchSecond)
        }
        println(countLines)
    }
    
    @Test
    fun part1Test() {
        val input = readInput(part1TestFileName)
        part1Calculation(input)
    }

    @Test
    fun part1() {
        val input = readInput(part1FileName)
        part1Calculation(input)
    }

    @Test
    fun part2Test() {
        val input = readInput(part1TestFileName)
        part2Calculation(input)
    }

    @Test
    fun part2() {
        val input = readInput(part1FileName)
        part2Calculation(input)
    }
}