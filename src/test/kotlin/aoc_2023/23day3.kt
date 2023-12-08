package aoc_2023

import com.kietyo.ktruth.assertThat
import utils.Grid
import utils.splitByPredicateIndexed
import utils.toGrid
import kotlin.test.Test

internal class `23day3` {
    private val fileName = "day3"
    private val testFileName = "${fileName}_test"

    private fun List<String>.convertToDataObjectList() = run {
        this
    }

    private fun isEmptyAround(grid: Grid<Char>, numString: String, y: Int, x: Int): Boolean {
        // check y-1
        for (i in (x - 1)..(x + numString.length)) {
            // check y-1
            if (grid.getOrDefault(i, y - 1) { '.' } != '.' && !grid.getOrDefault(i, y - 1) { '.' }
                    .isDigit()) {
                return false
            }
            // check y+1
            if (grid.getOrDefault(i, y + 1) { '.' } != '.' && !grid.getOrDefault(i, y + 1) { '.' }
                    .isDigit()) {
                return false
            }
        }
        if (grid.getOrDefault(x - 1, y) { '.' } != '.') {
            return false
        }
        if (grid.getOrDefault(x + numString.length, y) { '.' } != '.') {
            return false
        }
        return true
    }

    private fun part1Calculation(input: List<String>): Int {
        val grid = input.convertToDataObjectList().toGrid()
        println(grid)
        val numsPerRow = input.map {
            it.splitByPredicateIndexed { !it.isDigit() }.mapNotNull {
                val num = it.value.toIntOrNull()
                if (num == null) {
                    null
                } else {
                    IndexedValue(it.index, num)
                }
            }
        }
        var sum = 0
        for ((i, nums) in numsPerRow.withIndex()) {
            for (num in nums) {
//                val numIndex = input[i].indexOf(num.toString())
                println("$i, $num")
                if (!isEmptyAround(grid, num.value.toString(), i, num.index)) {
                    println("adding $num")
                    sum += num.value
                }
            }
        }
        println("sum: $sum")
        return sum
    }

    private fun part2Calculation(input: List<String>) {
        val converted = input.convertToDataObjectList()
        println(converted)
    }

    @Test
    fun part1Test() {
        val input = readInput(testFileName)
        assertThat(part1Calculation(input)).isEqualTo(4361)
    }

    @Test
    fun part1Test2() {
        val input = readInput("day3_test2")
        assertThat(part1Calculation(input)).isEqualTo(134)
    }

    @Test
    fun part1() {
        val input = readInput(fileName)
        // 535294, too high, 535235
        assertThat(part1Calculation(input)).isEqualTo(535235)
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