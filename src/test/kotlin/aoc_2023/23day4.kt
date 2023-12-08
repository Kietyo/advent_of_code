package aoc_2023

import com.kietyo.ktruth.assertThat
import kotlin.math.min
import kotlin.test.Test

internal class `23day4` {
    private val fileName = "day4"
    private val testFileName = "${fileName}_test"

    private fun List<String>.convertToDataObjectList() = run {
        this
    }

    private fun part1Calculation(input: List<String>): Int {
        val converted = input.convertToDataObjectList()
        println(converted)

        val res = converted.sumOf {
            val (cardStr, bodyStr) = it.split(": ")

            val (nums1, nums2) = bodyStr.split(" | ").map {
                it.split(" ").mapNotNull {
                    it.toIntOrNull()
                }
            }

            val pow = nums1.intersect(nums2).size
            if (pow == 0) 0 else 1 shl pow-1
        }

        println(res)
        return res
    }

    private fun part2Calculation(input: List<String>): Int {
        val converted = input.convertToDataObjectList()
        println(converted)

        val cardCounts = IntArray(input.size)
        for ((i, line) in input.withIndex()) {
            println("Processing $line")
            val (cardStr, bodyStr) = line.split(": ")

            val (nums1, nums2) = bodyStr.split(" | ").map {
                it.split(" ").mapNotNull {
                    it.toIntOrNull()
                }
            }
            cardCounts[i] += 1
            val pow = nums1.intersect(nums2).size
            val res = pow
            if (res > 0) {
                for (ii in (i+1)..min(i+res, cardCounts.size-1)) {
                    cardCounts[ii] += cardCounts[i]
                }
            }

        }
        println(cardCounts.toList())
        println(cardCounts.sum())
        return cardCounts.sum()
    }

    @Test
    fun part1Test() {
        val input = readInput(testFileName)
        assertThat(part1Calculation(input)).isEqualTo(13)
    }

    @Test
    fun part1() {
        val input = readInput(fileName)
        assertThat(part1Calculation(input)).isEqualTo(24848)
    }

    @Test
    fun part2Test() {
        val input = readInput(testFileName)
        assertThat(part2Calculation(input)).isEqualTo(30)
    }

    @Test
    fun part2() {
        val input = readInput(fileName)
        assertThat(part2Calculation(input)).isEqualTo(7258152)
    }
}