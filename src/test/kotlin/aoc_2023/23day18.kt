package aoc_2023

import com.kietyo.ktruth.assertThat
import utils.Direction
import utils.IntPoint
import utils.MutableListGrid
import utils.MutableRangeGrid
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
        val distance: Int,
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

    private fun internalCalculate(instructions: List<Instruction>, pointInInner: IntPoint): Int {
        var numWalls = 0
        val grid = MutableRangeGrid()
        var current = IntPoint(0, 0)
        for (instruction in instructions) {
            val dir = instruction.dir
            val distance = instruction.distance
            val vector = dir.movementOffset * (distance - 1)
            val start = current + dir
            val end = start + vector
            val r = Range(start, end)
            grid.add(r)
            numWalls += r.range
            current = end
        }

        println(grid)

        val horizontalRanges = mutableListOf<Range>()
        for (y in grid.minY..grid.maxY) {
            var currStart: IntPoint? = null
            for (x in grid.minX..grid.maxX) {
                val currValue = grid.getOrDefault(x, y) { false }
                if (currValue) {
                    if (currStart != null) {
                        horizontalRanges.add(Range(currStart, IntPoint(x - 1, y)))
                        currStart = null
                    }
                } else {
                    if (currStart == null) {
                        currStart = IntPoint(x, y)
                    }
                }
            }
            if (currStart != null) {
                horizontalRanges.add(Range(currStart, IntPoint(grid.maxX, y)))
            }
        }
        println("horizontalRanges: $horizontalRanges")

        val verticalRanges = mutableListOf<Range>()
        for (x in grid.minX..grid.maxX) {
            var currStart: IntPoint? = null
            for (y in grid.minY..grid.maxY) {
                val currValue = grid.getOrDefault(x, y) { false }
                if (currValue) {
                    if (currStart != null) {
                        verticalRanges.add(Range(currStart, IntPoint(x, y-1)))
                        currStart = null
                    }
                } else {
                    if (currStart == null) {
                        currStart = IntPoint(x, y)
                    }
                }
            }
            if (currStart != null) {
                verticalRanges.add(Range(currStart, IntPoint(x, grid.maxY)))
            }
        }
        println(verticalRanges)

        val matchingRange = horizontalRanges.first { pointInInner in it }
        println(matchingRange)

        val horizontalInnerRanges = mutableSetOf(matchingRange)
        val verticalInnerRanges = mutableSetOf<Range>()
        while (true) {
            val prevHorizontalSize = horizontalInnerRanges.size
            val prevVerticalSize = verticalInnerRanges.size
            for (range in verticalRanges) {
                if (horizontalInnerRanges.any { it.intersectsWith(range) }) {
                    verticalInnerRanges.add(range)
                }
            }

            for (range in horizontalRanges) {
                if (verticalInnerRanges.any { it.intersectsWith(range) }) {
                    horizontalInnerRanges.add(range)
                }
            }

            if (prevHorizontalSize == horizontalInnerRanges.size &&
                prevVerticalSize == verticalInnerRanges.size) {
                break
            }
        }
        println(horizontalInnerRanges)
        println(verticalInnerRanges)
        println(numWalls)

        val sumInner = horizontalInnerRanges.sumOf { it.range }
        println(sumInner)

        println(numWalls + sumInner)
        return numWalls + sumInner
    }

    private fun part1Calculation(input: List<String>, pointInInner: IntPoint): Int {
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
                }, numString.toInt()
            )
        }
        println(instructions)

        return internalCalculate(instructions, pointInInner)
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun part2Calculation(input: List<String>, pointInInner: IntPoint): Int {
        val converted = input.convertToDataObjectList()

        println(converted)
        val regex = Regex("(\\w) (\\d+) \\((.+)\\)")
        val instructions = converted.map {
            val match = regex.matchEntire(it)
            val (dir, numString, hex) = match!!.destructured
            Instruction(
                run {
                when (hex.last().digitToInt()) {
                    0 -> Direction.RIGHT
                    1 -> Direction.DOWN
                    2 -> Direction.LEFT
                    3 -> Direction.UP
                    else -> TODO()
                }
            }, run {
                    hex.drop(1).dropLast(1).hexToInt()
                },
            )
        }
        println(instructions.joinToString("\n"))

        return internalCalculate(instructions, pointInInner)
    }

    @Test
    fun part1Test() {
        val input = readInput(testFileName)
        assertThat(part1Calculation(input, 1 toip 1)).isEqualTo(62)
    }

    @Test
    fun part1() {
        val input = readInput(fileName)
        assertThat(part1Calculation(input, 65 toip -232)).isEqualTo(61865)
    }

    @Test
    fun part2Test() {
        val input = readInput(testFileName)
        assertThat(part2Calculation(input, 0 toip 0)).isEqualTo(0)
    }

    @Test
    fun part2() {
        val input = readInput(fileName)
        assertThat(part2Calculation(input, 0 toip 0)).isEqualTo(0)
    }
}