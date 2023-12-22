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

    class Calculator(val arrangement: List<Int>) {
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

        val memo = mutableMapOf<Item, Int>()

        fun calculate(curr: String): Int {
            return calculateInternal(Item(arrangement, curr))
        }



        fun calculateInternal(curr: Item): Int {
            val currArrangement = mutableListOf<Int>()
            var currNumDmg = 0
            var containsUnknown = false
            var lastIWithDot = -1
            var lastI = 0
            for ((i, c) in curr.s.withIndex()) {
                lastI = i
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
            }

            if (lastIWithDot != -1) {
                val firstHalf = curr.s.substring(0..<lastIWithDot)
                val secondHalf = curr.s.substring(lastIWithDot..<curr.s.length)
                println("here, firstHalf: $firstHalf, secondHalf: $secondHalf")
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

            if (lastI == curr.s.lastIndex) {
                if (currArrangement == curr.arr) {
                    return 1
                } else {
                    return 0
                }
            }

            val res  = calculateInternal(Item(curr.arr, curr.s.replaceFirst('?', '#'))) +
                    calculateInternal(Item(curr.arr, curr.s.replaceFirst('?', '.')))
            return res
        }

        fun calculateInternalOld(curr: Item): Int {
            if (curr in memo) return memo[curr]!!
            val idxOfFirstUnknown = curr.s.indexOf('?')
            if (idxOfFirstUnknown == -1) {
                if (curr.s.split(".").filter { it.isNotEmpty() }.map { it.count() } == curr.arr) {
                    memo[curr] = 1
                    return 1
                } else {
                    memo[curr] = 0
                    return 0
                }
            }

            val firstHalf = curr.s.substring(0..<idxOfFirstUnknown)
            val arr = firstHalf.split(".").filter { it.isNotEmpty() }.map { it.count() }

            if (firstHalf.isNotEmpty() && firstHalf.contains("#")) {
                if (firstHalf.endsWith(".")) {
                    val expectToMatch = curr.arr.take(arr.size)
                    if (arr != expectToMatch) {
                        return 0
                    }
                } else {
                    if (arr.dropLast(1) != curr.arr.take(arr.size - 1)) {
                        return 0
                    }

                    println("curr: $curr")
                    if (arr.last() > curr.arr[arr.size-1]) {
                        return 0
                    }
                }

            }
            val secondHalf = curr.s.substring(idxOfFirstUnknown..<curr.s.length)


            if (firstHalf.isNotEmpty() && firstHalf.last() == '.') {
                println("curr: $curr, firstHalf: $firstHalf, secondHalf: $secondHalf")

                if (!firstHalf.contains('?')) {
                    val remainingArrangements = curr.arr.subList(arr.size, curr.arr.size)
                    val res = calculateInternal(Item(remainingArrangements, secondHalf))
                    memo[curr] = res
                    return res
                }
            }

            val res = calculateInternal(Item(curr.arr, curr.s.replaceFirst('?', '.'))) + calculateInternal(Item(curr.arr, curr.s.replaceFirst('?', '#')))
            memo[curr] = res
            return res
        }

        fun calculate2(curr: String): Int {
            val currArr = curr.toCharArray()
            return calculateInternal2(currArr, 0, 0, 0)
        }

        fun calculateInternal2(curr: CharArray, currIdx: Int, currArrangementIdx: Int, currDamage: Int): Int {
            if (currDamage > 0 && currArrangementIdx >= arrangement.size) {
                return 0
            }
            if (currIdx == curr.size) {
                if (currDamage > 0) {
                    if (currArrangementIdx >= arrangement.size) {
                        return 0
                    }
                    if (arrangement[currArrangementIdx] != currDamage) {
                        return 0
                    }
                    if (arrangement[currArrangementIdx] == currDamage && currArrangementIdx == arrangement.size-1) {
                        return 1
                    }
                }
                if (currDamage == 0 && currArrangementIdx == arrangement.size) {
                    return 1
                }
                if (currDamage == 0 && currArrangementIdx < arrangement.size) {
                    return 0
                }
                if (currDamage > 0 && currArrangementIdx < arrangement.size) {
                    return 0
                }
                println("see here")
            }

            if (curr[currIdx] == '.') {
                if (currDamage == 0) {
                    return calculateInternal2(curr, currIdx+1, currArrangementIdx, currDamage)
                } else {
                    if (currDamage == arrangement[currArrangementIdx]) {
                        return calculateInternal2(curr, currIdx+1, currArrangementIdx+1, 0)
                    } else {
                        return 0
                    }
                }
            } else if (curr[currIdx] == '#') {
                return calculateInternal2(curr, currIdx+1, currArrangementIdx, currDamage+1)
            } else {
                curr[currIdx] = '.'
                val sum1 = calculateInternal2(curr, currIdx, currArrangementIdx, currDamage)
                curr[currIdx] = '#'
                val sum2 = calculateInternal2(curr, currIdx, currArrangementIdx, currDamage)
                curr[currIdx] = '?'
                return sum1 + sum2
            }

        }
        fun calculateOld(curr: String): Int {
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

        val calcs = pairs.map {
            println("Processing $it")
            val calc = Calculator(it.second)
            calc.calculate2(it.first)
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
            (0..4).map { records }.joinToString("?") to arrangementStr.split(",").map { it.toInt() }.repeat(5)
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
    fun calc5() {
        val calc = Calculator(listOf(1,2,2))
        assertThat(calc.calculate("?.????????#???")).isEqualTo(34)
    }

    @Test
    fun calc6() {
        val calc = Calculator(listOf(1, 3, 1, 6, 1, 3, 1, 6, 1, 3, 1, 6, 1, 3, 1, 6, 1, 3, 1, 6))
        assertThat(calc.calculate("?#?#?#?#?#?#?#??#?#?#?#?#?#?#??#?#?#?#?#?#?#??#?#?#?#?#?#?#??#?#?#?#?#?#?#?")).isEqualTo(34)
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

//    @Test
//    fun part2Test() {
//        val input = readInput(testFileName)
//        assertThat(part2Calculation(input)).isEqualTo(525152)
//    }
//
//    @Test
//    fun part2() {
//        val input = readInput(fileName)
//        assertThat(part2Calculation(input)).isEqualTo(0)
//    }
}

