package aoc_2023

import com.kietyo.ktruth.assertThat
import utils.Direction
import utils.IntPoint
import utils.MutableListGrid
import utils.MutableRangeGrid
import utils.Range
import utils.forEachWithDefault
import utils.toip
import kotlin.math.abs
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

    private fun internalCalculate(instructions: List<Instruction>, pointInInner: IntPoint): Long {
        var numWalls = 0L
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
        println("width: ${grid.width}")
        println("height: ${grid.height}")

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

    private fun internalCalculateV2(instructions: List<Instruction>): Long {
        var numWalls = 0L
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
        println("width: ${grid.width}")
        println("height: ${grid.height}")

        // 32 -> 58
        // 9          6       11
        // ######### ...... ###########
        //             8         15               1
        // # ....... ######## .... .... .... ... #

        //    9            1        15           13
        // #.......#......#.... .... .... ... #### #### #### #
        var sum = 0L
        for (y in grid.minY..grid.maxY) {
            val currLine = grid.getLineString(y)
            println(currLine)
            val horizontalRanges = grid.getHorizontalRanges(y)
            var left: Range? = null
            for ((i, range) in horizontalRanges.withIndex()) {
                if (left == null) {
                    left = range
                    continue
                }

                sum += left.range + (range.minX-1 - left.maxX)
                if ((horizontalRanges.size - (i + 1)) % 2 == 0) {
                    sum += range.range
                    left = null
                } else {
                    left = range
                }

//                if (range.range == 1) {
//                    sum += left.range + range.range + (range.minX-1 - left.maxX)
//                    left = null
//                } else {
//
//                    if ((horizontalRanges.size - (i + 1)) % 2 == 0) {
//                        sum += range.range
//                        left = null
//                    } else {
//                        left = range
//                    }
//                }
            }
            if (left != null) {
                sum += left.range
            }
        }

        println(sum)

        return sum
    }

    private fun internalCalculateV3(instructions: List<Instruction>): Long {
        var sum = 0L
        var numWalls = 0L
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
            if (r.isHorizontalRange && r.range > 1) {
                val offset = if (end.x > start.x) 1 else -1
//                val offset = 0
                sum += (start.y) * (end.x - start.x - offset)
            } else {
//                sum += 1 * end.y
            }
        }

        println("sum1: $sum")

        sum = abs(sum / 2) + numWalls

        println(grid)
        println("width: ${grid.width}")
        println("height: ${grid.height}")
        println("numWalls: $numWalls")
        println("sum: $sum")

        return sum
    }

    private fun part1Calculation(input: List<String>): Long {
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

        return internalCalculateV3(instructions)
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun part2Calculation(input: List<String>, pointInInner: IntPoint): Long {
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

        return 0L
//        return internalCalculateV2(instructions)
    }

    @Test
    fun part1Test() {
        val input = readInput(testFileName)
        assertThat(part1Calculation(input)).isEqualTo(62)
    }

    @Test
    fun part1Test2() {
        val input = readInput("day18_test2")
        assertThat(part1Calculation(input)).isEqualTo(18)
    }

    @Test
    fun part1() {
        val input = readInput(fileName)
        assertThat(part1Calculation(input)).isEqualTo(61865)
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