package aoc_2023

import com.kietyo.ktruth.assertThat
import utils.Direction
import utils.IntPoint
import utils.MutableArrayGrid
import utils.MutableIntPoint
import utils.PointWithData
import utils.forEach
import utils.toGrid
import utils.toip
import kotlin.test.Test

internal class `23day10` {
    private val fileName = "day10"
    private val testFileName = "${fileName}_test"

    private fun List<String>.convertToDataObjectList() = run {
        this
    }

    private fun part1Calculation(input: List<String>): Int {
        val converted = input.convertToDataObjectList()
        println(converted)

        val grid = converted.toGrid()

        println(grid)

        val visitedPoints1 = mutableSetOf<IntPoint>()
        val visitedPoints2 = mutableSetOf<IntPoint>()

        val start = grid.find('S')
        visitedPoints1.add(start)
        visitedPoints2.add(start)

        var currPoint: MutableIntPoint = start

        println(currPoint)

        val allowedRelativeDirs = mapOf(
            'S' to listOf(Direction.LEFT, Direction.RIGHT, Direction.UP, Direction.DOWN),
            '-' to listOf(Direction.LEFT, Direction.RIGHT),
            '|' to listOf(Direction.UP, Direction.DOWN),
            '7' to listOf(Direction.LEFT, Direction.DOWN),
            'F' to listOf(Direction.RIGHT, Direction.DOWN),
            'J' to listOf(Direction.LEFT, Direction.UP),
            'L' to listOf(Direction.RIGHT, Direction.UP),
        )

        fun nextPoints(data: Char, pos: IntPoint): List<PointWithData<Char>> {
            val adjacents = grid.getAdjacents(pos.x, pos.y, includeDiagonals = false)
            return adjacents.filter {
                it.relativeDirection in allowedRelativeDirs[data]!!
            }.filter {
                when (it.relativeDirection) {
                    Direction.RIGHT -> it.data in listOf('-', 'J', '7')
                    Direction.LEFT -> it.data in listOf('-', 'L', 'F')
                    Direction.DOWN -> it.data in listOf('|', 'L', 'J')
                    Direction.UP -> it.data in listOf('|', '7', 'F')
                    else -> TODO()
                }
            }
        }

        val stepsFromStart = mutableMapOf<MutableIntPoint, Int>()
        stepsFromStart[start] = 0

        // Go clockwise
        while (true) {
            val prevPointSteps = stepsFromStart[currPoint]!!
            val prevPipe = grid.get(currPoint)
            val nextPoints = nextPoints(prevPipe, currPoint).filter { it.toIntPoint() !in visitedPoints1 }
            if (nextPoints.isEmpty()) break
            currPoint = nextPoints.first().toIntPoint()
            stepsFromStart[currPoint] = prevPointSteps + 1
            visitedPoints1.add(MutableIntPoint(currPoint.x, currPoint.y))
        }

        currPoint = start
        // Go counter clockwise
        while (true) {
            val prevPointSteps = stepsFromStart[currPoint]!!
            val prevPipe = grid.get(currPoint)
            val nextPoints = nextPoints(prevPipe, currPoint).filter { it.toIntPoint() !in visitedPoints2 }
            if (nextPoints.isEmpty()) break
            currPoint = nextPoints.last().toIntPoint()
            stepsFromStart[currPoint] =
                minOf(prevPointSteps + 1, stepsFromStart[currPoint]!!)
            visitedPoints2.add(MutableIntPoint(currPoint.x, currPoint.y))
        }

        val res = stepsFromStart.maxOf {
            it.value
        }

        println(res)

        return res
    }

    private fun part2Calculation(input: List<String>): Int {
        val converted = input.convertToDataObjectList()
        println(converted)

        val grid = converted.toGrid()

        println(grid)

        val pipePoints = mutableListOf<IntPoint>()

        val start = grid.find('S')
        pipePoints.add(start)

        var currPoint: MutableIntPoint = start

        println(currPoint)

        val allowedRelativeDirs = mapOf(
            'S' to listOf(Direction.LEFT, Direction.RIGHT, Direction.UP, Direction.DOWN),
            '-' to listOf(Direction.LEFT, Direction.RIGHT),
            '|' to listOf(Direction.UP, Direction.DOWN),
            '7' to listOf(Direction.LEFT, Direction.DOWN),
            'F' to listOf(Direction.RIGHT, Direction.DOWN),
            'J' to listOf(Direction.LEFT, Direction.UP),
            'L' to listOf(Direction.RIGHT, Direction.UP),
        )

        val pipeChars = allowedRelativeDirs.keys

        fun nextPoints(data: Char, pos: IntPoint): List<PointWithData<Char>> {
            val adjacents = grid.getAdjacents(pos.x, pos.y, includeDiagonals = false)
            return adjacents.filter {
                it.relativeDirection in allowedRelativeDirs[data]!!
            }.filter {
                when (it.relativeDirection) {
                    Direction.RIGHT -> it.data in listOf('-', 'J', '7')
                    Direction.LEFT -> it.data in listOf('-', 'L', 'F')
                    Direction.DOWN -> it.data in listOf('|', 'L', 'J')
                    Direction.UP -> it.data in listOf('|', '7', 'F')
                    else -> TODO()
                }
            }
        }

        val pointsInPipe = mutableMapOf<MutableIntPoint, Int>()
        pointsInPipe[start] = 0

        while (true) {
            val prevPointSteps = pointsInPipe[currPoint]!!
            val prevPipe = grid.get(currPoint)
            val nextPoints = nextPoints(prevPipe, currPoint).filter { it.toIntPoint() !in pipePoints }
            if (nextPoints.isEmpty()) break
            currPoint = nextPoints.first().toIntPoint()
            pointsInPipe[currPoint] = prevPointSteps + 1
            pipePoints.add(MutableIntPoint(currPoint.x, currPoint.y))
        }

        println("pipePoints")
        println(pipePoints)

        // Replace start pipe with the best fitting pipe
        val firstPipeFromStart = pipePoints[1]
        val lastPipeFromStart = pipePoints.last()
        val dirToFirstPipeFromStart = start.directionTo(firstPipeFromStart)
        val dirToLastPipeFromStart = start.directionTo(lastPipeFromStart)
        grid[start.x, start.y] = when (dirToFirstPipeFromStart) {
            Direction.RIGHT -> when (dirToLastPipeFromStart) {
                Direction.LEFT -> '-'
                Direction.DOWN -> 'F'
                Direction.UP -> 'L'
                else -> TODO()
            }
            Direction.LEFT -> when (dirToLastPipeFromStart) {
                Direction.RIGHT -> '-'
                Direction.DOWN -> '7'
                Direction.UP -> 'J'
                else -> TODO()
            }
            Direction.DOWN -> when (dirToLastPipeFromStart) {
                Direction.RIGHT -> 'F'
                Direction.LEFT -> '7'
                Direction.UP -> '|'
                else -> TODO()
            }
            Direction.UP -> when (dirToLastPipeFromStart) {
                Direction.RIGHT -> 'L'
                Direction.LEFT -> 'J'
                Direction.DOWN -> '|'
                else -> TODO()
            }
            else -> TODO()
        }

        val wideGrid = MutableArrayGrid(buildList {
            repeat(grid.numRows * 2) {
                add(Array(grid.numColumns * 2) {'.'})
            }
        })

        grid.forEach { x, y, value, isFirstElementInNewRow ->
            if (x toip y in pipePoints) {
                wideGrid[x * 2, y * 2] = value
            }
        }

        // Fill gaps of expanded grid
        wideGrid.forEach { x, y, value, isFirstElementInNewRow ->
            if (wideGrid.getOrNull(x - 1, y) in setOf('F', '-', 'L') && wideGrid.getOrNull(x + 1, y) in setOf('-', '7', 'J')) {
                wideGrid[x, y] = '-'
            } else if (wideGrid.getOrNull(x, y-1) in setOf('|', 'F', '7') && wideGrid.getOrNull(x, y+1) in setOf('|', '7', 'L', 'J')) {
                wideGrid[x, y] = '|'
            }
        }

        val wideGridOutPoints = mutableSetOf<MutableIntPoint>()

        fun connectsToOpenSpace(x: Int, y: Int, direction: Direction): Boolean {
            val stride = wideGrid.getStrideFrom(x, y, direction)
            for (e in stride) {
                if (e.toIntPoint() in wideGridOutPoints) return true
                return false
            }
            return true
        }

        while (true) {
            val newOutPoints = wideGrid.filter  { x, y, value ->
                if (value in pipeChars) return@filter false
                if (x toip y in wideGridOutPoints) return@filter false
                if (wideGrid.isNearWall(x, y)) return@filter true

                connectsToOpenSpace(x, y, Direction.DOWN) ||
                connectsToOpenSpace(x, y, Direction.UP) ||
                connectsToOpenSpace(x, y, Direction.LEFT) ||
                connectsToOpenSpace(x, y, Direction.RIGHT)
            }.map { it.point.x toip it.point.y }
            if (newOutPoints.isEmpty()) break
            wideGridOutPoints.addAll(newOutPoints)
        }

        wideGridOutPoints.forEach {
            wideGrid[it.x, it.y] = 'O'
        }

        println(grid)

        println(wideGrid)

        val containedPoints = wideGrid.filter { x, y, value ->
            if (x % 2 == 0 && y % 2 == 0 && value == '.') {
                val oldGridX = x / 2
                val oldGridY = y / 2
                if (oldGridX toip oldGridY in pipePoints) return@filter false
                return@filter true
            }
            false
        }

        return containedPoints.count()
    }

    @Test
    fun part1Test() {
        val input = readInput(testFileName)
        assertThat(part1Calculation(input)).isEqualTo(4)
    }

    @Test
    fun part1Test2() {
        val input = readInput("day10_test2")
        assertThat(part1Calculation(input)).isEqualTo(8)
    }

    @Test
    fun part1() {
        val input = readInput(fileName)
        assertThat(part1Calculation(input)).isEqualTo(6951)
    }

    @Test
    fun part2Test3() {
        val input = readInput("day10_test3")
        assertThat(part2Calculation(input)).isEqualTo(4)
    }

    @Test
    fun part2Test4() {
        val input = readInput("day10_test4")
        assertThat(part2Calculation(input)).isEqualTo(4)
    }

    @Test
    fun part2Test5() {
        val input = readInput("day10_test5")
        assertThat(part2Calculation(input)).isEqualTo(8)
    }

    @Test
    fun part2Test6() {
        val input = readInput("day10_test6")
        assertThat(part2Calculation(input)).isEqualTo(10)
    }

    @Test
    fun part2() {
        val input = readInput(fileName)
        // 64, not right
        assertThat(part2Calculation(input)).isEqualTo(563)
    }
}