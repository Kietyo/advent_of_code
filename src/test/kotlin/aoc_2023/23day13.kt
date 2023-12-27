package aoc_2023

import com.kietyo.ktruth.assertThat
import utils.Grid
import utils.MutableArrayGrid
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

    sealed class MirrorIndexResult {
        data class Vertical(val i: Int) : MirrorIndexResult() {
            override fun calculateScore(): Int {
                return i + 1
            }
        }

        data class Horizontal(val i: Int) : MirrorIndexResult() {
            override fun calculateScore(): Int {
                return (i + 1) * 100
            }
        }

        abstract fun calculateScore(): Int
    }

    //    data class MirrorIndexResult(
    //        val verticalIs: List<Int> = emptyList(),
    //        val horizontalIs: List<Int> = emptyList()
    //    ) {
    //        fun isVerticalMirror() = verticalIs.isNotEmpty()
    //        fun isHorizontalMirror() = horizontalIs.isNotEmpty()
    //        fun hasMirror() = isVerticalMirror() || isHorizontalMirror()
    //        fun onlyHasOneAlignment() = isVerticalMirror() xor isHorizontalMirror()
    //        fun calculateScore(): Int {
    //            require(isVerticalMirror() xor isHorizontalMirror())
    //            if (isHorizontalMirror()) {
    //                return (horizontalIs.first()+1) * 100
    //            }
    //            if (isVerticalMirror()) {
    //                return verticalIs.first()+1
    //            }
    //            TODO()
    //        }
    //
    //        fun calculateScoreUsingAlignment(alignment: Alignment): Int {
    //            return when (alignment) {
    //                Alignment.VERTICAL -> {
    //                    require(isVerticalMirror())
    //                    return verticalIs.first()+1
    //                }
    //                Alignment.HORIZONTAL -> {
    //                    require(isHorizontalMirror())
    //                    return (horizontalIs.first()+1)*100
    //                }
    //            }
    //        }
    //    }

    private fun calculateMirrorIndexes(grid: Grid<Char>): List<MirrorIndexResult> {
        val results = mutableListOf<MirrorIndexResult>()
        for (i in 0..<grid.width - 1) {
            var leftI = i
            var rightI = i + 1
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
                results.add(MirrorIndexResult.Vertical(i))
            }
        }

        for (i in 0..<grid.height - 1) {
            var upI = i
            var downI = i + 1
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
                results.add(MirrorIndexResult.Horizontal(i))
            }
        }
        return results
    }

    private fun calculate(grid: Grid<Char>): Int {
        val calc = calculateMirrorIndexes(grid)
        return calc.first().calculateScore()
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

    private fun calculatePart2(grid: MutableArrayGrid<Char>): Int {
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
            if (newMirrorIndex.isNotEmpty() && firstCalc != newMirrorIndex) {
                return (newMirrorIndex - firstCalc).first().calculateScore()
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

        return calcs.sum()
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
        assertThat(part2Calculation(input)).isEqualTo(400)
    }

    @Test
    fun part2Test2() {
        val input = readInput("day13_test2")
        assertThat(part2Calculation(input)).isEqualTo(1400)
    }

    @Test
    fun part2() {
        val input = readInput(fileName)
        assertThat(part2Calculation(input)).isEqualTo(28627)
    }
}