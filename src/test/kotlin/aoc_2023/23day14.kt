package aoc_2023

import com.kietyo.ktruth.assertThat
import utils.Grid
import utils.MutableArrayGrid
import utils.forEach
import utils.forEachReversed
import utils.toGrid
import kotlin.test.Test

internal class `23day14` {
    private val fileName = "day14"
    private val testFileName = "${fileName}_test"

    private fun List<String>.convertToDataObjectList() = run {
        this
    }

    fun shiftNorth(grid: Grid<Char>, newGrid: MutableArrayGrid<Char>) {
        grid.forEach { x, y, value, _ ->
            when (value) {
                '.' -> newGrid[x, y] = '.'
                '#' ->  newGrid[x, y] = '#'
                'O' -> {
                    val isEmptyAbove = (newGrid.getOrNull(x, y-1) ?: '#') == '.'
                    if (isEmptyAbove) {
                        newGrid[x,y] = '.'
                        newGrid[x,y-1] = 'O'
                    } else {
                        newGrid[x,y] = 'O'
                    }
                }
            }
        }
    }

    fun shiftWest(grid: Grid<Char>,newGrid: MutableArrayGrid<Char>) {
        grid.forEach { x, y, value, _ ->
            when (value) {
                '.' -> newGrid[x, y] = '.'
                '#' ->  newGrid[x, y] = '#'
                'O' -> {
                    val isEmptyAbove = (newGrid.getOrNull(x-1, y) ?: '#') == '.'
                    if (isEmptyAbove) {
                        newGrid[x,y] = '.'
                        newGrid[x-1,y] = 'O'
                    } else {
                        newGrid[x,y] = 'O'
                    }
                }
            }
        }
    }

    fun shiftSouth(grid: Grid<Char>, newGrid: MutableArrayGrid<Char>){
        grid.forEachReversed { x, y, value, _ ->
            when (value) {
                '.' -> newGrid[x, y] = '.'
                '#' ->  newGrid[x, y] = '#'
                'O' -> {
                    val isEmptyAbove = (newGrid.getOrNull(x, y+1) ?: '#') == '.'
                    if (isEmptyAbove) {
                        newGrid[x,y] = '.'
                        newGrid[x,y+1] = 'O'
                    } else {
                        newGrid[x,y] = 'O'
                    }
                }
            }
        }
    }

    fun shiftEast(grid: Grid<Char>, newGrid: MutableArrayGrid<Char>) {
        grid.forEachReversed { x, y, value, _ ->
            when (value) {
                '.' -> newGrid[x, y] = '.'
                '#' ->  newGrid[x, y] = '#'
                'O' -> {
                    val isEmptyAbove = (newGrid.getOrNull(x+1, y) ?: '#') == '.'
                    if (isEmptyAbove) {
                        newGrid[x,y] = '.'
                        newGrid[x+1,y] = 'O'
                    } else {
                        newGrid[x,y] = 'O'
                    }
                }
            }
        }
    }

    private fun fullyShift(initialGrid: MutableArrayGrid<Char>, initialNewGrid: MutableArrayGrid<Char>, shiftFn: (grid: Grid<Char>, newGrid: MutableArrayGrid<Char>) -> Unit) {
        var fullyShiftedGrid = initialGrid
        var newGrid = initialNewGrid
        while (true) {
            shiftFn(fullyShiftedGrid, newGrid)
            if (fullyShiftedGrid.contentEquals(newGrid)) {
                break
            }
            val tmp = fullyShiftedGrid
            fullyShiftedGrid = newGrid
            newGrid = tmp
        }
    }

    private fun part1Calculation(input: List<String>): Int {
        val converted = input.convertToDataObjectList()
        println(converted)

        val originalGrid = converted.toGrid()
        println(originalGrid)
        println()

        val fullyShiftedNorthGrid = originalGrid
        val newGrid = MutableArrayGrid.create(fullyShiftedNorthGrid.width, fullyShiftedNorthGrid.height){'.'}
        fullyShift(fullyShiftedNorthGrid, newGrid, ::shiftNorth)
        println(fullyShiftedNorthGrid)

        var sum = 0
        fullyShiftedNorthGrid.forEach { x, y, value, isFirstElementInNewRow ->
            if (value == 'O') {
                sum += (fullyShiftedNorthGrid.height - y)
            }
        }
        println(sum)

        return sum
    }

    private fun part2Calculation(input: List<String>): Int {
        val converted = input.convertToDataObjectList()
        println(converted)

        val originalGrid = converted.toGrid()
        println(originalGrid)
        println()
        val memo = mutableMapOf<List<List<Char>>, List<List<Char>>>()
        val memoItrsFirstSeen = mutableMapOf<List<List<Char>>, Long>()

        var currentGrid = originalGrid
        var newGrid = MutableArrayGrid.create(originalGrid.width, originalGrid.height){'.'}
        var i = 0L
        while (true) {
            if (i == 1_000_000_000L) break
            if (i % 1_000_000L == 0L) {
                println(i)
            }
            val currentGridData = currentGrid.data.map { it.toList() }
            if (memoItrsFirstSeen.containsKey(currentGridData)) {
                println("saw key again")
                val delta = i - memoItrsFirstSeen[currentGridData]!!
                val diff = (1_000_000_000L - i)
                if (diff / delta > 0) {
                    i += (diff) / delta * delta
                    continue
                }
            }
            if (!memoItrsFirstSeen.containsKey(currentGridData)) {
                memoItrsFirstSeen[currentGridData] = i
            }
            if (currentGridData in memo) {
                println("using cached value...")
                val tmp = currentGrid
                val endState = memo[currentGridData]!!
                endState.forEachIndexed { y, chars ->
                    for ((x, value) in chars.withIndex()) {
                        newGrid[x, y] = value
                    }
                }
                currentGrid = newGrid
                newGrid = tmp
                i++
                continue
            }

            fullyShift(currentGrid, newGrid, ::shiftNorth)
            var tmp = currentGrid
            currentGrid = newGrid
            newGrid = tmp
//            println("shifted north")
//            println(currentGrid)
//            println()

            fullyShift(currentGrid, newGrid, ::shiftWest)
            tmp = currentGrid
            currentGrid = newGrid
            newGrid = tmp
//            println("shifted west")
//            println(currentGrid)
//            println()

            fullyShift(currentGrid, newGrid, ::shiftSouth)
            tmp = currentGrid
            currentGrid = newGrid
            newGrid = tmp
//            println("shifted south")
//            println(currentGrid)
//            println()

            fullyShift(currentGrid, newGrid, ::shiftEast)
            tmp = currentGrid
            currentGrid = newGrid
            newGrid = tmp
//            println("shifted east")
//            println(currentGrid)
//            println()

            memo[currentGridData] = currentGrid.data.map { it.toList() }

            i++
        }
        println(currentGrid)

        var sum = 0
        currentGrid.forEach { x, y, value, isFirstElementInNewRow ->
            if (value == 'O') {
                sum += (currentGrid.height - y)
            }
        }
        println(sum)

        return sum
    }

    @Test
    fun part1Test() {
        val input = readInput(testFileName)
        assertThat(part1Calculation(input)).isEqualTo(136)
    }

    @Test
    fun part1() {
        val input = readInput(fileName)
        assertThat(part1Calculation(input)).isEqualTo(108857)
    }

    @Test
    fun part2Test() {
        val input = readInput(testFileName)
        assertThat(part2Calculation(input)).isEqualTo(64)
    }

    @Test
    fun part2() {
        val input = readInput(fileName)
        assertThat(part2Calculation(input)).isEqualTo(95273)
    }
}