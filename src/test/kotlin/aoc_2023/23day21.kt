package aoc_2023

import com.kietyo.ktruth.assertThat
import utils.IntPoint
import utils.toGrid
import java.util.LinkedList
import kotlin.test.Test

internal class `23day21` {
    private val fileName = "day21"
    private val testFileName = "${fileName}_test"

    private fun List<String>.convertToDataObjectList() = run {
        this
    }

    private fun part1Calculation(input: List<String>, iterations: Int): Int {
        val converted = input.convertToDataObjectList()
        println(converted)

        val grid = converted.toGrid()

        println(grid)

        var currentPoints = listOf<IntPoint>(grid.find('S'))

        repeat(iterations) {
            val nextPoints = mutableListOf<IntPoint>()
            for (point in currentPoints) {
                val adjacents = grid.getAdjacents(point.x, point.y, includeDiagonals = false)
                for (adjacent in adjacents) {
                    val asPoint = adjacent.toIntPoint()
                    if ((adjacent.data == '.' || adjacent.data == 'S') &&
                        !nextPoints.contains(asPoint)) {
                        nextPoints.add(asPoint)
                    }
                }
            }
            currentPoints = nextPoints
        }

        println(currentPoints.size)

        for (point in currentPoints) {
            if (grid[point.x, point.y] == 'S') {
                grid[point.x, point.y] = 'X'
            } else {
                grid[point.x, point.y] = '0'
            }
        }

        println(grid)

        return currentPoints.size
    }

    private fun part2Calculation(input: List<String>): Int {
        val converted = input.convertToDataObjectList()
        println(converted)

        return 0
    }

    @Test
    fun part1Test() {
        val input = readInput(testFileName)
        assertThat(part1Calculation(input, 6)).isEqualTo(16)
    }

    @Test
    fun part1() {
        val input = readInput(fileName)
        assertThat(part1Calculation(input, 64)).isEqualTo(3795)
    }

    @Test
    fun part1b() {
        val input = readInput(fileName)
        assertThat(part1Calculation(input, 65)).isEqualTo(3795)
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