package aoc_2020

import utils.println
import kotlin.test.Test

internal class `20day5` {
    private val fileName = "day5"
    private val testFileName = "${fileName}_test"

    private fun List<String>.convertToDataObjectList() = run {
        this
    }

    private fun calculateNum(initLow: Int, initHigh: Int, chars: String): Int {
        var low = initLow
        var high = initHigh
        for (char in chars) {
            when (char) {
                'F','L' -> high = (high + low) / 2
                'B','R'-> low = (high + low) / 2 + 1
                else -> TODO("Char $char, not implemented.")
            }
            println(low, high)
        }
        require(low == high)
        return low
    }

    private fun part1Calculation(input: List<String>) {
        val converted = input.convertToDataObjectList()
        println(converted.maxOf {
            val rowChars = it.substring(0, 7)
            val columnChars = it.substring(7, it.length)
            println(rowChars)
            println(columnChars)
            val row = calculateNum(0, 127, rowChars)
            val column = calculateNum(0, 7, columnChars)
            row * 8 + column
        })
    }

    private fun part2Calculation(input: List<String>) {
        val converted = input.convertToDataObjectList()
        println(converted.map {
            val rowChars = it.substring(0, 7)
            val columnChars = it.substring(7, it.length)
            println(rowChars)
            println(columnChars)
            val row = calculateNum(0, 127, rowChars)
            val column = calculateNum(0, 7, columnChars)
            row * 8 + column
        }.sorted().windowed(2) {
            if (it[0] + 1 != it[1]) {
                println(it)
            }
        })
    }

    @Test
    fun part1Test() {
        val input = readInput(testFileName)
        part1Calculation(input)
    }

    @Test
    fun part1() {
        val input = readInput(fileName)
        part1Calculation(input)
    }

    @Test
    fun part2Test() {
        val input = readInput(testFileName)
        part2Calculation(input)
    }

    @Test
    fun part2() {
        val input = readInput(fileName)
        part2Calculation(input)
    }
}