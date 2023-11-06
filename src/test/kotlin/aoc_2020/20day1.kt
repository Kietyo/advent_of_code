package aoc_2020

import utils.toIntList
import kotlin.test.Test

internal class `20day1` {
    @Test
    fun part1Test() {
        val input = readInput("day1_test").toIntList()
        println(input)

        input.calculateDay1Part1()
    }

    @Test
    fun part1() {
        val input = readInput("day1").toIntList()
        println(input)
        input.calculateDay1Part1()
    }

    @Test
    fun part2Test() {
        val input = readInput("day1_test").toIntList()
        println(input)

        input.calculateDay1Part2()
    }

    @Test
    fun part2() {
        val input = readInput("day1").toIntList()
        println(input)

        input.calculateDay1Part2()
    }
}

private fun List<Int>.calculateDay1Part1() {
    forEachIndexed fe1@{ index1, e1 ->
        forEachIndexed fe2@{ index2, e2 ->
            if (index1 != index2 && e1 + e2 == 2020) {
                println(e1 * e2)
                return
            }
        }
    }
}

private fun List<Int>.calculateDay1Part2() {
    forEachIndexed fe1@{ index1, e1 ->
        forEachIndexed fe2@{ index2, e2 ->
            forEachIndexed fe3@{ index3, e3 ->
                if (index1 != index2 && index2 != index3 && index1 != index3 && e1 + e2 + e3 == 2020) {
                    println(e1 * e2 * e3)
                    return
                }
            }

        }
    }
}

