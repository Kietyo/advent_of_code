package aoc_2023

import com.kietyo.ktruth.assertThat
import utils.Direction
import utils.IntPoint
import utils.MutableListGrid
import utils.Range
import utils.forEachWithDefault
import utils.toip
import kotlin.test.Test

internal class `23day18` {
    private val fileName = "day18"
    private val testFileName = "${fileName}_test"

    private fun List<String>.convertToDataObjectList() = run {
        this
    }

    data class Instruction(
        val dir: Direction,
        val num: Int,
        val hex: String
    )

    fun version1(grid: MutableListGrid<Char>) {
        val openSpacePoints = mutableListOf<IntPoint>()
        println(grid)
        println("width: ${grid.width}, height: ${grid.height}")
        while (true) {
            var wasChanged = false
            grid.forEachWithDefault('.') { x, y, value, isFirstElementInNewRow ->
                if (x toip y in openSpacePoints) return@forEachWithDefault
                if (value == '.') {
                    if (grid.isNearEnclosingBoundary(x, y) ||
                        grid.getAdjacents(x, y, includeDiagonals = false).any {
                            it.x toip it.y in openSpacePoints
                        }
                    ) {
                        wasChanged = true
                        //                        grid.put(x, y, '0')
                        openSpacePoints.add(x toip y)
                    }
                }
            }
            if (!wasChanged) {
                break
            }
        }
        println(grid)
        val res = grid.width * grid.height - openSpacePoints.size
        println(res)
    }

    private fun part1Calculation(input: List<String>): Int {
        val converted = input.convertToDataObjectList()

        println(converted)
        val regex = Regex("(\\w) (\\d+) \\((.+)\\)")
        val instructions = converted.map {
            val match = regex.matchEntire(it)
            val (dir, numString, hex) = match!!.destructured
            Instruction(
                when (dir.first()) {
                    'U' -> Direction.UP
                    'D' -> Direction.DOWN
                    'L' -> Direction.LEFT
                    'R' -> Direction.RIGHT
                    else -> TODO()
                }, numString.toInt(), hex
            )
        }
        println(instructions)

        val grid = MutableListGrid<Char>('.')
        var current = IntPoint(0, 0)
        for (instruction in instructions) {
            for (i in 0 until instruction.num) {
                current += instruction.dir
                grid.put(current.x, current.y, '#')
            }
        }

        println(grid)

        val horizontalRanges = mutableListOf<Range>()
        for (y in grid.minY..grid.maxY) {
            var currStart: IntPoint? = null
            for (x in grid.minX..grid.maxX) {
                val currValue = grid.getOrDefault(x, y){'.'}
                if (currValue == '.') {
                    if (currStart == null) {
                        currStart = IntPoint(x, y)
                    }
                } else if (currValue == '#') {
                    if (currStart != null) {
                        horizontalRanges.add(Range(currStart, IntPoint(x-1,y)))
                        currStart = null
                    }
                }
            }
            if (currStart != null) {
                horizontalRanges.add(Range(currStart, IntPoint(grid.maxX,y)))
            }
        }

        println(horizontalRanges)

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