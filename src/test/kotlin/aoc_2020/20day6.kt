package aoc_2020

import utils.splitByNewLine
import kotlin.test.Test

internal class `20day6` {
    private val fileName = "day6"
    private val testFileName = "${fileName}_test"

    private fun List<String>.convertToDataObjectList() = run {
        this
    }

    private fun part1Calculation(input: List<String>) {
        val converted = input.convertToDataObjectList().splitByNewLine()
        println(converted)
        println(converted.sumOf {
            val questionsAnswered = it.flatMap { it.toList() }.toSet()
            questionsAnswered.size
        })
    }

    private fun part2Calculation(input: List<String>) {
        val converted = input.convertToDataObjectList().splitByNewLine()
        println(converted)
        println(converted.sumOf {
            val questionsAnswered = it.fold(('a'..'z').toSet()) { acc, s ->
                acc.intersect(s.toSet())
            }
            questionsAnswered.size
        })

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