package aoc_2024

import com.kietyo.ktruth.assertThat
import kotlin.math.absoluteValue
import kotlin.test.Test

internal class `24day1` {
    private val fileName = "day1"
    private val testFileName = "${fileName}_test"

    private fun List<String>.convertToDataObjectList() = run {
        val list1 = mutableListOf<Long>()
        val list2 = mutableListOf<Long>()

        forEach {
            val (l, r) = it.split("   ")
            println("$l $r")
            list1.add(l.toLong())
            list2.add(r.toLong())
        }

        list1.sort()
        list2.sort()

        list1 to list2
    }

    private fun part1Calculation(input: List<String>): Long {
        val converted = input.convertToDataObjectList()
        println(converted)

        val len = converted.first.size

        var sum = 0L
        repeat(len) {
            sum += (converted.first[it] - converted.second[it]).absoluteValue
        }

        println(sum)

        return sum
    }

    private fun part2Calculation(input: List<String>): Long {
        val converted = input.convertToDataObjectList()
        println(converted)


        val occurrenceMap = converted.second.groupingBy { it }.eachCount()

        var sum = 0L
        converted.first.forEach {
            sum += it * occurrenceMap.getOrDefault(it, 0).toLong()
        }

        return sum
    }

    @Test
    fun part1Test() {
        val input = readInput(testFileName)
        assertThat(part1Calculation(input)).isEqualTo(11)
    }

    @Test
    fun part1() {
        val input = readInput(fileName)
        assertThat(part1Calculation(input)).isEqualTo(1223326)
    }

    @Test
    fun part2Test() {
        val input = readInput(testFileName)
        assertThat(part2Calculation(input)).isEqualTo(31)
    }



    @Test
    fun part2() {
        val input = readInput(fileName)
        assertThat(part2Calculation(input)).isEqualTo(21070419)
    }
}