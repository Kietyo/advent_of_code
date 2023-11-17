package aoc_2020

import java.math.BigInteger
import kotlin.test.Test

internal class `20day13` {
    private val fileName = "day13"
    private val testFileName = "${fileName}_test"

    private fun List<String>.convertToDataObjectList() = run {
        this
    }

    private fun part1Calculation(input: List<String>) {
        val converted = input.convertToDataObjectList()
        println(converted)

        val earliestDepartableTimestamp = input[0].toInt()
        val intervals = input[1].split(",").map { it.trim() }.filter { it != "x" }.map { it.toInt() }

        println(earliestDepartableTimestamp)
        println(intervals)

        println(939 / 59)

        val departingIntervalWithTimestamp = intervals.map {
            println("interval: $it")
            val div = earliestDepartableTimestamp / it
            println("div: $div")
            val mul = div * it
            println("mul: $mul")
            println("mul + interval: ${mul + it}")
            println("---------------")
            it to (mul + it)
        }.minBy {
            it.second - earliestDepartableTimestamp
        }

        println("departingIntervalWithTimestamp: $departingIntervalWithTimestamp")
        println((departingIntervalWithTimestamp.second - earliestDepartableTimestamp) * departingIntervalWithTimestamp.first)
    }

    private fun part2Calculation(input: List<String>) {
        val converted = input.convertToDataObjectList()
        println(converted)

        val intervals = input[1].split(",").map { it.trim() }.map { it.toIntOrNull() ?: 0 }.toIntArray()
        println(intervals)
        var curr = 0L
        var count = 0L
        var maxI = 0
        var multiplier = 1
        var lowI = 1

        while (true) {
            var found = true
            var currMaxI = 0
            for (i in lowI..<intervals.size) {
                if (intervals[i] == 0) {
                    continue
                }
                if ((curr + i) % intervals[i] != 0L) {
                    found = false
                    break
                } else {
                    if (i > currMaxI) {
                        currMaxI = i
                    }
                    if (i >= 1) {
                        println("i: $i, curr: $curr")
                    }
                }
            }
            if (currMaxI > maxI) {
                println("Largest i reached: $currMaxI, intervals[i]: ${intervals[currMaxI]}, curr: $curr")
                maxI = currMaxI
            }
            if (count % 1000L == 0L) {
                println("curr: $curr")
            }
            if (count > 100000) {
                break
            }
            if (found) {
                break
            }
            curr += intervals[0] * multiplier
            count++
        }

        println(curr)
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