package aoc_2020

import utils.Direction
import utils.MutableGrid
import utils.PointWithData
import utils.toGrid
import kotlin.test.Test

internal class `20day11` {
    private val fileName = "day11"
    private val testFileName = "${fileName}_test"

    private val EMPTY = 'L'
    private val OCCUPIED = '#'

    private fun List<String>.convertToDataObjectList() = run {
        this.toGrid()
    }

    private fun simulateRound(grid: MutableGrid<Char>): MutableGrid<Char> {
        val newGrid = grid.copy()
        grid.getAllPoints(EMPTY).forEach {
            val numOccupiedAdjacents = grid.getAdjacents(it.x, it.y).filter { it.data == OCCUPIED }.size
            if (numOccupiedAdjacents == 0) {
                newGrid[it.x, it.y] = OCCUPIED
            }
        }
        grid.getAllPoints(OCCUPIED).forEach {
            val numOccupiedAdjacents = grid.getAdjacents(it.x, it.y).filter { it.data == OCCUPIED }.size
            if (numOccupiedAdjacents >= 4) {
                newGrid[it.x, it.y] = EMPTY
            }
        }
        return newGrid
    }

    private fun MutableGrid<Char>.getStrideAdjacents(x: Int, y: Int): List<PointWithData<Char>> {
        return Direction.entries.mapNotNull {
            getStrideFrom(x, y, it).firstOrNull {
                it.data != '.'
            }
        }
    }


    private fun simulateRoundV2(grid: MutableGrid<Char>): MutableGrid<Char> {
        val newGrid = grid.copy()
        grid.getAllPoints(EMPTY).forEach {
            val strideAdjacents = grid.getStrideAdjacents(it.x, it.y)
            val numOccupiedAdjacents = strideAdjacents.filter { it.data == OCCUPIED }.size
            if (numOccupiedAdjacents == 0) {
                newGrid[it.x, it.y] = OCCUPIED
            }
        }
        grid.getAllPoints(OCCUPIED).forEach {
            val numOccupiedAdjacents = grid.getStrideAdjacents(it.x, it.y).filter { it.data == OCCUPIED }.size
            if (numOccupiedAdjacents >= 5) {
                newGrid[it.x, it.y] = EMPTY
            }
        }
        return newGrid
    }

    private fun part1Calculation(input: List<String>) {
        val converted = input.convertToDataObjectList()

        var curr = converted
        while (true) {
            val newGrid = simulateRound(curr.copy())
            if (curr.contentEquals(newGrid)) {
                break
            }
            curr = newGrid
        }

        println(curr.getAllPoints(OCCUPIED).size)
    }

    private fun part2Calculation(input: List<String>) {
        val converted = input.convertToDataObjectList()
        var curr = converted
        while (true) {
            println("------------------------------")
            println(curr)
            val newGrid = simulateRoundV2(curr.copy())
            if (curr.contentEquals(newGrid)) {
                break
            }
            curr = newGrid
        }

        println(curr.getAllPoints(OCCUPIED).size)
    }

    @Test
    fun part1Test() {
        val input = readInput(testFileName)
        part1Calculation(input)
    }

    @Test
    fun part1() {
        val input = readInput(fileName)
        part1Calculation(input)
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

