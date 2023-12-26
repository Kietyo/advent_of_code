package aoc_2023

import com.kietyo.ktruth.assertThat
import kotlin.test.Test

internal class `23day15` {
    private val fileName = "day15"
    private val testFileName = "${fileName}_test"

    private fun List<String>.convertToDataObjectList() = run {
        this
    }

    fun String.HASH(): Int {
        var curr = 0
        for (c in this) {
            curr += c.code
            curr *= 17
            curr %= 256
        }
        return curr
    }

    private fun part1Calculation(input: List<String>): Int {
        val converted = input.convertToDataObjectList()
        println(converted)

        val lines = converted.first().split(",")
        println(lines)

        var sum = 0
        lines.forEach { line ->
            val curr = line.HASH()
            println("Processing $line, curr: $curr")
            sum += curr
        }

        println("sum: $sum")

        return sum
    }

    data class Item(val label: String, var focalLength: Int)

    private fun part2Calculation(input: List<String>): Int {
        val converted = input.convertToDataObjectList()
        println(converted)

        val lines = converted.first().split(",")
        println(lines)

        val hashmap = mutableMapOf<Int, MutableList<Item>>()
        repeat(256) {
            hashmap.put(it, mutableListOf())
        }

        lines.forEach { line ->
            if (line.contains("-")) {
                require(line.endsWith("-"))
                val label = line.dropLast(1)
                val hash = label.HASH()
                hashmap[hash]!!.removeIf {
                    it.label == label
                }
            } else {
                require(line.contains("="))
                val (label, focalLengthStr) = line.split("=")
                val focalLength = focalLengthStr.toInt()
                val hash = label.HASH()
                val items = hashmap[hash]!!
                val existingItem = items.firstOrNull {
                    it.label == label
                }
                if (existingItem == null) {
                    items.add(Item(label, focalLength))
                } else {
                    existingItem.focalLength = focalLength
                }
            }
        }

        var sum = 0
        for (entry in hashmap) {
            for ((i, item) in entry.value.withIndex()) {
                sum += (entry.key+1) * (i+1) * item.focalLength
            }
        }

        println(sum)

        return sum
    }

    @Test
    fun part1Test() {
        val input = readInput(testFileName)
        assertThat(part1Calculation(input)).isEqualTo(1320)
    }

    @Test
    fun part1() {
        val input = readInput(fileName)
        assertThat(part1Calculation(input)).isEqualTo(497373)
    }

    @Test
    fun part2Test() {
        val input = readInput(testFileName)
        assertThat(part2Calculation(input)).isEqualTo(145)
    }

    @Test
    fun part2() {
        val input = readInput(fileName)
        assertThat(part2Calculation(input)).isEqualTo(259356)
    }
}