package aoc_2023

import com.kietyo.ktruth.assertThat
import utils.intersectRangeOrNull
import utils.length
import utils.println
import utils.splitByNewLine
import utils.subtractRange
import kotlin.math.min
import kotlin.test.Test

internal class `23day5` {
    private val fileName = "day5"
    private val testFileName = "${fileName}_test"

    private fun List<String>.convertToDataObjectList() = run {
        this
    }

    private data class SourceDestMap(
        val source: String,
        val dest: String,
        val sourceDestRanges: List<Pair<LongRange, LongRange>>
    ) {
        init {
            require(sourceDestRanges.all {
                it.first.length() == it.second.length()
            })
        }

        fun get(sourceNum: Long): Long {
            val rangeOrNull = sourceDestRanges.firstOrNull { sourceNum in it.first }
                ?: return sourceNum
            val idx = sourceNum - rangeOrNull.first.first
            return rangeOrNull.second.first + idx
        }

        fun getDestRanges(sourceRange: LongRange): List<LongRange> {
            var nonCustomRange = listOf(sourceRange)
            val customDestRanges = sourceDestRanges.mapNotNull {
                val intersectSourceRange = sourceRange.intersectRangeOrNull(it.first)
                if (intersectSourceRange == null) {
                    null
                } else {
                    nonCustomRange = nonCustomRange.flatMap {
                        it.subtractRange(intersectSourceRange)
                    }
                    val idx = intersectSourceRange.first - it.first.first
                    (it.second.first+idx)..(it.second.first + idx + intersectSourceRange.length() - 1)
                }
            }
            return nonCustomRange + customDestRanges
        }
    }

    private fun part1Calculation(input: List<String>): Long {
        val converted = input.convertToDataObjectList().splitByNewLine()
        println(converted)

        val seeds = converted[0].first().removePrefix("seeds: ").split(" ").map { it.toLong() }
        println("seeds: $seeds")

        val regex = "(\\w+)-to-(\\w+) map:".toRegex()

        val maps = converted.drop(1).map {
            val firstMap = it
            val firstLine = firstMap[0]
            val regexResult = regex.matchEntire(firstLine)

            val (sourceName, destName) = regexResult!!.destructured
            println(sourceName, destName)
            val ranges = firstMap.drop(1).map {
                val (destStart, sourceStart, range) = it.split(" ").map { it.toLong() }
                Pair(
                    sourceStart..<sourceStart + range,
                    destStart..<destStart + range
                )
            }.sortedBy { it.first.first }
            SourceDestMap(sourceName, destName, ranges)
        }
        kotlin.io.println(maps)

        seeds.first()
        val minLocation = seeds.minOf {
            var currNum = it
            var currSource = "seed"
            while (true) {
                val mapOrNull = maps.firstOrNull { it.source == currSource }
                if (mapOrNull == null) {
                    break
                }
                currNum = mapOrNull.get(currNum)
                currSource = mapOrNull.dest
            }
            println(currSource, currNum)
            currNum
        }
        kotlin.io.println(minLocation)
        return minLocation
    }

    private fun getMinOfRange(
        maps: List<SourceDestMap>,
        source: String,
        sourceRanges: List<LongRange>
    ) {
        val mapOrNull = maps.firstOrNull { it.source == source }
        if (mapOrNull == null) {
            TODO()
        }
    }

    private fun part2Calculation(input: List<String>): Long {
        val converted = input.convertToDataObjectList().splitByNewLine()
        println(converted)

        val seedRanges = converted[0].first().removePrefix("seeds: ").split(" ").map { it.toLong() }
            .windowed(2, step = 2).map { it.first()..<it.first() + it.last() }
        println("seedRanges: $seedRanges")

        val regex = "(\\w+)-to-(\\w+) map:".toRegex()

        val maps = converted.drop(1).map {
            val firstMap = it
            val firstLine = firstMap[0]
            val regexResult = regex.matchEntire(firstLine)

            val (sourceName, destName) = regexResult!!.destructured
            println(sourceName, destName)
            val ranges = firstMap.drop(1).map {
                val (destStart, sourceStart, range) = it.split(" ").map { it.toLong() }
                Pair(
                    sourceStart..<sourceStart + range,
                    destStart..<destStart + range
                )
            }.sortedBy { it.first.first }
            SourceDestMap(sourceName, destName, ranges)
        }
        kotlin.io.println(maps)

        var currRanges = seedRanges
        var currSource = "seed"

        while (true) {
            println("processing source: $currSource")
            val mapOrNull = maps.firstOrNull { it.source == currSource }
            if (mapOrNull == null) break
            currRanges = currRanges.flatMap {
                mapOrNull.getDestRanges(it)
            }
            currSource = mapOrNull.dest
        }

        println(currSource)
        println(currRanges)

        val minLocation = currRanges.minOf { it.first }
        println("minLocation: $minLocation")
        return minLocation
    }

    @Test
    fun getDestFromSourceNum() {
        val map = SourceDestMap("seed", "blah", listOf(50L..97L to 52L..99L))
        assertThat(map.get(50)).isEqualTo(52)
        assertThat(map.get(97)).isEqualTo(99)
        assertThat(map.get(49)).isEqualTo(49)
    }

    @Test
    fun part1Test() {
        val input = readInput(testFileName)
        assertThat(part1Calculation(input)).isEqualTo(35)
    }

    @Test
    fun part1() {
        val input = readInput(fileName)
        assertThat(part1Calculation(input)).isEqualTo(836040384)
    }

    @Test
    fun part2Test() {
        val input = readInput(testFileName)
        assertThat(part2Calculation(input)).isEqualTo(46)
    }

    @Test
    fun part2Test2() {
        val input = readInput("day5_test2")
        part2Calculation(input)
    }

    @Test
    fun part2() {
        val input = readInput(fileName)
        assertThat(part2Calculation(input)).isEqualTo(10834440)
    }


}

