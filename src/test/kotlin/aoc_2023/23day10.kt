package aoc_2023

import com.kietyo.ktruth.assertThat
import utils.Direction
import utils.IntPoint
import utils.MutableIntPoint
import utils.PointWithData
import utils.toGrid
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
                    Direction.UP_LEFT -> TODO()
                    Direction.UP_RIGHT -> TODO()
                    Direction.DOWN_LEFT -> TODO()
                    Direction.DOWN_RIGHT -> TODO()
                }
            }
        }

        val stepsFromStart = mutableMapOf<MutableIntPoint, Int>()
        stepsFromStart[start] = 0

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

        println(visitedPoints1)

        val res = stepsFromStart.maxOf {
            it.value
        }

        println(res)

        return res
    }

    private fun part2Calculation(input: List<String>): Int {
        val converted = input.convertToDataObjectList()
        println(converted)


        return 0
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