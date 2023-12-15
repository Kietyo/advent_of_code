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
        },
        val bestHandTypeIfJokerExists: HandType = run {
            val hasJoker = cards.contains('J')
            val cardToNum = cards.groupBy { it }.mapValues { it.value.size }
            val jokerIsMaxCount = cardToNum.getOrDefault('J', 0) == cardToNum.maxOf { it.value }
            println(cardToNum)
            if (hasJoker) {
                if (jokerIsMaxCount) {
                    when {
                        cardToNum.size == 1 -> HandType.FIVE_OF_A_KIND
                        cardToNum.size == 2 && cardToNum.values.toSet() == setOf(1, 4)-> HandType.FIVE_OF_A_KIND
                        cardToNum.size == 2 && cardToNum.values.toSet() == setOf(2, 3) -> HandType.FIVE_OF_A_KIND
                        cardToNum.size == 3 && cardToNum.values.toSet() == setOf(1, 3) -> HandType.FOUR_OF_A_KIND
                        cardToNum.size == 3 && cardToNum.values.toSet() == setOf(1, 2) -> HandType.FOUR_OF_A_KIND
                        cardToNum.size == 4 && cardToNum.values.toSet() == setOf(1, 2) -> HandType.THREE_OF_A_KIND
                        else -> HandType.ONE_PAIR
                    }
                } else {
                    when {
                        cardToNum.size == 1 -> HandType.FIVE_OF_A_KIND
                        cardToNum.size == 2 && cardToNum.values.toSet() == setOf(1, 4) -> HandType.FIVE_OF_A_KIND
                        cardToNum.size == 2 && cardToNum.values.toSet() == setOf(2, 3) -> HandType.FIVE_OF_A_KIND
                        cardToNum.size == 3 && cardToNum.values.toSet() == setOf(1, 3) -> HandType.FOUR_OF_A_KIND
                        cardToNum.size == 3 && cardToNum.values.toSet() == setOf(1, 2) -> HandType.FULL_HOUSE
                        cardToNum.size == 4 && cardToNum.values.toSet() == setOf(1, 2) -> HandType.THREE_OF_A_KIND
                        else -> HandType.ONE_PAIR
                    }
                }

            } else {
                when {
                    cardToNum.size == 1 -> HandType.FIVE_OF_A_KIND
                    cardToNum.size == 2 && cardToNum.values.toSet() == setOf(1, 4)-> HandType.FOUR_OF_A_KIND
                    cardToNum.size == 2 && cardToNum.values.toSet() == setOf(2, 3) -> HandType.FULL_HOUSE
                    cardToNum.size == 3 && cardToNum.values.toSet() == setOf(1, 3) -> HandType.THREE_OF_A_KIND
                    cardToNum.size == 3 && cardToNum.values.toSet() == setOf(1, 2) -> HandType.TWO_PAIR
                    cardToNum.size == 4 && cardToNum.values.toSet() == setOf(1, 2) -> HandType.ONE_PAIR
                    else -> HandType.HIGH_CARD
                }
            }
        }
    ) {

    }

    private fun part1Calculation(input: List<String>): Int {
        val ranking = "23456789TJQKA"

        val converted = input.convertToDataObjectList().map {
            val (hand, bid) = it.split(" ")
            Hand(hand.toList()) to bid.toInt()
        }
        println(converted)


        val sorted = converted.sortedWith { a, b ->
            if (a.first.handType == b.first.handType) {
                a.first.cards.zip(b.first.cards).forEach {
                    if (it.first != it.second) {
                        return@sortedWith ranking.indexOf(it.first).compareTo(ranking.indexOf(it.second))
                    }
                }
                0
            } else {
                a.first.handType.ordinal.compareTo(b.first.handType.ordinal)
            }
        }

        println(sorted)

        return sorted.foldIndexed(0) { index, acc, pair ->
            acc + (index + 1) * pair.second
        }
    }

    private fun part2Calculation(input: List<String>): Int {
        val ranking = "J23456789TQKA"
        val converted = input.convertToDataObjectList().map {
            val (hand, bid) = it.split(" ")
            Hand(hand.toList()) to bid.toInt()
        }
        println(converted)

        val sorted = converted.sortedWith { a, b ->
            if (a.first.bestHandTypeIfJokerExists == b.first.bestHandTypeIfJokerExists) {
                a.first.cards.zip(b.first.cards).forEach {
                    if (it.first != it.second) {
                        return@sortedWith ranking.indexOf(it.first).compareTo(ranking.indexOf(it.second))
                    }
                }
                0
            } else {
                a.first.bestHandTypeIfJokerExists.ordinal.compareTo(b.first.bestHandTypeIfJokerExists.ordinal)
            }
        }

        println(sorted)
        val res = sorted.foldIndexed(0) { index, acc, pair ->
            acc + (index + 1) * pair.second
        }

        return res
    }

    @Test
    fun part1Test() {
        val input = readInput(testFileName)
        assertThat(part1Calculation(input)).isEqualTo(6440)
    }

    @Test
    fun part1Test2() {
        val input = readInput("day7_test2")
        assertThat(part1Calculation(input)).isEqualTo(6105)
    }

    @Test
    fun part1() {
        val input = readInput(fileName)
        assertThat(part1Calculation(input)).isEqualTo(241344943)
    }

    @Test
    fun part2Test() {
        val input = readInput(testFileName)
        assertThat(part2Calculation(input)).isEqualTo(5905)
    }

    @Test
    fun part2() {
        val input = readInput(fileName)
        assertThat(part2Calculation(input)).isEqualTo(243101568)
    }
}