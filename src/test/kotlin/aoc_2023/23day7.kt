package aoc_2023

import com.kietyo.ktruth.assertThat
import kotlin.test.Test

internal class `23day7` {
    private val fileName = "day7"
    private val testFileName = "${fileName}_test"

    private fun List<String>.convertToDataObjectList() = run {
        this
    }

    enum class HandType {
        HIGH_CARD,
        ONE_PAIR,
        TWO_PAIR,
        THREE_OF_A_KIND,
        FULL_HOUSE,
        FOUR_OF_A_KIND,
        FIVE_OF_A_KIND,
    }

    data class Hand(
        val cards: List<Char>,
        val handType: HandType = run {
            val cardToNum = cards.groupBy { it }.mapValues { it.value.size }
            println(cardToNum)
            when {
                cardToNum.size == 1 -> HandType.FIVE_OF_A_KIND
                cardToNum.size == 2 && cardToNum.values.toSet() == setOf(1, 4) -> HandType.FOUR_OF_A_KIND
                cardToNum.size == 2 && cardToNum.values.toSet() == setOf(2, 3) -> HandType.FULL_HOUSE
                cardToNum.size == 3 && cardToNum.values.toSet() == setOf(1, 3) -> HandType.THREE_OF_A_KIND
                cardToNum.size == 3 && cardToNum.values.toSet() == setOf(1, 2) -> HandType.TWO_PAIR
                cardToNum.size == 4 && cardToNum.values.toSet() == setOf(1, 2) -> HandType.ONE_PAIR
                else -> HandType.HIGH_CARD
            }
        }
    ) {

    }

    private fun part1Calculation(input: List<String>): Int {
        val converted = input.convertToDataObjectList().map {
            val (hand, bid) = it.split(" ")
            Hand(hand.toList()) to bid.toInt()
        }
        println(converted)


        return 0
    }

    private fun part2Calculation(input: List<String>): Int {
        val converted = input.convertToDataObjectList()
        println(converted)

        return 0
    }

    @Test
    fun part1Test() {
        val input = readInput(testFileName)
        assertThat(part1Calculation(input)).isEqualTo(0)
    }

    @Test
    fun part1Test2() {
        val input = readInput("day7_test2")
        assertThat(part1Calculation(input)).isEqualTo(0)
    }

    @Test
    fun part1() {
        val input = readInput(fileName)
        assertThat(part1Calculation(input)).isEqualTo(0)
    }

    @Test
    fun part2Test() {
        val input = readInput(testFileName)
        assertThat(part2Calculation(input)).isEqualTo(0)
    }

    @Test
    fun part2() {
        val input = readInput(fileName)
        assertThat(part2Calculation(input)).isEqualTo(0)
    }
}