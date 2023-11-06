package aoc_2020

import kotlin.test.Test

internal class `20day0` {
    private val part1TestFileName = "day1_test"
    private val part1FileName = "day1"

    private fun part1Calculation(input: List<String>) {
        println(input)
    }

    private fun part2Calculation(input: List<String>) {
        println(input)
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