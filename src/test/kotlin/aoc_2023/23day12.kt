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

    companion object {
        fun calculateCurrentArrangement(s: String): List<Int> {
            require(!s.contains("?"))
            val currArrangement = mutableListOf<Int>()
            var currNumDmg = 0
            for ((i, c) in s.withIndex()) {
                if (c == '#') {
                    currNumDmg++
                    if (i == s.lastIndex) {
                        currArrangement.add(currNumDmg)
                        currNumDmg = 0
                    }
                } else {
                    if (currNumDmg > 0) {
                        currArrangement.add(currNumDmg)
                        currNumDmg = 0
                    }
                }
            }
            return currArrangement
        }
    }



    internal class Calculator(val arrangement: List<Int>) {
        val sumDamage = arrangement.sum()
        val remainingSumDamagePerIndex = buildList<Int> {
            arrangement.reduceRight { it1, it2 ->
                val sum = it1+it2
                add(sum)
                sum
            }

            reverse()
            add(arrangement.first())
        }

        init {
            println("arrangement: $arrangement")
            println("remainingSumDamagePerIndex: $remainingSumDamagePerIndex")
        }

        data class Item(val arr: List<Int>, val s: String)

        val memo = mutableMapOf<Item, Long>()

        fun calculatePublic(curr: String): Long {
            return calculateInternal(Item(arrangement, curr))
        }

        fun calculate(curr: Item): Long {
            if (memo.containsKey(curr)) return memo[curr]!!
            val res = calculateInternal(curr)
            memo[curr] = res
            return res
        }

        fun calculateInternal(curr: Item): Long {
            val currArrangement = mutableListOf<Int>()
            var currNumDmg = 0
            var containsUnknown = false
            var lastIWithDot = -1
            var lastI = 0
            for ((i, c) in curr.s.withIndex()) {
                if (c == '?') {
                    containsUnknown = true
                    break
                } else if (c == '#') {
                    currNumDmg++
                    if (i == curr.s.lastIndex) {
                        currArrangement.add(currNumDmg)
                        currNumDmg = 0
                    }
                } else {
                    lastIWithDot = i
                    if (currNumDmg > 0) {
                        currArrangement.add(currNumDmg)
                        currNumDmg = 0
                    }
                }
                lastI++
            }

            if (currArrangement.isEmpty() && currNumDmg > 0) {
                if (curr.arr.isEmpty()) {
                    return 0
                }
                if (currNumDmg > curr.arr.first()) {
                    return 0
                }
            }

            if (lastIWithDot != -1) {
                val firstHalf = curr.s.substring(0..<lastIWithDot)
                if (firstHalf.isNotEmpty()) {
                    val secondHalf = curr.s.substring(lastIWithDot..<curr.s.length)
                    val firstHalfArr = calculateCurrentArrangement(firstHalf)
                    val expectedArrangement = curr.arr.take(firstHalfArr.size)
                    if (firstHalfArr == expectedArrangement) {
                        return calculate(Item(curr.arr.drop(firstHalfArr.size), secondHalf))
                    } else {
                        return 0
                    }
                }
            }

            if (currArrangement.isNotEmpty()) {
                if (currArrangement.size > curr.arr.size) {
                    return 0
                }

                for (i in 0..<currArrangement.size-1) {
                    val a = currArrangement[i]
                    val b = curr.arr[i]
                    if (a != b) {
                        return 0
                    }
                }

                if (currArrangement.last() > curr.arr[currArrangement.size-1]) {
                    return 0
                }
            }

            if (lastI == curr.s.length) {
                if (currArrangement == curr.arr) {
                    return 1
                } else {
                    return 0
                }
            }

            return calculate(Item(curr.arr, curr.s.replaceFirst('?', '#'))) +
                    calculate(Item(curr.arr, curr.s.replaceFirst('?', '.')))
        }


    }

    private fun part1Calculation(input: List<String>): Long {
        val converted = input.convertToDataObjectList()
        println(converted)

        val pairs = converted.map {
            val (records, arrangementStr) = it.split(" ")
            records to arrangementStr.split(",").map { it.toInt() }
        }

        println(pairs.joinToString("\n"))

        val calcs = pairs.map {
            println("Processing $it")
            val calc = Calculator(it.second)
            calc.calculatePublic(it.first)
        }

        println(calcs)
        println(calcs.sum())

        return calcs.sum()
    }

    private fun part2Calculation(input: List<String>): Long {
        val converted = input.convertToDataObjectList()
        println(converted)

        val pairs = converted.map {
            val (records, arrangementStr) = it.split(" ")
            (0..4).map { records }.joinToString("?") to arrangementStr.split(",").map { it.toInt() }.repeat(5)
        }

        println(pairs.joinToString("\n"))

        var i = 0
        val calcs = pairs.map {
            println("i: $i, Processing $it")
            i++
            val calc = Calculator(it.second)
            calc.calculatePublic(it.first).toLong()
        }

        println(calcs)
        println(calcs.sum())

        return calcs.sum()
    }

    @Test
    fun calculateCurrentArrangement1() {
        assertThat(calculateCurrentArrangement("###")).isEqualTo(listOf(3))
    }

    @Test
    fun calculateCurrentArrangement2() {
        assertThat(calculateCurrentArrangement("###.#.#")).isEqualTo(listOf(3, 1, 1))
    }

    @Test
    fun calculateCurrentArrangement3() {
        assertThat(calculateCurrentArrangement(".")).isEqualTo(listOf())
    }

    @Test
    fun calculateCurrentArrangement4() {
        assertThat(calculateCurrentArrangement("#")).isEqualTo(listOf(1))
    }

    @Test
    fun calculateCurrentArrangement5() {
        assertThat(calculateCurrentArrangement("#.")).isEqualTo(listOf(1))
    }

    @Test
    fun calc1() {
        val calc = Calculator(listOf(1, 1, 3))
        assertThat(calc.calculatePublic("???.###")).isEqualTo(1)
    }

    @Test
    fun calc2() {
        val calc = Calculator(listOf(1, 1, 3))
        assertThat(calc.calculatePublic(".??..??...?##.")).isEqualTo(4)
    }

    @Test
    fun calc3() {
        val calc = Calculator(listOf(1, 1, 3))
        assertThat(calc.calculatePublic("..#...#...###.")).isEqualTo(1)
    }

    @Test
    fun calc4() {
        val calc = Calculator(listOf(3, 2, 1))
        assertThat(calc.calculatePublic("?###????????")).isEqualTo(10)
    }

    @Test
    fun calc5() {
        val calc = Calculator(listOf(1,2,2))
        assertThat(calc.calculatePublic("?.????????#???")).isEqualTo(34)
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
    fun blah() {
//        val bad = listOf(3, 6, 2, 9, 47, 3, 2, 5, 29, 8, 2, 2, 1, 10, 3, 2, 1, 4, 8, 10, 10, 3, 13, 6, 4, 2, 4, 11, 15, 4, 2, 2, 6, 20, 4, 6, 3, 6, 4, 28, 5, 7, 9, 2, 16, 3, 3, 10, 3, 1, 4, 6, 4, 6, 9, 1, 7, 1, 2, 5, 3, 5, 2, 18, 2, 6, 8, 2, 7, 13, 3, 3, 4, 2, 2, 8, 3, 12, 7, 14, 15, 2, 2, 1, 2, 2, 5, 4, 7, 2, 5, 6, 9, 1, 157, 2, 2, 3, 1, 1, 3, 2, 5, 6, 5, 2, 9, 1, 1, 4, 55, 5, 10, 6, 2, 9, 2, 12, 2, 4, 12, 15, 3, 3, 13, 12, 4, 2, 4, 75, 10, 21, 6, 1, 33, 1, 4, 3, 4, 1, 1, 28, 10, 6, 4, 6, 2, 4, 7, 12, 21, 36, 4, 3, 29, 7, 2, 3, 2, 1, 1, 2, 4, 2, 49, 16, 16, 1, 2, 20, 10, 6, 4, 3, 15, 2, 21, 1, 2, 2, 1, 2, 3, 2, 10, 12, 12, 4, 2, 10, 2, 1, 1, 7, 2, 4, 2, 1, 4, 9, 5, 2, 26, 4, 5, 4, 3, 2, 6, 2, 5, 4, 5, 2, 3, 25, 2, 21, 6, 4, 1, 2, 2, 26, 17, 24, 7, 18, 2, 22, 3, 1, 3, 12, 3, 1, 28, 13, 1, 2, 2, 5, 4, 2, 13, 1, 9, 6, 1, 11, 10, 5, 6, 15, 12, 41, 4, 8, 6, 3, 9, 14, 16, 3, 1, 39, 11, 10, 1, 13, 20, 2, 2, 6, 18, 4, 1, 3, 8, 2, 5, 4, 2, 6, 1, 2, 3, 48, 2, 2, 3, 9, 9, 3, 56, 8, 5, 7, 4, 4, 4, 2, 3, 12, 4, 4, 6, 1, 2, 4, 3, 1, 1, 6, 3, 9, 1, 5, 23, 14, 3, 4, 49, 5, 3, 1, 1, 2, 1, 3, 2, 6, 6, 18, 13, 6, 5, 6, 10, 11, 10, 4, 24, 2, 15, 2, 8, 2, 1, 2, 10, 2, 25, 8, 1, 1, 2, 4, 1, 3, 2, 8, 18, 2, 2, 4, 21, 1, 8, 3, 1, 1, 2, 11, 6, 6, 28, 3, 6, 37, 6, 36, 1, 4, 4, 6, 2, 2, 3, 1, 13, 3, 3, 3, 4, 3, 3, 4, 2, 2, 3, 40, 4, 7, 2, 1, 3, 4, 6, 4, 50, 15, 2, 3, 10, 2, 3, 1, 5, 5, 5, 10, 8, 3, 1, 1, 36, 6, 1, 7, 2, 6, 3, 4, 3, 4, 4, 5, 3, 11, 6, 9, 2, 10, 6, 6, 1, 1, 8, 7, 2, 2, 8, 2, 3, 9, 4, 4, 5, 4, 20, 4, 3, 2, 12, 2, 2, 7, 13, 34, 22, 4, 9, 8, 6, 9, 4, 7, 3, 5, 1, 3, 2, 2, 9, 6, 1, 4, 5, 1, 19, 3, 30, 15, 4, 6, 1, 8, 7, 3, 1, 2, 17, 14, 4, 11, 8, 2, 3, 1, 6, 1, 7, 9, 1, 8, 12, 43, 9, 1, 1, 1, 2, 3, 5, 3, 6, 4, 3, 5, 4, 4, 3, 2, 4, 4, 10, 1, 29, 2, 13, 47, 1, 1, 1, 5, 6, 2, 2, 3, 2, 3, 7, 21, 6, 2, 3, 5, 5, 2, 2, 2, 2, 158, 10, 10, 24, 1, 10, 3, 1, 3, 4, 6, 34, 24, 4, 4, 7, 40, 5, 5, 9, 2, 4, 6, 34, 3, 18, 2, 2, 1, 3, 10, 3, 5, 1, 4, 7, 4, 1, 10, 12, 35, 6, 8, 2, 18, 2, 2, 2, 3, 10, 1, 32, 5, 3, 2, 3, 4, 119, 1, 3, 6, 10, 2, 7, 16, 3, 6, 6, 5, 10, 5, 21, 2, 20, 7, 3, 14, 2, 6, 18, 7, 6, 2, 2, 4, 4, 15, 3, 22, 3, 4, 2, 2, 3, 6, 3, 2, 2, 4, 55, 13, 12, 22, 13, 2, 1, 14, 4, 2, 6, 8, 3, 19, 4, 3, 13, 2, 3, 3, 3, 2, 4, 16, 5, 21, 11, 6, 15, 1, 5, 12, 6, 2, 2, 2, 8, 10, 10, 2, 24, 14, 4, 3, 6, 1, 6, 3, 1, 4, 3, 3, 10, 12, 14, 3, 24, 21, 6, 3, 4, 6, 2, 2, 25, 3, 2, 1, 10, 6, 9, 1, 20, 2, 2, 22, 3, 5, 6, 5, 4, 3, 4, 2, 6, 1, 9, 2, 2, 4, 3, 9, 4, 1, 4, 6, 35, 6, 6, 3, 4, 6, 2, 1, 27, 1, 3, 35, 3, 1, 1, 7, 45, 14, 2, 1, 6, 8, 1, 1, 3, 3, 4, 20, 10, 3, 8, 4, 13, 2, 2, 2, 29, 4, 2, 4, 2, 1, 5, 10, 1, 9, 1, 6, 4, 1, 2, 19, 18, 4, 3, 3, 40, 2, 4, 2, 2, 5, 4, 2, 7, 4, 20, 1, 5, 1, 6, 3, 3, 7, 7, 4, 41, 9, 6, 10, 4, 4, 1, 2, 1, 2, 37, 37, 48, 7, 10, 14, 6, 6, 47, 6, 5, 4, 20, 2, 7, 4, 4, 7, 1, 12, 35, 10, 1, 4, 35, 4, 6, 3, 29, 3, 2, 3, 2, 14, 5, 13, 6, 8, 3, 3, 1, 4, 6, 10, 20, 3, 1, 6, 6, 1, 5, 5, 4, 1, 1, 18, 5, 5, 1, 9, 13, 4, 3, 3, 3, 3, 12, 1, 1, 1, 11, 15, 6, 2, 2, 2, 4, 4, 1, 2, 2, 8, 6, 3, 1, 33, 3, 3, 2, 2, 2, 4, 2, 3, 1, 1, 2, 6, 2, 6, 1, 4, 1, 2, 2, 2, 2, 2, 38, 4, 3, 28, 4, 1, 12, 2, 5, 54, 15, 5, 2, 3, 20, 12, 18, 7, 2, 2, 4, 2, 4, 2, 8, 10, 2, 2, 2, 3, 6, 1, 3, 6, 10, 27, 10, 8, 4, 1, 4, 1, 18, 6, 5, 12, 2, 2)
//        val correct = listOf(3, 6, 2, 9, 34, 3, 1, 5, 29, 8, 2, 2, 1, 6, 3, 2, 1, 3, 7, 7, 6, 3, 13, 6, 4, 2, 4, 7, 15, 4, 2, 2, 6, 20, 4, 2, 3, 6, 4, 20, 5, 5, 7, 2, 10, 3, 3, 10, 3, 1, 4, 3, 4, 6, 9, 1, 7, 1, 2, 5, 3, 2, 2, 18, 2, 6, 8, 2, 7, 13, 3, 2, 3, 2, 2, 6, 3, 12, 7, 14, 15, 2, 2, 1, 2, 2, 5, 4, 7, 2, 5, 6, 9, 1, 141, 2, 2, 3, 1, 1, 3, 2, 4, 6, 4, 2, 9, 1, 1, 4, 55, 1, 10, 6, 2, 9, 2, 12, 2, 2, 12, 15, 3, 3, 6, 12, 2, 2, 4, 75, 10, 21, 6, 1, 33, 1, 4, 3, 2, 1, 1, 23, 10, 6, 4, 6, 2, 4, 7, 12, 21, 36, 4, 3, 17, 7, 2, 3, 2, 1, 1, 2, 4, 2, 49, 9, 16, 1, 2, 10, 10, 6, 4, 3, 13, 2, 20, 1, 2, 2, 1, 2, 3, 2, 10, 11, 11, 4, 2, 10, 2, 1, 1, 7, 1, 4, 2, 1, 4, 9, 5, 2, 26, 4, 5, 4, 3, 2, 5, 2, 5, 4, 5, 2, 2, 25, 2, 21, 5, 4, 1, 2, 2, 26, 17, 24, 7, 10, 2, 22, 3, 1, 3, 12, 3, 1, 22, 13, 1, 2, 2, 5, 2, 2, 13, 1, 9, 6, 1, 8, 10, 5, 6, 15, 12, 41, 4, 8, 4, 3, 9, 14, 16, 3, 1, 39, 11, 10, 1, 5, 19, 2, 2, 3, 16, 4, 1, 3, 8, 2, 5, 4, 2, 4, 1, 1, 3, 48, 2, 1, 2, 9, 9, 3, 56, 8, 5, 7, 4, 4, 4, 2, 3, 11, 4, 4, 6, 1, 2, 4, 3, 1, 1, 6, 3, 9, 1, 5, 23, 8, 3, 4, 43, 5, 3, 1, 1, 2, 1, 3, 2, 6, 6, 18, 13, 6, 5, 6, 2, 11, 10, 4, 21, 2, 8, 2, 3, 1, 1, 2, 10, 2, 25, 4, 1, 1, 2, 4, 1, 3, 2, 7, 18, 2, 2, 4, 21, 1, 8, 2, 1, 1, 2, 5, 6, 6, 28, 3, 6, 37, 6, 35, 1, 3, 4, 4, 2, 2, 3, 1, 13, 3, 3, 3, 4, 3, 3, 4, 2, 2, 3, 40, 4, 3, 2, 1, 2, 4, 6, 3, 50, 15, 2, 3, 6, 2, 3, 1, 5, 5, 5, 10, 6, 3, 1, 1, 36, 2, 1, 7, 2, 6, 3, 4, 3, 4, 3, 5, 3, 11, 6, 9, 2, 10, 6, 6, 1, 1, 8, 7, 2, 2, 2, 2, 3, 9, 4, 4, 2, 4, 20, 3, 3, 2, 6, 2, 2, 7, 9, 26, 18, 4, 9, 8, 6, 6, 4, 7, 3, 5, 1, 3, 2, 2, 6, 6, 1, 4, 5, 1, 19, 3, 30, 15, 4, 6, 1, 8, 7, 2, 1, 2, 17, 14, 4, 11, 8, 2, 3, 1, 6, 1, 7, 8, 1, 8, 12, 43, 9, 1, 1, 1, 2, 3, 5, 3, 6, 4, 3, 3, 3, 4, 3, 2, 3, 4, 6, 1, 29, 2, 13, 47, 1, 1, 1, 5, 6, 2, 2, 3, 2, 3, 7, 19, 6, 2, 3, 5, 5, 2, 2, 2, 2, 158, 10, 10, 24, 1, 10, 3, 1, 3, 4, 3, 31, 24, 4, 4, 3, 40, 5, 5, 9, 2, 4, 6, 34, 3, 18, 2, 2, 1, 3, 10, 3, 5, 1, 4, 7, 4, 1, 10, 12, 35, 6, 8, 2, 18, 2, 2, 2, 3, 10, 1, 32, 5, 1, 2, 3, 4, 96, 1, 3, 6, 10, 2, 7, 10, 3, 6, 6, 3, 10, 5, 21, 2, 20, 7, 3, 14, 2, 6, 18, 7, 6, 2, 2, 4, 4, 15, 3, 22, 3, 4, 2, 2, 3, 6, 3, 2, 2, 4, 55, 13, 12, 18, 13, 2, 1, 14, 4, 2, 5, 8, 3, 8, 4, 3, 13, 2, 3, 3, 3, 1, 4, 16, 5, 9, 11, 6, 15, 1, 5, 4, 6, 2, 2, 2, 8, 4, 10, 2, 17, 14, 4, 2, 6, 1, 6, 3, 1, 4, 3, 3, 10, 12, 14, 3, 24, 18, 6, 3, 4, 6, 2, 2, 25, 2, 2, 1, 7, 6, 9, 1, 12, 2, 2, 13, 3, 4, 6, 4, 4, 3, 4, 2, 6, 1, 9, 2, 2, 4, 3, 3, 4, 1, 4, 6, 35, 6, 6, 3, 4, 6, 2, 1, 27, 1, 3, 35, 3, 1, 1, 7, 45, 14, 2, 1, 6, 8, 1, 1, 3, 3, 4, 20, 10, 3, 6, 4, 13, 2, 2, 2, 27, 4, 2, 4, 2, 1, 5, 10, 1, 9, 1, 6, 4, 1, 2, 19, 18, 4, 3, 3, 40, 2, 2, 2, 2, 3, 4, 2, 7, 4, 20, 1, 5, 1, 6, 3, 3, 7, 7, 2, 41, 9, 6, 10, 4, 4, 1, 2, 1, 2, 37, 37, 48, 7, 10, 14, 6, 6, 26, 6, 4, 4, 20, 2, 2, 4, 4, 7, 1, 6, 30, 10, 1, 4, 35, 4, 6, 3, 29, 3, 2, 3, 2, 14, 3, 13, 6, 1, 3, 3, 1, 4, 6, 10, 3, 3, 1, 6, 6, 1, 3, 5, 4, 1, 1, 18, 5, 5, 1, 9, 11, 4, 3, 3, 3, 3, 11, 1, 1, 1, 11, 5, 6, 1, 2, 2, 3, 4, 1, 2, 2, 4, 6, 3, 1, 28, 3, 3, 2, 2, 2, 4, 2, 3, 1, 1, 2, 6, 2, 6, 1, 3, 1, 2, 2, 2, 2, 2, 8, 1, 3, 21, 3, 1, 12, 2, 5, 43, 15, 5, 2, 3, 20, 12, 18, 7, 2, 2, 4, 2, 4, 1, 4, 10, 2, 2, 2, 3, 6, 1, 3, 6, 10, 23, 10, 8, 4, 1, 4, 1, 18, 6, 5, 3, 2, 2)

    }

    @Test
    fun part2Test() {
        val input = readInput(testFileName)
        assertThat(part2Calculation(input)).isEqualTo(525152)
    }
//
    @Test
    fun part2() {
        val input = readInput(fileName)
    // 15464163264, too low
        assertThat(part2Calculation(input)).isEqualTo(0)
    }
}

