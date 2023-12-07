package aoc_2023

import com.kietyo.ktruth.assertThat
import utils.splitIntStringPartsOrNull
import utils.splitStringIntPartsOrNull
import kotlin.math.max
import kotlin.test.Test

internal class `23day2` {
    private val fileName = "day2"
    private val testFileName = "${fileName}_test"

    private fun List<String>.convertToDataObjectList() = run {
        this
    }

    private fun part1Calculation(input: List<String>): Int {
        val converted = input.convertToDataObjectList()
        println(converted)

        val maxRed = 12
        val maxGreen = 13
        val maxBlue = 14

        val numsMatching = converted.sumOf {
            val split = it.split(": ", limit = 2)
            println(split)

            val (_, gameId) = split[0].splitStringIntPartsOrNull()!!
            split[1].split("; ").map {
                it.split(", ").forEach {
                    val (num, colorString) = it.splitIntStringPartsOrNull(trimStringPart = true)!!
                    val isAllowed = when (colorString) {
                        "red" -> num <= maxRed
                        "green" -> num <= maxGreen
                        "blue" -> num <= maxBlue
                        else -> TODO()
                    }
                    if (!isAllowed) {
                        return@sumOf 0
                    }
                }
            }
            gameId
        }

        return numsMatching
    }

    private fun part2Calculation(input: List<String>): Int {
        val converted = input.convertToDataObjectList()
        val power = converted.sumOf {
            val maxCubesNeeded = mutableMapOf("red" to 0, "green" to 0, "blue" to 0)
            val split = it.split(": ", limit = 2)
            println(split)

            val (_, gameId) = split[0].splitStringIntPartsOrNull()!!
            split[1].split("; ").map {
                it.split(", ").forEach {
                    val (num, colorString) = it.splitIntStringPartsOrNull(trimStringPart = true)!!
                    maxCubesNeeded[colorString] = max(maxCubesNeeded[colorString]!!, num)
                }
            }
            maxCubesNeeded["red"]!! * maxCubesNeeded["green"]!! * maxCubesNeeded["blue"]!!
        }
        return power
    }

    @Test
    fun part1Test() {
        val input = readInput(testFileName)
        assertThat(part1Calculation(input)).isEqualTo(8)
    }

    @Test
    fun part1() {
        val input = readInput(fileName)
        assertThat(part1Calculation(input)).isEqualTo(2716)
    }

    @Test
    fun part2Test() {
        val input = readInput(testFileName)
        assertThat(part2Calculation(input)).isEqualTo(2286)
    }

    @Test
    fun part2() {
        val input = readInput(fileName)
        assertThat(part2Calculation(input)).isEqualTo(72227)
    }
}