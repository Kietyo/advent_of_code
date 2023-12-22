package aoc_2023

import com.kietyo.ktruth.assertThat
import utils.repeat
import kotlin.test.Test

internal class `23day12` {
    private val fileName = "day12"
    private val testFileName = "${fileName}_test"

    private fun List<String>.convertToDataObjectList() = run {
        this
    }

    class Calculator(val arrangement: List<Int>) {
        val sumDamage = arrangement.sum()
        fun calculate(curr: String): Int {
            val countUnknownSpots = curr.count { it == '?' }
            val countDamagedSpots = curr.count { it == '#' }
            if ((countUnknownSpots + countDamagedSpots) < sumDamage) {
                return 0
            }
            if (countDamagedSpots > sumDamage) {
                return 0
            }
            //            if (countUnknownSpots == 0) {
            //                println(curr)
            //            }
            //            println(curr)
            var arrangementIdx = 0
            var arrangementNum = arrangement.first()
            var currAcc = 0
            var fullyMatching = true
            var i = 0
            while (i < curr.length) {
                if (curr[i] == '?') {
                    // Still processing, break
                    fullyMatching = false
                    break
                } else if (curr[i] == '.') {
                    if (currAcc > 0) {
                        if (currAcc == arrangementNum) {
                            arrangementIdx++
                            arrangementNum = arrangement.getOrElse(arrangementIdx) { 0 }
                            currAcc = 0
                        } else {
                            return 0
                        }
                    }
                } else {
                    require(curr[i] == '#')
                    currAcc++
                }
                i++
            }

            if (currAcc > 0 && countUnknownSpots == 0) {
                if (currAcc == arrangementNum) {
                    arrangementIdx++
                } else {
                    return 0
                }
            }

            if (fullyMatching && arrangementIdx == arrangement.size) {
                return 1
            }

            if (i == curr.length && countUnknownSpots == 0) {
                return 0
            }

            return calculate(curr.replaceFirst('?', '.')) + calculate(curr.replaceFirst('?', '#'))
        }
    }

    private fun part1Calculation(input: List<String>): Int {
        val converted = input.convertToDataObjectList()
        println(converted)

        val pairs = converted.map {
            val (records, arrangementStr) = it.split(" ")
            records to arrangementStr.split(",").map { it.toInt() }
        }

        println(pairs.joinToString("\n"))

        val calc = Calculator(listOf(1, 1, 3))
        println(calc.calculate("???.###"))

        val calcs = pairs.map {
            println("Processing $it")
            val calc = Calculator(it.second)
            calc.calculate(it.first)
        }

        println(calcs)
        println(calcs.sum())

        return calcs.sum()
    }

    private fun part2Calculation(input: List<String>): Int {
        val converted = input.convertToDataObjectList()
        println(converted)

        val pairs = converted.map {
            val (records, arrangementStr) = it.split(" ")
            records.repeat(5) to arrangementStr.split(",").map { it.toInt() }.repeat(5)
        }

        println(pairs.joinToString("\n"))

        var i = 0
        val calcs = pairs.map {
            println("i: $i, Processing $it")
            i++
            val calc = Calculator(it.second)
            calc.calculate(it.first)
        }

        println(calcs)
        println(calcs.sum())

        return calcs.sum()
    }

    @Test
    fun calc1() {
        val calc = Calculator(listOf(1, 1, 3))
        assertThat(calc.calculate("???.###")).isEqualTo(1)
    }

    @Test
    fun calc2() {
        val calc = Calculator(listOf(1, 1, 3))
        assertThat(calc.calculate(".??..??...?##.")).isEqualTo(4)
    }

    @Test
    fun calc3() {
        val calc = Calculator(listOf(1, 1, 3))
        assertThat(calc.calculate("..#...#...###.")).isEqualTo(1)
    }

    @Test
    fun calc4() {
        val calc = Calculator(listOf(3, 2, 1))
        assertThat(calc.calculate("?###????????")).isEqualTo(10)
    }

    @Test
    fun part1Test() {
        val input = readInput(testFileName)
        assertThat(part1Calculation(input)).isEqualTo(21)
    }

    @Test
    fun part1() {
        val input = readInput(fileName)
        assertThat(part1Calculation(input)).isEqualTo(7163)
    }

    @Test
    fun part2Test() {
        val input = readInput(testFileName)
        assertThat(part2Calculation(input)).isEqualTo(525152)
    }

    @Test
    fun part2() {
        val input = readInput(fileName)
        assertThat(part2Calculation(input)).isEqualTo(0)
    }
}

