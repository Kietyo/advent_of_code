package aoc_2024

import com.kietyo.ktruth.assertThat
import utils.Direction
import utils.toGrid
import kotlin.test.Test

internal class `24day4` {
    private val fileName = "day4"
    private val testFileName = "${fileName}_test"

    private fun List<String>.convertToDataObjectList() = run {
        this.toGrid()
    }

    private fun part1Calculation(input: List<String>): Int {
        val converted = input.convertToDataObjectList()
        println(converted)

        val wordToFind = "XMAS"
        val directions = listOf(
            Direction.UP, Direction.UP_LEFT, Direction.UP_RIGHT,
            Direction.DOWN, Direction.DOWN_LEFT, Direction.DOWN_RIGHT,
            Direction.LEFT, Direction.RIGHT)

        for (i in wordToFind.indices) {
            println("i: $i")
        }

        var numFound = 0
        for (point in converted) {
            println(point)
            for (direction in directions) {
                var found = true
                for (i in wordToFind.indices) {
                    if (converted.getOrNull(point.x + direction.x * i, point.y + direction.y * i) != wordToFind[i]) {
                        found = false
                        break
                    }
                }
                if (found) {
                    numFound++
                }
            }
        }

        println("numFound: $numFound")

        return numFound
    }

    private fun part2Calculation(input: List<String>): Int {
        val converted = input.convertToDataObjectList()
        println(converted)

        val wordToFind = "MAS"
        val directions = listOf(
            Direction.UP, Direction.UP_LEFT, Direction.UP_RIGHT,
            Direction.DOWN, Direction.DOWN_LEFT, Direction.DOWN_RIGHT,
            Direction.LEFT, Direction.RIGHT)

        val leftRightDirections = listOf(Direction.UP_LEFT, Direction.DOWN_RIGHT)
        val rightLeftDirections = listOf(Direction.UP_RIGHT, Direction.DOWN_LEFT)

        for (i in wordToFind.indices) {
            println("i: $i")
        }

        var numFound = 0
        for (point in converted) {

            val findFn = fun (direction: Direction): Boolean {
                var found = true
                for (i in wordToFind.indices) {
                    if (converted.getOrNull(point.x + direction.x * (i - 1), point.y + direction.y * (i - 1)) != wordToFind[i]) {
                        found = false
                        break
                    }
                }
                return found
            }

            if ((findFn(Direction.DOWN_RIGHT) || findFn(Direction.UP_LEFT)) &&
                (findFn(Direction.UP_RIGHT) || findFn(Direction.DOWN_LEFT))) {
                numFound++
            }
        }

        println("numFound: $numFound")

        return numFound
    }

    @Test
    fun part1Test() {
        val input = readInput(testFileName)
        assertThat(part1Calculation(input)).isEqualTo(18)
    }

    @Test
    fun part1() {
        val input = readInput(fileName)
        assertThat(part1Calculation(input)).isEqualTo(2644)
    }

    @Test
    fun part2Test() {
        val input = readInput(testFileName)
        assertThat(part2Calculation(input)).isEqualTo(9)
    }

    @Test
    fun part2() {
        val input = readInput(fileName)
        assertThat(part2Calculation(input)).isEqualTo(0)
    }
}