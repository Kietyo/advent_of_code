package aoc_2023

import com.kietyo.ktruth.assertThat
import utils.Direction
import utils.MutableGrid
import utils.MutableIntPoint
import utils.toGrid
import kotlin.test.Test

internal class `23day16` {
    private val fileName = "day16"
    private val testFileName = "${fileName}_test"

    private fun List<String>.convertToDataObjectList() = run {
        this
    }

    data class Cursor(
        val x: Int,
        val y: Int,
        val direction: Direction
    ) {
        val point get() = MutableIntPoint(x, y)
        fun move(): Cursor {
            return when (direction) {
                Direction.RIGHT -> this.copy(x = x+1)
                Direction.LEFT -> this.copy(x = x-1)
                Direction.DOWN -> this.copy(y = y+1)
                Direction.UP -> this.copy(y = y-1)
                Direction.UP_LEFT -> TODO()
                Direction.UP_RIGHT -> TODO()
                Direction.DOWN_LEFT -> TODO()
                Direction.DOWN_RIGHT -> TODO()
            }
        }
    }

    private fun part1Calculation(input: List<String>): Int {
        val converted = input.convertToDataObjectList()
        println(converted)

        val grid = converted.toGrid()

        println(grid)

        val visitedCursors = mutableSetOf<Cursor>()

        var cursors = listOf<Cursor>(Cursor(-1, 0, Direction.RIGHT))

        var i = 0
        while (cursors.isNotEmpty()) {
            println("i: $i")
            val newCursors = mutableListOf<Cursor>()
            fun addNewCursorIfNew(cursor: Cursor) {
                if (cursor !in visitedCursors) {
                    newCursors.add(cursor)
                    visitedCursors.add(cursor)
                }
            }
            for (prevCursor in cursors) {
                val prevCursorPoint = prevCursor.point
                val newCursor = prevCursor.move()
                val newPoint = newCursor.point
                if (!grid.contains(newPoint)) {
                    continue
                }
                val charUnderNewCursor = grid[newPoint]
                when (charUnderNewCursor) {
                    '.' -> {
                        addNewCursorIfNew(newCursor)
                    }
                    '|' -> {
                        when (newCursor.direction) {
                            Direction.RIGHT,
                            Direction.LEFT -> {
                                addNewCursorIfNew(Cursor(newPoint.x, newPoint.y, Direction.UP))
                                addNewCursorIfNew(Cursor(newPoint.x, newPoint.y, Direction.DOWN))
                            }
                            Direction.DOWN,
                            Direction.UP -> {
                                addNewCursorIfNew(newCursor)
                            }
                            else -> TODO()
                        }
                    }
                    '-' -> {
                        when (newCursor.direction) {
                            Direction.RIGHT,
                            Direction.LEFT -> {
                                addNewCursorIfNew(newCursor)
                            }
                            Direction.DOWN,
                            Direction.UP -> {
                                addNewCursorIfNew(Cursor(newPoint.x, newPoint.y, Direction.LEFT))
                                addNewCursorIfNew(Cursor(newPoint.x, newPoint.y, Direction.RIGHT))
                            }
                            else -> TODO()
                        }
                    }
                    '\\' -> { // \
                        val directionToSplitter = prevCursorPoint.directionTo(newPoint)
                        when (directionToSplitter) {
                            Direction.RIGHT -> {
                                addNewCursorIfNew(Cursor(newPoint.x, newPoint.y, Direction.DOWN))
                            }
                            Direction.LEFT -> {
                                addNewCursorIfNew(Cursor(newPoint.x, newPoint.y, Direction.UP))
                            }
                            Direction.DOWN -> {
                                addNewCursorIfNew(Cursor(newPoint.x, newPoint.y, Direction.RIGHT))
                            }
                            Direction.UP -> {
                                addNewCursorIfNew(Cursor(newPoint.x, newPoint.y, Direction.LEFT))
                            }
                            else -> TODO()
                        }
                    }
                    '/' -> {
                        val directionToSplitter = prevCursorPoint.directionTo(newPoint)
                        when (directionToSplitter) {
                            Direction.RIGHT -> {
                                addNewCursorIfNew(Cursor(newPoint.x, newPoint.y, Direction.UP))
                            }
                            Direction.LEFT -> {
                                addNewCursorIfNew(Cursor(newPoint.x, newPoint.y, Direction.DOWN))
                            }
                            Direction.DOWN -> {
                                addNewCursorIfNew(Cursor(newPoint.x, newPoint.y, Direction.LEFT))
                            }
                            Direction.UP -> {
                                addNewCursorIfNew(Cursor(newPoint.x, newPoint.y, Direction.RIGHT))
                            }
                            else -> TODO()
                        }
                    }
                }
            }

            cursors = newCursors

            val visitedPointsGrid = MutableGrid.create(grid.width, grid.height) {'.'}
            for (cursor in visitedCursors) {
                val point = cursor.point
                visitedPointsGrid.set(point.x, point.y, '#')
            }
            println(visitedPointsGrid)

            i++
        }

        println(visitedCursors.size)
        val count = visitedCursors.map { it.point }.toSet().size
        println(count)
        return count
    }

    private fun part2Calculation(input: List<String>): Int {
        val converted = input.convertToDataObjectList()
        println(converted)

        return 0
    }

    @Test
    fun part1Test() {
        val input = readInput(testFileName)
        assertThat(part1Calculation(input)).isEqualTo(46)
    }

    @Test
    fun part1Test2() {
        val input = readInput("day16_test2")
        assertThat(part1Calculation(input)).isEqualTo(14)
    }

    @Test
    fun part1Test3() {
        val input = readInput("day16_test3")
        assertThat(part1Calculation(input)).isEqualTo(22)
    }

    @Test
    fun part1() {
        val input = readInput(fileName)
        // 5463, too low
        // 7183, too low
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