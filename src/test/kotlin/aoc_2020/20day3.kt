package aoc_2020

import utils.MutableGrid
import utils.toGrid
import kotlin.test.Test

internal class `20day3` {
    private val part1FileName = "day3"
    private val part1TestFileName = "day3_test"

    private fun List<String>.convertToDataObjectList() = run {
        this.toGrid()
    }

    private fun MutableGrid<Char>.countTrees(rightNum: Int, downNum: Int): Int {
        var i = 1
        var numTrees = 0
        while (true) {
            val newY = i * downNum
            if (newY >= maxRows) {
                break
            }
            val e = getCyclic(i * rightNum, newY)
            if (e == '#') {
                numTrees++
            }

            i++
        }
        return numTrees
    }

    private fun part1Calculation(input: List<String>) {
        val converted = input.convertToDataObjectList()
        println(converted)

        val numTrees = converted.countTrees(3, 1)
        println("numTrees: $numTrees")
    }

    private fun part2Calculation(input: List<String>) {
        val converted = input.convertToDataObjectList()
        val numTrees = listOf(
            converted.countTrees(1, 1),
            converted.countTrees(3, 1),
            converted.countTrees(5, 1),
            converted.countTrees(7, 1),
            converted.countTrees(1, 2),
        )
        println(numTrees)
        println(numTrees.fold(1L) { acc, i ->
            acc * i
        })
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