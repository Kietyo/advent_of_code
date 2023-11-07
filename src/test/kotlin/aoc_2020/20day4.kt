package aoc_2020

import utils.hexColorRegex
import utils.splitByNewLine
import utils.splitIntStringPart
import kotlin.test.Test

internal class `20day4` {
    private val part1FileName = "day4"
    private val part1TestFileName = "${part1FileName}_test"

    private fun List<String>.convertToDataObjectList() = run {
        splitByNewLine().map {
            val passportData = mutableMapOf<String, String>()
            it.forEach { line ->
                line.split(" ").forEach { pair ->
                    val (first, second) = pair.split(":")
                    passportData[first] = second
                }
            }
            passportData as Map<String, String>
        }
    }

    private fun part1Calculation(input: List<String>) {
        val converted = input.convertToDataObjectList()
        println(converted)
        println(converted.count {
            it.containsKey("byr") &&
            it.containsKey("iyr") &&
            it.containsKey("eyr") &&
            it.containsKey("hgt") &&
            it.containsKey("hcl") &&
            it.containsKey("ecl") &&
            it.containsKey("pid")
        })
    }

    private fun Map<String, String>.predicateOrFalse(key: String, fn: (String) -> Boolean): Boolean {
        return get(key)?.let {
            fn(it)
        } ?: false
    }

    private fun part2Calculation(input: List<String>) {
        val converted = input.convertToDataObjectList()
        println(converted)
        val eyeColors = listOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth")
        println(converted.count { pp: Map<String, String> ->
            pp.predicateOrFalse("byr") {
                it.toInt() in 1920..2020
            } &&
            pp.predicateOrFalse("iyr") {
                it.toInt() in 2010..2020
            } &&
            pp.predicateOrFalse("eyr") {
                it.toInt() in 2020..2030
            } &&
            pp.predicateOrFalse("hgt") {
                val parts = it.splitIntStringPart()
                if (parts == null) {
                    false
                } else {
                    if (parts.second == "cm") {
                        parts.first in 150..193
                    } else if (parts.second == "in") {
                        parts.first in 59..76
                    } else {
                        TODO()
                    }
                }
            } &&
            pp.predicateOrFalse("hcl") {
                hexColorRegex.matchEntire(it) != null
            } &&
            pp.predicateOrFalse("ecl") {
                it in eyeColors
            } &&
            pp.predicateOrFalse("pid") {
                it.length == 9 && it.all { it.isDigit() }
            }
        })
    }

    @Test
    fun part1Test() {
        val input = readInput(part1TestFileName)
        part1Calculation(input)
    }

    @Test
    fun part1() {
        val input = readInput(part1FileName)
        part1Calculation(input)
    }

    @Test
    fun part2Test() {
        val input = readInput(part1TestFileName)
        part2Calculation(input)
    }

    @Test
    fun part2() {
        val input = readInput(part1FileName)
        part2Calculation(input)
    }
}

