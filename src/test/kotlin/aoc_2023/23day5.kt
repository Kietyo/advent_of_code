package aoc_2023

import com.kietyo.ktruth.assertThat
import utils.println
import utils.splitByNewLine
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

                    it.second.first..(it.second.first + intersectSourceRange.length() - 1)
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

    private fun part2Calculation(input: List<String>) {
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

        // cache of seed range to min location value found
        val cache = mutableListOf<Pair<LongRange, Long>>()

        val firstSeedRange = seedRanges.first()
        var currSource = "seed"
        val mapOrNull = maps.firstOrNull { it.source == currSource }!!
        val destRanges = mapOrNull.getDestRanges(firstSeedRange)
        println(destRanges)

//        val minLocation = seeds.minOf {
//            var minLocation = Long.MAX_VALUE
//            for (seed in it) {
//                val cacheResult = cache.firstOrNull { seed in it.first }
//                if (cacheResult != null) {
//                    minLocation = min(cacheResult.second, minLocation)
//                    continue
//                }
//                //                kotlin.io.println("processing seed: $seed")
//                var currNum = seed
//                var currSource = "seed"
//                while (true) {
//                    val mapOrNull = maps.firstOrNull { it.source == currSource }
//                        ?: break
//                    currNum = mapOrNull.get(currNum)
//                    currSource = mapOrNull.dest
//                }
//                //                println(currSource, currNum)
//                minLocation = min(currNum, minLocation)
//            }
//            cache.add(it to minLocation)
//            minLocation
//        }
//        kotlin.io.println(minLocation)
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
        part2Calculation(input)
    }

    @Test
    fun part2Test2() {
        val input = readInput("day5_test2")
        part2Calculation(input)
    }

    @Test
    fun part2() {
        val input = readInput(fileName)
        part2Calculation(input)
    }

    @Test
    fun longIntersectsWith() {
        assertThat((0L..5L).intersectsWith(5L..7L)).isTrue()
        assertThat((0L..5L).intersectsWith(4L..7L)).isTrue()
        assertThat((0L..5L).intersectsWith(6L..7L)).isFalse()

        assertThat((5L..7L).intersectsWith(0L..5L)).isTrue()
        assertThat((4L..7L).intersectsWith(0L..5L)).isTrue()
        assertThat((6L..7L).intersectsWith(0L..5L)).isFalse()
    }

    @Test
    fun longIntersectRangeOrNull() {
        assertThat((0L..5L).intersectRangeOrNull(5L..7L)).isEqualTo(5L..5L)
        assertThat((0L..5L).intersectRangeOrNull(4L..7L)).isEqualTo(4L..5L)
        assertThat((0L..5L).intersectRangeOrNull(6L..7L)).isNull()

        assertThat((5L..7L).intersectRangeOrNull(0L..5L)).isEqualTo(5L..5L)
        assertThat((4L..7L).intersectRangeOrNull(0L..5L)).isEqualTo(4L..5L)
        assertThat((6L..7L).intersectRangeOrNull(0L..5L)).isNull()
    }

    @Test
    fun longRangeSubtractRange() {
        assertThat((10L..40L).subtractRange(50L..60L)).isEqualTo(listOf(
            10L..40L
        ))
        assertThat((10L..40L).subtractRange(20L..30L)).isEqualTo(listOf(
            10L..19L, 31L..40L
        ))
        assertThat((10L..40L).subtractRange(10L..30L)).isEqualTo(listOf(
            31L..40L
        ))
        assertThat((10L..40L).subtractRange(30L..40L)).isEqualTo(listOf(
            10L..29L
        ))

        assertThat((10L..40L).subtractRange(40L..40L)).isEqualTo(listOf(
            10L..39L
        ))
        assertThat((10L..40L).subtractRange(10L..10L)).isEqualTo(listOf(
            11L..40L
        ))
        assertThat((10L..40L).subtractRange(30L..30L)).isEqualTo(listOf(
            10L..29L, 31L..40L
        ))

        assertThat((10L..40L).subtractRange(10L..40L)).isEmpty()
        assertThat((10L..40L).subtractRange(0L..50L)).isEmpty()
    }
}

private fun LongRange.subtractRange(other: LongRange): List<LongRange> {
    val intersectRangeOrNull = intersectRangeOrNull(other)
        ?: return listOf(this)
    if (this.length() == intersectRangeOrNull.length()) return emptyList()
    if (intersectRangeOrNull.first > this.first && intersectRangeOrNull.last < this.last) {
        return listOf(
            this.first..<intersectRangeOrNull.first,
            intersectRangeOrNull.last+1..this.last
        )
    } else if (this.first == intersectRangeOrNull.first) {
        return listOf(intersectRangeOrNull.last+1..this.last)
    } else if (this.last == intersectRangeOrNull.last) {
        return listOf(this.first..<intersectRangeOrNull.first)
    }
    TODO()
}

private fun LongRange.length() = last - first + 1

private fun LongRange.intersectsWith(other: LongRange): Boolean {
    val firstRange = if (first <= other.first) this else other
    val secondRange = if (first <= other.first) other else this
    return secondRange.first in firstRange
}

private fun LongRange.intersectRangeOrNull(other: LongRange): LongRange? {
    if (this.intersectsWith(other)) {
        val firstRange = if (first <= other.first) this else other
        val secondRange = if (first <= other.first) other else this
        return secondRange.first..min(firstRange.last, secondRange.last)
    }
    return null
}
