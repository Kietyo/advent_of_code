package aoc_2024

import com.kietyo.ktruth.assertThat
import utils.second
import kotlin.math.absoluteValue
import kotlin.test.Test

fun List<Int>.isSafe(): Boolean = windowed(2).all { (a, b) -> (b - a) in (1..3) } ||
        windowed(2).all { (a, b) -> (a - b) in (1..3) }


internal class `24day2` {
    private val fileName = "day2"
    private val testFileName = "${fileName}_test"

    private fun List<String>.convertToDataObjectList() = run {
        this.map { it.split(" ").map { it.toInt() } }
    }

    enum class LevelType {
        INCREASING, DECREASING
    }

    private fun part1Calculation(input: List<String>): Int {
        val converted = input.convertToDataObjectList()
        println(converted)

        val check = converted.map { currList ->
            var comp: Int? = null
            currList.windowed(2, 1) { currWindow ->
                println(currWindow)
                val currComp = currWindow.first().compareTo(currWindow.second())
                println(currComp)
                if (comp == null) {
                    comp = currComp
                }

                if (comp == 0) {
                    return@windowed false
                }

                if (currComp != comp) {
                    return@windowed false
                }

                if (currWindow.first().minus(currWindow.second()).absoluteValue !in 1..3) {
                    return@windowed false
                }
                true
            }.all { it }
        }

        return check.count { it }
    }

    private fun checkRecord(currList: List<Int>, canRemove: Boolean): Boolean {
        require(currList.size > 2)
        var i = 0
        var comp: Int? = null
        while (i < currList.size - 1) {
            val first = currList[i]
            val second = currList[i + 1]
            val currComp = first.compareTo(second)
            if (comp == null) {
                comp = currComp
            }

            if (comp == 0 || currComp != comp || first.minus(second).absoluteValue !in 1..3) {
                if (canRemove) {
                    var isRight = false
                    val newList1 = currList.toMutableList()
                    newList1.removeAt(i)
                    isRight = isRight || checkRecord(newList1, false)

                    val newList2 = currList.toMutableList()
                    newList2.removeAt(i + 1)
                    isRight = isRight || checkRecord(newList2, false)

                    if (i != 0) {
                        // It's possible the ordering can change by removing the first element.
                        val newList3 = currList.toMutableList()
                        newList3.removeAt(i - 1)
                        isRight = isRight || checkRecord(newList3, false)
                    }

                    return isRight
                }
                return false
            }

            i++
        }
        return true
    }

    fun checkRecord2(line: List<Int>): Boolean {
        return line.indices.asSequence()
            .map { bad -> line.filterIndexed { index, _ -> index != bad } }
            .any(List<Int>::isSafe)
    }

    private fun part2Calculation(input: List<String>): Int {
        val converted = input.convertToDataObjectList()
        println(converted)

        val check = converted.map { currList ->
            val mySolution = checkRecord(currList, true)
            val otherSolution = checkRecord2(currList)
            if (mySolution != otherSolution) {
                println("Diff found: $currList, mySolution: $mySolution, otherSolution: $otherSolution")
            }
            mySolution
        }

        println(check.joinToString { it.toString() })

        return check.count { it }
    }


    fun part2(input: List<List<Int>>) = input.count { line ->
        line.indices.asSequence()
            .map { bad -> line.filterIndexed { index, _ -> index != bad } }
            .any(List<Int>::isSafe)
    }

    @Test
    fun part1Test() {
        val input = readInput(testFileName)
        assertThat(part1Calculation(input)).isEqualTo(2)
    }

    @Test
    fun part1() {
        val input = readInput(fileName)
        assertThat(part1Calculation(input)).isEqualTo(421)
    }

    @Test
    fun part2Test() {
        val input = readInput(testFileName)
        assertThat(part2Calculation(input)).isEqualTo(4)
    }

    @Test
    fun part2Test2() {
        val input = readInput("day2_test2")
        assertThat(part2Calculation(input)).isEqualTo(1)
    }

    @Test
    fun part2() {
        val input = readInput(fileName)
        assertThat(part2Calculation(input)).isEqualTo(476)
    }
}