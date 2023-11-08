package aoc_2020

import utils.sumOf
import kotlin.test.Test

internal class `20day7` {
    private val fileName = "day7"
    private val testFileName = "${fileName}_test"

    private data class Rule(val main: String, val parts: Map<String, Int>)

    private fun List<String>.convertToDataObjectList() = run {
        map {
            val (main, parts) = it.dropLast(1).split(" contain ")
            val main2 = main.dropLastWhile { it == 's' }
            if (parts == "no other bags") {
                return@map Rule(main2, emptyMap())
            }
            val parts2 = parts.split(", ").map {
                println(it)
                val (count, part) = it.dropLastWhile { it == 's' }.split(" ", limit = 2)
                part to count.toInt()
            }.toMap()
            println(parts2)
            Rule(main2, parts2)
        }
    }

    private val SHINY_GOLD_BAG = "shiny gold bag"

    private fun List<Rule>.containsShinyGoldBag(curr: String): Boolean {
        val rule = filter { it.main == curr }.run {
            require(size == 1)
            this
        }.first()
        if (rule.parts.isEmpty()) return false

        val containsShinyGold = rule.parts.any { it.key == SHINY_GOLD_BAG || containsShinyGoldBag(it.key) }
        return containsShinyGold
    }

    private fun List<Rule>.countBags(curr: String): Int {
        val rule = filter { it.main == curr }.run {
            require(size == 1)
            this
        }.first()
        if (rule.parts.isEmpty()) return 0

        return rule.parts.sumOf {
            it.value + it.value * countBags(it.key)
        }
    }

    private fun part1Calculation(input: List<String>) {
        val converted = input.convertToDataObjectList()
        println(converted)
        println(converted.count {
            converted.containsShinyGoldBag(it.main)
        })
    }

    private fun part2Calculation(input: List<String>) {
        val converted = input.convertToDataObjectList()
        println(converted)
        println(converted.countBags(SHINY_GOLD_BAG))
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
        val input = readInput("day7_test2")
        part2Calculation(input)
    }

    @Test
    fun part2() {
        val input = readInput(fileName)
        part2Calculation(input)
    }
}

