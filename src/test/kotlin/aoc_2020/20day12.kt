package aoc_2020

import utils.createRotationMatrix
import utils.splitStringIntPartsOrNull
import utils.toip
import kotlin.math.absoluteValue
import kotlin.test.Test

internal class `20day12` {
    private val fileName = "day12"
    private val testFileName = "${fileName}_test"

    private fun List<String>.convertToDataObjectList() = run {
        mapNotNull { it.splitStringIntPartsOrNull() }
    }

    private fun part1Calculation(input: List<String>) {
        val converted = input.convertToDataObjectList()
        println(converted)

        var currPoint = 0 toip 0
        var dir = 1 toip 0

        converted.forEach {
            when (it.first) {
                "F" -> currPoint = currPoint + (dir * it.second)
                "N" -> currPoint = currPoint + ((0 toip 1) * it.second)
                "S" -> currPoint = currPoint + ((0 toip -1) * it.second)
                "E" -> currPoint = currPoint + ((1 toip 0) * it.second)
                "W" -> currPoint = currPoint + ((-1 toip 0) * it.second)
                "R" -> dir = (createRotationMatrix(-it.second) * dir).toIntPoint()
                "L" -> dir = (createRotationMatrix(it.second) * dir).toIntPoint()
                else -> TODO()
            }
        }

        println(currPoint)
        println(currPoint.x.absoluteValue + currPoint.y.absoluteValue)
    }

    private fun part2Calculation(input: List<String>) {
        val converted = input.convertToDataObjectList()
        println(converted)

        var waypoint = 10 toip 1
        var currPoint = 0 toip 0

        converted.forEach {
            when (it.first) {
                "F" -> currPoint = currPoint + (waypoint * it.second)
                "N" -> waypoint = waypoint + ((0 toip 1) * it.second)
                "S" -> waypoint = waypoint + ((0 toip -1) * it.second)
                "E" -> waypoint = waypoint + ((1 toip 0) * it.second)
                "W" -> waypoint = waypoint + ((-1 toip 0) * it.second)
                "L" -> waypoint = (createRotationMatrix(it.second) * waypoint).toIntPoint()
                "R" -> waypoint = (createRotationMatrix(-it.second) * waypoint).toIntPoint()
                else -> TODO()
            }
        }

        println(currPoint)
        println(currPoint.x.absoluteValue + currPoint.y.absoluteValue)
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

        // 21300, too high
    }
}