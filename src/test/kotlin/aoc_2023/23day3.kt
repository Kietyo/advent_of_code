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

    private fun part2Calculation(input: List<String>): Int {
        val grid = input.convertToDataObjectList().toGrid()
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
        // x=3
        // 0, 2
        var sum = 0
        for ((y, line) in input.withIndex()) {
            for ((x, c) in line.withIndex()) {
                if (c == '*') {
                    val xRange = (x-1)..(x+1)
                    val closeNums = numsPerRow.withIndex().flatMap { (y2, nums) ->
                        nums.filter {(x2, num) ->
                            y2 in (y-1)..(y+1) &&
                            (x2..<x2+num.toString().length).intersect(xRange).isNotEmpty()
                        }
                    }
                    require(closeNums.size <= 2)
                    if (closeNums.size == 2) {
                        sum += closeNums.first().value * closeNums.last().value
                    }
                }
            }
        }

        println(sum)
        return sum
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
        assertThat(part2Calculation(input)).isEqualTo(467835)
    }

    @Test
    fun part2() {
        val input = readInput(fileName)
        assertThat(part2Calculation(input)).isEqualTo(79844424)
    }
}