package aoc_2024

import com.kietyo.ktruth.assertThat
import kotlin.test.Test

internal class `24day5` {
    private val fileName = "day5"
    private val testFileName = "${fileName}_test"

    data class Struct(
        val pairs: List<Pair<Int, Int>>,
        val updateLines: List<List<Int>>,
    )

    private fun List<String>.convertToDataObjectList(): Struct = run {
        val pairs = mutableListOf<Pair<Int, Int>>()
        val updateLines = mutableListOf<List<Int>>()
        for (e in this) {
            if ('|' in e) {
                val (e1,e2) = e.split("|").map { it.toInt() }
                pairs.add(e1 to e2)
            }
            if (',' in e) {
                updateLines.add(e.split(",").map { it.toInt() })
            }
        }
        return Struct(pairs, updateLines)
    }

    private fun part1Calculation(input: List<String>): Int {
        val converted = input.convertToDataObjectList()
        println(converted)


        converted.updateLines.forEach { updates ->
            val previouslySeen = mutableListOf<Int>()

            updates.forEach { e ->
                if (previouslySeen.isEmpty()) {
                    previouslySeen.add(e)
                } else {

                }
            }
        }

        return 0
    }

    private fun part2Calculation(input: List<String>): Int {
        val converted = input.convertToDataObjectList()
        println(converted)

        return 0
    }

    @Test
    fun part1Test() {
        val input = readInput(testFileName)
        assertThat(part1Calculation(input)).isEqualTo(0)
    }

    @Test
    fun part1() {
        val input = readInput(fileName)
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