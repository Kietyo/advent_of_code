package aoc_2023

import com.kietyo.ktruth.assertThat
import kotlin.math.sqrt
import kotlin.test.Test

internal class `23day6` {
    private val fileName = "day6"
    private val testFileName = "${fileName}_test"

    private fun List<String>.convertToDataObjectList() = run {
        this
    }

    private fun part1Calculation(input: List<String>): Int {
        val converted = input.convertToDataObjectList()
        println(converted)

        val times = converted[0].removePrefix("Time:").split(" ").mapNotNull { it.toIntOrNull() }
        val distances = converted[1].removePrefix("Distance:").split(" ").mapNotNull { it.toIntOrNull() }

        println(times)
        println(distances)

        val numWaysToBeatRecord = mutableListOf<Int>()
        for (i in times.indices) {
            val time = times[i]
            val distance = distances[i]
            println("current. time: $time, distance: $distance")
            var firstTime = 0
            for (j in 1..<time) {
                val remainingTime = time - j
                val velocity = j
                val distanceInRemainingTime = remainingTime * velocity
                println("j: $j, remainingTime: $remainingTime, velocity: $velocity, distanceInRemainingTime: $distanceInRemainingTime")
                if (distanceInRemainingTime > distance) {
                    firstTime = j
                    break
                }
            }
            var lastTime = 0
            for (j in time-1 downTo 1) {
                val remainingTime = time - j
                val velocity = j
                val distanceInRemainingTime = remainingTime * velocity
                println("j: $j, remainingTime: $remainingTime, velocity: $velocity, distanceInRemainingTime: $distanceInRemainingTime")
                if (distanceInRemainingTime > distance) {
                    lastTime = j
                    break
                }
            }

            numWaysToBeatRecord.add(lastTime - firstTime + 1)
        }
        println(numWaysToBeatRecord)
        val res = numWaysToBeatRecord.fold(1) { acc, i -> acc * i }
        println(res)
        return res
    }

    private fun part2Calculation(input: List<String>): Long {
        val converted = input.convertToDataObjectList()
        println(converted)

        val time = converted[0].removePrefix("Time:").replace(" ", "").toLong()
        val distance = converted[1].removePrefix("Distance:").replace(" ", "").toLong()

        println(time)
        println(distance)

//        var firstTime = 0L
//        for (j in 1..<time) {
//            val remainingTime = time - j
//            val velocity = j
//            val distanceInRemainingTime = remainingTime * velocity
////            println("j: $j, remainingTime: $remainingTime, velocity: $velocity, distanceInRemainingTime: $distanceInRemainingTime")
//            if (distanceInRemainingTime > distance) {
//                firstTime = j
//                break
//            }
//        }
//        var lastTime = 0L
//        for (j in time-1 downTo 1) {
//            val remainingTime = time - j
//            val velocity = j
//            val distanceInRemainingTime = remainingTime * velocity
////            println("j: $j, remainingTime: $remainingTime, velocity: $velocity, distanceInRemainingTime: $distanceInRemainingTime")
//            if (distanceInRemainingTime > distance) {
//                lastTime = j
//                break
//            }
//        }
//
//        println("firstTime: $firstTime")
//        println("lastTime: $lastTime")
//        println(lastTime - firstTime + 1)

        // Method 2: Quadratic formula
        val first2 = ((time - sqrt(time * time - 4.0 * distance))
                / 2).toLong() + 1
        val last2 = ((time + sqrt(time * time - 4.0 * distance))
                / 2).toLong()
        println(
            last2 - first2 + 1
        )
        return last2 - first2 + 1
    }

    @Test
    fun part1Test() {
        val input = readInput(testFileName)
        assertThat(part1Calculation(input)).isEqualTo(288)
    }

    @Test
    fun part1() {
        val input = readInput(fileName)
        assertThat(part1Calculation(input)).isEqualTo(316800)
    }

    @Test
    fun part2Test() {
        val input = readInput(testFileName)
        assertThat(part2Calculation(input)).isEqualTo(71503)
    }

    @Test
    fun part2() {
        val input = readInput(fileName)
        part2Calculation(input)
        assertThat(part2Calculation(input)).isEqualTo(45647654)
    }
}