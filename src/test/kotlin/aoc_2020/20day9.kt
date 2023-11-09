package aoc_2020

import utils.cross
import utils.println
import kotlin.test.Test
import kotlin.test.assertEquals

internal class `20day9` {
    private val fileName = "day9"
    private val testFileName = "${fileName}_test"

    private fun List<String>.convertToDataObjectList() = run {
        map { it.toLong() }
    }

    private fun part1Calculation(input: List<String>, preambleStride: Int): Long {
        val converted = input.convertToDataObjectList()
        println(converted)
        val preamble = preambleStride
        val blah = converted.windowed(preamble + 1) {
            val preambleNums = it.take(preamble)
            val currNum = it.last()
            val cross = preambleNums.cross(preambleNums, includeSameIndex = false)
            if (!cross.any {
                    (it.first + it.second) == currNum
                }) {
                currNum
            } else {
                0
            }
        }
        return blah.first { it != 0L }
    }

    private fun part2Calculation(input: List<String>, weakness: Long): Long {
        val converted = input.convertToDataObjectList()
        println(converted)
        println(weakness)
        for (i in converted.indices) {
            var sum = 0L
            var currI = i
            while (currI < converted.size - 1) {
                sum += converted[currI]
                if (sum == weakness) {
                    val nums = converted.subList(i, currI)
                    val encryptionWeakness = nums.min() + nums.max()
                    println("encryption weakness: ", encryptionWeakness)
                    return encryptionWeakness
                }
                if (sum > weakness) {
                    break
                }
                currI++
            }
        }
        return 0L
    }

    @Test
    fun part1Test() {
        val input = readInput(testFileName)
        assertEquals(127, part1Calculation(input, 5))
    }

    @Test
    fun part1() {
        val input = readInput(fileName)
        assertEquals(756008079L, part1Calculation(input, 25))
    }

    @Test
    fun part2Test() {
        val input = readInput(testFileName)
        val weakness = part1Calculation(input, 5)
        println(weakness)
        part2Calculation(input, weakness)
    }

    @Test
    fun part2() {
        val input = readInput(fileName)
        val weakness = part1Calculation(input, 25)
        assertEquals(93727241L, part2Calculation(input, weakness))
    }
}