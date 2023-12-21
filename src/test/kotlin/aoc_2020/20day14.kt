package aoc_2020

import com.kietyo.ktruth.assertThat
import utils.println
import utils.splitByNewLine
import utils.splitByPredicate
import utils.sumOf
import java.lang.StringBuilder
import kotlin.test.Ignore
import kotlin.test.Test

internal class `20day14` {
    private val fileName = "day14"
    private val testFileName = "${fileName}_test"

    private fun List<String>.convertToDataObjectList() = run {
        this
    }

    private fun calculateSum(input: List<String>, mem: MutableMap<Long, Long>) {
        val mask = input[0].split(" = ")[1].toCharArray().reversed()
        println("mask: $mask")

        var orMask = 0L
        mask.forEachIndexed { index, c ->
            if (c == '1') {
                orMask = orMask or ((1L shl index))
            }
        }

        var andMask = "1".repeat(36).toLong(2)
        mask.forEachIndexed { index, c ->
            if (c == '0') {
                andMask = andMask xor ((1L shl index).toLong())
            }
        }
        println("orMask: $orMask, ${orMask.toString(2)}")
        println("andMask: $andMask, ${andMask.toString(2)}")

        val regex = Regex("mem\\[(\\d+)\\] = (\\d+)")

        input.drop(1).forEach {
            println("processing $it")
            val matchResult = regex.matchEntire(it)
            val (idx, v) = matchResult!!.groupValues.drop(1).map { it.toLong() }
            val newValue = (v or orMask) and andMask
            println(idx, v, newValue)
            mem[idx] = newValue
        }
    }

    private fun expandNumbers(code: String): List<Long> {
        val codes = mutableListOf(code)
        val results = mutableListOf<Long>()
        while (codes.isNotEmpty()) {
            val curr = codes.removeFirst()
            if (curr.contains('X')) {
                codes.add(curr.replaceFirst('X', '0'))
                codes.add(curr.replaceFirst('X', '1'))
            } else {
                results.add(curr.toLong(2))
            }
        }
        return results
    }

    private fun calculateSum2(input: List<String>, mem: MutableMap<Long, Long>) {
        val mask = input[0].split(" = ")[1]
        println("mask: $mask")

        val regex = Regex("mem\\[(\\d+)\\] = (\\d+)")

        input.drop(1).forEach {
            println("processing $it")
            val matchResult = regex.matchEntire(it)
            val (idx, v) = matchResult!!.groupValues.drop(1).map { it.toLong() }
            val paddedIdx = idx.toString(2).padStart(36, '0')
            println("idx: $idx, v: $v, paddedIdx: $paddedIdx")

            val result = StringBuilder()

            for ((idx, c) in paddedIdx.withIndex()) {
                if (mask[idx] == '0') {
                    result.append(c)
                } else if (mask[idx] == '1') {
                    result.append('1')
                } else if (mask[idx] == 'X') {
                    result.append('X')
                } else {
                    TODO()
                }
            }

            require(result.length == 36)

            val expandedNumbers = expandNumbers(result.toString())
            for (n in expandedNumbers) {
                mem[n] = v
            }
        }
    }

    private fun part1Calculation(input: List<String>) {
        val converted = input.convertToDataObjectList()
//        println(converted)

        val splitLines = converted.splitByPredicate { it.startsWith("mask = ") }
        val mem = mutableMapOf<Long, Long>()

        splitLines.forEach {
            calculateSum(it, mem)
        }

        println(mem.sumOf { it.value })
    }

    private fun part2Calculation(input: List<String>): Long {
        val converted = input.convertToDataObjectList()
        println(converted)

        val splitLines = converted.splitByPredicate { it.startsWith("mask = ") }
        val mem = mutableMapOf<Long, Long>()

        splitLines.forEach {
            calculateSum2(it, mem)
        }

        val res = mem.sumOf { it.value }
        println(res)
        return res
    }

    @Test
    fun part1Test() {
        val input = readInput(testFileName)
        part1Calculation(input)
    }

    @Test
    fun part1() {
        val input = readInput(fileName)
        // 17735573558716, too high
        // 14839536808842
        part1Calculation(input)
    }

    @Test
    @Ignore
    fun part2Test() {
        val input = readInput(testFileName)
        part2Calculation(input)
    }

    @Test
    fun part2Test2() {
        val input = readInput("day14_test2")
        part2Calculation(input)
    }

    @Test
    fun part2() {
        val input = readInput(fileName)
        assertThat( part2Calculation(input)).isEqualTo(4215284199669L)
    }

//    000000000000000000000000000000111010  (decimal 58)
//    000000000000000000000000000000111011  (decimal 59)
//
//    000000000000000000000000000000010000  (decimal 16)
//    000000000000000000000000000000010001  (decimal 17)
//    000000000000000000000000000000010010  (decimal 18)
//    000000000000000000000000000000010011  (decimal 19)
//
//    000000000000000000000000000000011000  (decimal 24)
//    000000000000000000000000000000011001  (decimal 25)
//    000000000000000000000000000000011010  (decimal 26)
//    000000000000000000000000000000011011  (decimal 27)
}