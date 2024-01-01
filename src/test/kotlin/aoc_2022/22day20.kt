package aoc_2022

import com.kietyo.ktruth.assertThat
import java.lang.Exception
import kotlin.math.absoluteValue
import kotlin.math.sign
import kotlin.test.Test

internal class `22day20` {
    private val fileName = "day1"
    private val testFileName = "${fileName}_test"

    data class CircularArrayElement(
        var idx: Int,
        val data: Long
    )

    @JvmInline
    value class CircularArray(
        val data: Array<CircularArrayElement>
    ) {
        private fun normalizeIndex(idx: Int): Int {
            val mod = idx % data.size
            return if (mod < 0) mod + data.size else mod
        }

        operator fun get(idx: Int) = data[normalizeIndex(idx)]

        fun swap(idx1: Int, idx2: Int) {
            val circularIdx1 = normalizeIndex(idx1)
            val circularIdx2 = normalizeIndex(idx2)
            try {
                val t = data[circularIdx1]

                data[circularIdx1] = data[circularIdx2]
                data[circularIdx1].idx = circularIdx1

                data[circularIdx2] = t
                t.idx = circularIdx2
            } catch (e: Exception) {
                println("Failed with: idx1: $idx1, idx2: $idx2")
                throw e
            }

        }
    }


    private fun List<String>.convertToDataObjectList() = run {
        this
    }

    private fun part1Calculation(input: List<String>): Int {
        val converted = input.convertToDataObjectList()
        println(converted)

        val originalNums = input.map { it.toLong() }
        val originalNumsCircularArrayElements = originalNums.mapIndexed { index, i ->
            CircularArrayElement(index, i)
        }
        val circularArray = CircularArray(originalNumsCircularArrayElements.toTypedArray())
        println("size: ${originalNums.size}")
        println(circularArray)

        for (circularArrayElement in originalNumsCircularArrayElements) {
            val sign = circularArrayElement.data.sign
            var idx = circularArrayElement.idx

            //            println("$it: processing: $currNum, sign: $sign")

            repeat((circularArrayElement.data.absoluteValue % (originalNums.size * originalNums.size)).toInt()) {
                circularArray.swap(idx, idx + sign)
                idx += sign
            }
        }

        println(circularArray)

        val zeroIdx = originalNumsCircularArrayElements.first {
            it.data == 0L
        }.idx

        val nums = listOf(
            circularArray[zeroIdx + 1000],
            circularArray[zeroIdx + 2000],
            circularArray[zeroIdx + 3000],
        ).map { it.data }

        println(nums)
        println(nums.sum())

        return 0
    }

    private fun part2Calculation(input: List<String>): Int {
        val converted = input.convertToDataObjectList()
        println(converted)

        val originalNums = input.map { it.toLong() * 811589153L }
        val originalNumsCircularArrayElements = originalNums.mapIndexed { index, i ->
            CircularArrayElement(index, i)
        }
        val circularArray = CircularArray(originalNumsCircularArrayElements.toTypedArray())
        println("size: ${originalNums.size}")
        println(circularArray)

        repeat(10) {
            println("Finished $it")
            for (circularArrayElement in originalNumsCircularArrayElements) {
                val sign = circularArrayElement.data.sign
                var idx = circularArrayElement.idx

                //            println("$it: processing: $currNum, sign: $sign")

                repeat((circularArrayElement.data.absoluteValue %
                        (originalNums.size.dec() * originalNums.size)).toInt()) {
                    circularArray.swap(idx, idx + sign)
                    idx += sign
                }
            }
        }


        println(circularArray)

        val zeroIdx = originalNumsCircularArrayElements.first {
            it.data == 0L
        }.idx

        val nums = listOf(
            circularArray[zeroIdx + 1000],
            circularArray[zeroIdx + 2000],
            circularArray[zeroIdx + 3000],
        ).map { it.data }

        println(nums)
        println(nums.sum())

        return 0
    }

    @Test
    fun part1Test() {
        val input = readInput(testFileName)
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