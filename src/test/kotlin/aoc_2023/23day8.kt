package aoc_2023

import com.kietyo.ktruth.assertThat
import kotlin.test.Test

internal class `23day8` {
    private val fileName = "day8"
    private val testFileName = "${fileName}_test"

    private fun List<String>.convertToDataObjectList() = run {
        this
    }

    private fun part1Calculation(input: List<String>): Int {
        val converted = input.convertToDataObjectList()
        println(converted)

        val instructions = converted.first()

        val regex = Regex("(\\w+) = \\((\\w+), (\\w+)\\)")

        val map = converted.drop(2).map {
            val match = regex.matchEntire(it)
            val (source, left, right) = match!!.destructured
            source to (left to right)
        }.toMap()
        println(map)

        var idx = 0
        var currentNode = "AAA"
        while (true) {
            if (currentNode == "ZZZ") {
                break
            }
            val node = map[currentNode]!!
            val currInstruction = instructions.getCyclic(idx)
            when (currInstruction) {
                'L' -> currentNode = node.first
                'R' -> currentNode = node.second
                else -> TODO()
            }
            idx++
        }

        println(idx)
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
    fun part1Test2() {
        val input = readInput("day8_test2")
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

private fun String.getCyclic(idx: Int): Char {
    return get(idx % length)
}
