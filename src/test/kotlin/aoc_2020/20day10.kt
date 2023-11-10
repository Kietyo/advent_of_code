package aoc_2020

import kotlin.test.Test

internal class `20day10` {
    private val fileName = "day10"
    private val testFileName = "${fileName}_test"

    private fun List<String>.convertToDataObjectList() = run {
        map { it.toInt() }
    }

    private fun part1Calculation(input: List<String>) {
        val converted = input.convertToDataObjectList()
        println(converted)
        val myDeviceJoltage = converted.max() + 3

        val joltages = (converted + myDeviceJoltage + 0).sorted()
        println(joltages)
        println(joltages.size)

        val count = joltages.sorted().windowed(2) {
            it[1] - it[0]
        }.groupBy {
            it
        }.mapValues { it.value.size }

        println(count[1]!! * count[3]!!)
    }

    data class Context(val joltages: List<Int>)

    class OptSolver(val context: Context) {
        val cache: MutableMap<Int, Long> = mutableMapOf()
        fun opt(currentJoltage: Int): Long {
            if (currentJoltage !in cache) cache[currentJoltage] = optInternal(currentJoltage)
            return cache[currentJoltage]!!
        }

        fun optInternal(currentJoltage: Int): Long {
            if (currentJoltage in cache) return cache[currentJoltage]!!

            if (currentJoltage == context.joltages.max()) return 1L

            val nextJoltages = context.joltages.asSequence().dropWhile { it <= currentJoltage }.takeWhile { (it - currentJoltage) <= 3}.toList()
            if (nextJoltages.isEmpty()) return 0L
            return nextJoltages.sumOf {
                opt(it)
            }
        }
    }



    private fun part2Calculation(input: List<String>) {
        val converted = input.convertToDataObjectList()
        println(converted)
        val myDeviceJoltage = converted.max() + 3

        val joltages = (converted + myDeviceJoltage + 0).sorted()

        println( OptSolver(Context(joltages)).opt(0) )
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
    fun part2Test2() {
        val input = readInput("day10_test2")
        part2Calculation(input)
    }

    @Test
    fun part2() {
        val input = readInput(fileName)
        part2Calculation(input)
    }
}