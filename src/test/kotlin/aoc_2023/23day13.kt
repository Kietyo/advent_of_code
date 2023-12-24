package aoc_2023

import com.kietyo.ktruth.assertThat
import utils.Grid
import utils.MutableGrid
import utils.forEach
import utils.splitByNewLine
import utils.toGrid
import kotlin.test.Test

internal class `23day13` {
    private val fileName = "day13"
    private val testFileName = "${fileName}_test"

    private fun List<String>.convertToDataObjectList() = run {
        this
    }

    enum class Alignment {
        VERTICAL, HORIZONTAL
    }

    data class MirrorIndexResult(
        val verticalI: Int? = null,
        val horizontalI: Int? = null
    ) {
        fun isVerticalMirror() = verticalI != null
        fun isHorizontalMirror() = horizontalI != null
        fun hasMirror() = isVerticalMirror() || isHorizontalMirror()
        fun onlyHasOneAlignment() = isVerticalMirror() xor isHorizontalMirror()
        fun calculateScore(): Int {
            require((horizontalI == null) xor (verticalI == null))
            if (horizontalI != null) {
                return (horizontalI+1) * 100
            }
            if (verticalI != null) {
                return verticalI+1
            }
            TODO()
        }

        fun calculateScoreUsingAlignment(alignment: Alignment): Int {
            return when (alignment) {
                Alignment.VERTICAL -> {
                    require(verticalI != null)
                    return verticalI+1
                }
                Alignment.HORIZONTAL -> {
                    require(horizontalI != null)
                    return (horizontalI+1)*100
                }
            }
        }
    }

    private fun calculateMirrorIndexes(grid: Grid<Char>): MirrorIndexResult {
        val verticalMirrorI = run {
            for (i in 0..<grid.width-1) {
                var leftI = i
                var rightI = i+1
                var numEquals = 0
                var isMirror = false
                while (true) {
                    if (leftI < 0) {
                        isMirror = true
                        break
                    }
                    if (rightI >= grid.width) {
                        isMirror = true
                        break
                    }
                    val leftRow = grid.getColumn(leftI)
                    val rightRow = grid.getColumn(rightI)
                    if (leftRow == rightRow) {
                        numEquals++
                        leftI--
                        rightI++
                    } else {
                        break
                    }
                }
                if (isMirror) {
                    return@run i
                }
            }
            return@run null
        }

        val horizontalMirrorI =  run {
            for (i in 0..<grid.height-1) {
                var upI = i
                var downI = i+1
                var numEquals = 0
                var isMirror = false
                while (true) {
                    if (upI < 0) {
                        isMirror = true
                        break
                    }
                    if (downI >= grid.height) {
                        isMirror = true
                        break
                    }
                    val upRow = grid.getRow(upI)
                    val downRow = grid.getRow(downI)
                    if (upRow == downRow) {
                        numEquals++
                        upI--
                        downI++
                    } else {
                        break
                    }
                }
                if (isMirror) {
                    return@run i
                }
            }
            return@run null
        }
        return MirrorIndexResult(verticalMirrorI, horizontalMirrorI)
    }

    private fun calculate(grid: Grid<Char>): Int {
        val calc = calculateMirrorIndexes(grid)
        return calc.calculateScore()
    }

    private fun part1Calculation(input: List<String>): Int {
        val converted = input.convertToDataObjectList()
        println(converted)

        val grids = converted.splitByNewLine().map { it.toGrid() }

        val calcs = grids.map { calculate(it) }
        println(calcs)
        println(calcs.sum())
        return calcs.sum()
    }

    private fun calculatePart2(grid: MutableGrid<Char>): Int {
        val firstCalc = calculateMirrorIndexes(grid)
        println("firstCalc: $firstCalc")

        grid.forEach { x, y, value, isFirstElementInNewRow ->
            println("x: $x, y: $y")
            when (value) {
                '.' -> grid[x, y] = '#'
                '#' -> grid[x, y] = '.'
            }
            val newMirrorIndex = calculateMirrorIndexes(grid)
            println("newMirrorIndex: $newMirrorIndex")
            when (value) {
                '.' -> grid[x, y] = '.'
                '#' -> grid[x, y] = '#'
            }
            if (newMirrorIndex.hasMirror() && firstCalc != newMirrorIndex) {
                if (firstCalc.isVerticalMirror() && newMirrorIndex.isHorizontalMirror()) {
                    return newMirrorIndex.calculateScoreUsingAlignment(Alignment.HORIZONTAL)
                }
                if (firstCalc.isHorizontalMirror() && newMirrorIndex.isVerticalMirror()) {
                    return newMirrorIndex.calculateScoreUsingAlignment(Alignment.VERTICAL)
                }
                if (newMirrorIndex.onlyHasOneAlignment()) {
                    return newMirrorIndex.calculateScore()
                }
                TODO()
            }


        }
        TODO()
    }

    private fun part2Calculation(input: List<String>): Int {
        val converted = input.convertToDataObjectList()
        println(converted)

        val grids = converted.splitByNewLine().map { it.toGrid() }

        val calcs = grids.map {
            println("processing new grid:")
            println(it)
            calculatePart2(it)
        }

        println(calcs)
        println(calcs.sum())

        return 0
    }

    @Test
    fun part1Test() {
        val input = readInput(testFileName)
        assertThat(part1Calculation(input)).isEqualTo(405)
    }

    @Test
    fun part1() {
        val input = readInput(fileName)
        assertThat(part1Calculation(input)).isEqualTo(40006)
    }

    @Test
    fun part2Test() {
        val input = readInput(testFileName)
        assertThat(part2Calculation(input)).isEqualTo(0)
    }

    @Test
    fun part2Test2() {
        val input = readInput("day13_test2")
        assertThat(part2Calculation(input)).isEqualTo(0)
    }

    @Test
    fun part2() {
        val input = readInput(fileName)
        assertThat(part2Calculation(input)).isEqualTo(0)
    }
}