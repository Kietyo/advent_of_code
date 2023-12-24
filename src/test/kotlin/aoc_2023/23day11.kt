package aoc_2023

import com.kietyo.ktruth.assertThat
import utils.MutableIntPoint
import utils.forEach
import utils.toGrid
import utils.toip
import kotlin.math.exp
import kotlin.math.max
import kotlin.math.min
import kotlin.test.Test

internal class `23day11` {
    private val fileName = "day11"
    private val testFileName = "${fileName}_test"

    private fun List<String>.convertToDataObjectList() = run {
        this
    }

    private fun part1Calculation(input: List<String>): Int {
        val converted = input.convertToDataObjectList()
        println(converted)

        val grid = converted.toGrid()
        println(grid)

        val emptyColumnIdxs = mutableListOf<Int>()
        grid.forEachColumn { x: Int, data: List<Char> ->
            if (data.all { it == '.' }) {
                emptyColumnIdxs += x
            }
        }
        val emptyRowIdxs = mutableListOf<Int>()
        grid.forEachRow { y: Int, data: List<Char> ->
            if (data.all { it == '.' }) {
                emptyRowIdxs += y
            }
        }
        println("emptyColumnIdxs: $emptyColumnIdxs, emptyRowIdxs: $emptyRowIdxs")

        val galaxyIdxs = mutableListOf<MutableIntPoint>()
        grid.forEach { x, y, value, _ ->
            if (value == '#') {
                galaxyIdxs.add(x toip y)
            }
        }
        println("galaxyIdxs: $galaxyIdxs")
//        val first = 3 toip 0
//        val second = 7 toip 8

        val distances = mutableListOf<Int>()
        galaxyIdxs.forEachIndexed l1@{ index1, first ->
            galaxyIdxs.forEachIndexed l2@{ index2, second ->
                if (index1 >= index2) return@l2
                val dst = first.manhattanDistance(second) + emptyColumnIdxs.count {
                    it in min(first.x, second.x)..max(first.x, second.x)
                } + emptyRowIdxs.count {
                    it in min(first.y, second.y)..max(first.y, second.y)
                }
                distances.add(dst)
            }
        }

        println(distances)
        println(distances.size)
        println(distances.sum())
        return distances.sum()
    }

    private fun part2Calculation(input: List<String>, expansionMultipler: Int = 2): Long {
        val converted = input.convertToDataObjectList()
        println(converted)

        val grid = converted.toGrid()
        println(grid)

        val emptyColumnIdxs = mutableListOf<Int>()
        grid.forEachColumn { x: Int, data: List<Char> ->
            if (data.all { it == '.' }) {
                emptyColumnIdxs += x
            }
        }
        val emptyRowIdxs = mutableListOf<Int>()
        grid.forEachRow { y: Int, data: List<Char> ->
            if (data.all { it == '.' }) {
                emptyRowIdxs += y
            }
        }
        println("emptyColumnIdxs: $emptyColumnIdxs, emptyRowIdxs: $emptyRowIdxs")

        val galaxyIdxs = mutableListOf<MutableIntPoint>()
        grid.forEach { x, y, value, _ ->
            if (value == '#') {
                galaxyIdxs.add(x toip y)
            }
        }
        println("galaxyIdxs: $galaxyIdxs")
        //        val first = 3 toip 0
        //        val second = 7 toip 8

        val distances = mutableListOf<Long>()
        galaxyIdxs.forEachIndexed l1@{ index1, first ->
            galaxyIdxs.forEachIndexed l2@{ index2, second ->
                if (index1 >= index2) return@l2
                val dst = first.manhattanDistance(second).toLong() + emptyColumnIdxs.count {
                    it in min(first.x, second.x)..max(first.x, second.x)
                } * (expansionMultipler - 1) + emptyRowIdxs.count {
                    it in min(first.y, second.y)..max(first.y, second.y)
                } * (expansionMultipler - 1)
                distances.add(dst)
            }
        }

        println(distances)
        println(distances.size)
        println(distances.sum())
        return distances.sum()
    }

    @Test
    fun part1Test() {
        val input = readInput(testFileName)
        assertThat(part1Calculation(input)).isEqualTo(374)
    }

    @Test
    fun part1() {
        val input = readInput(fileName)
        assertThat(part1Calculation(input)).isEqualTo(9591768)
    }

    @Test
    fun part2Test() {
        val input = readInput(testFileName)
        assertThat(part2Calculation(input, expansionMultipler = 10)).isEqualTo(1030)
    }

    @Test
    fun part2Test2() {
        val input = readInput(testFileName)
        assertThat(part2Calculation(input, expansionMultipler = 100)).isEqualTo(8410)
    }

    @Test
    fun part2() {
        val input = readInput(fileName)
        assertThat(part2Calculation(input, expansionMultipler = 1000000)).isEqualTo(746962097860L)
    }
}