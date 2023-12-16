package aoc_2023

import com.kietyo.ktruth.assertThat
import kotlin.test.Test

internal class `23day8` {
    private val fileName = "day8"
    private val testFileName = "${fileName}_test"

    private fun List<String>.convertToDataObjectList() = run {
        this
    }

    private fun part1Calculation(input: List<String>): Int {
        val converted = input.convertToDataObjectList()
        println(converted)

        val instructions = converted.first()

        val regex = Regex("(\\w+) = \\((\\w+), (\\w+)\\)")

        val map = converted.drop(2).map {
            val match = regex.matchEntire(it)
            val (source, left, right) = match!!.destructured
            source to (left to right)
        }.toMap()
        println(map)

        var idx = 0
        var currentNode = "AAA"
        while (true) {
            if (currentNode == "ZZZ") {
                break
            }
            val node = map[currentNode]!!
            val currInstruction = instructions.getCyclic(idx)
            when (currInstruction) {
                'L' -> currentNode = node.first
                'R' -> currentNode = node.second
                else -> TODO()
            }
            idx++
        }

        println(idx)
        return idx
    }

    private fun part2Calculation(input: List<String>): Long {
        val converted = input.convertToDataObjectList()
        println(converted)

        val instructions = converted.first()

        val regex = Regex("(\\w+) = \\((\\w+), (\\w+)\\)")

        val map = converted.drop(2).map {
            val match = regex.matchEntire(it)
            val (source, left, right) = match!!.destructured
            source to (left to right)
        }.toMap()
        println(map)

        val nodesThatEndWithA = map.keys.filter { it.endsWith("A") }
        println(nodesThatEndWithA)

        // 167011838 -17621> 167029459 -17621> 167047080

        var idx = 0L
        var currentNodes = nodesThatEndWithA
        val firstTimeSeen = BooleanArray(currentNodes.size) {false}
        val idxForFirstTimeSeen = LongArray(currentNodes.size)
        var counted2 = false
        var counted3 = false
        var counted4 = false
        var counted5 = false

        while (true) {
            currentNodes.forEachIndexed { index, s ->
                if (s.endsWith("Z") && !firstTimeSeen[index]) {
                    firstTimeSeen[index] = true
                    idxForFirstTimeSeen[index] = idx
                }
            }
            if (firstTimeSeen.all { it }) {
                println("idx: $idx")
                println(idxForFirstTimeSeen.toList())
                break
            }
            if (currentNodes.count { it.endsWith("Z") } == 2 && !counted2) {
                println("idx: $idx, 2 nodes seen: $currentNodes")
                counted2 = true
            }
            if (currentNodes.count { it.endsWith("Z") } == 3 && !counted3) {
                println("idx: $idx, 3 nodes seen: $currentNodes")
                counted3 = true
            }
            if (currentNodes.count { it.endsWith("Z") } == 4 && !counted4) {
                println("idx: $idx, 4 nodes seen: $currentNodes")
                counted4 = true
            }
            if (currentNodes.count { it.endsWith("Z") } == 5 && !counted5) {
                println("idx: $idx, 5 nodes seen: $currentNodes")
                counted5 = true
            }
            if (currentNodes.all { it.endsWith("Z") }) {
                break
            }
            val currInstruction = instructions.getCyclic(idx)
            currentNodes = currentNodes.map {
                val curr = map[it]!!
                when (currInstruction) {
                    'L' -> curr.first
                    'R' -> curr.second
                    else -> TODO()
                }
            }

            idx++
        }

        // [VBA, TVA, DVA, VPA, AAA, DTA]
        // [16043, 20777, 13939, 18673, 11309, 17621]

        var currSum = idxForFirstTimeSeen.min()
        var curr = idxForFirstTimeSeen.min()
        var numMatching = 1
        while (true) {
            currSum += curr
            val countMultiples = idxForFirstTimeSeen.count { currSum % it == 0L }
            if (countMultiples == idxForFirstTimeSeen.size) {
                break
            }
            if (countMultiples > numMatching) {
                numMatching = countMultiples
                curr = currSum
            }
        }
        println(currSum)
        return currSum
    }

    @Test
    fun part1Test() {
        val input = readInput(testFileName)
        assertThat(part1Calculation(input)).isEqualTo(2)
    }

    @Test
    fun part1Test2() {
        val input = readInput("day8_test2")
        assertThat(part1Calculation(input)).isEqualTo(6)
    }

    @Test
    fun part1() {
        val input = readInput(fileName)
        assertThat(part1Calculation(input)).isEqualTo(11309)
    }

    @Test
    fun part2Test3() {
        val input = readInput("day8_test3")
        assertThat(part2Calculation(input)).isEqualTo(6)
    }

    @Test
    fun part2() {
        val input = readInput(fileName)
        // 8339819458498168537, too high
        // 8339819458498168537
        assertThat(part2Calculation(input)).isEqualTo(13740108158591L)
    }
}

private fun String.getCyclic(idx: Int): Char {
    return get(idx % length)
}

private fun String.getCyclic(idx: Long): Char {
    return get((idx % length).toInt())
}