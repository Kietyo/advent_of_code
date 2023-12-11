package utils

import com.sun.org.apache.xpath.internal.operations.Bool
import kotlin.experimental.ExperimentalTypeInference
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.round
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.time.measureTime

infix fun Int.ind(v: String) = IndexedValue(this, v)


fun String.splitByPredicate(predicate: (Char) -> Boolean) = splitByPredicateIndexed(predicate).map {
    it.value
}

fun String.splitByPredicateIndexed(predicate: (Char) -> Boolean): List<IndexedValue<String>> {
    val splitData = mutableListOf<IndexedValue<String>>()

    val runningSb = StringBuilder()
    var currWordI = 0
    var currI = 0
    val itr = this.iterator()
    while (itr.hasNext()) {
        val currChar = itr.nextChar()
        if (predicate(currChar)) {
            if (runningSb.isNotEmpty()) {
                splitData.add(IndexedValue(currWordI, runningSb.toString()))
            }
            splitData.add(IndexedValue(currI, currChar.toString()))
            runningSb.clear()
            currWordI = currI+1
        } else {
            runningSb.append(currChar)
        }
        currI++
    }
    if (runningSb.isNotEmpty()) {
        splitData.add(IndexedValue(currWordI, runningSb.toString()))
    }
    return splitData
}

fun List<String>.toIntList() = map { it.toInt() }

infix fun Int.toip(y: Int) = MutableIntPoint(this to y)

// Assumes the current number is in terms of degrees.
fun Number.toRadians(): Double = toDouble() * PI / 180.0

fun createRotationMatrix(degrees: Number): Matrix {
    val radians = degrees.toRadians()
    return Matrix(listOf(
        listOf(cos(radians), -sin(radians)),
        listOf(sin(radians), cos(radians))
    ))
}

data class Matrix(
    val data: List<List<Number>>
) {
    init {
        if (data.isNotEmpty()) {
            val size = data.first().size
            require(data.all { it.size == size })
        }
    }

    val rows = data.size
    val columns = data.firstOrNull()?.size ?: 0

    fun toIntPoint(): MutableIntPoint {
        require(rows == 2)
        require(columns == 1)
        return MutableIntPoint(data[0][0].toDouble().roundToInt(), data[1][0].toDouble().roundToInt())
    }

    operator fun times(other: IntPoint): Matrix {
        return Matrix(
            listOf(
                listOf(data[0].let {
                    it[0].toDouble() * other.x + it[1].toDouble() * other.y
                }),
                listOf(data[1].let {
                    it[0].toDouble() * other.x + it[1].toDouble() * other.y
                }),
            )
        )
    }
}

fun List<String>.toGrid(): MutableGrid<Char> {
    return MutableGrid(this.map { it.toCharArray().toTypedArray() })
}

enum class Direction(
    val movementOffset: IntPoint,
) {
    RIGHT(1 toip 0),
    LEFT(-1 toip 0),
    DOWN(0 toip 1),
    UP(0 toip -1),
    UP_LEFT(-1 toip -1),
    UP_RIGHT(1 toip -1),
    DOWN_LEFT(-1 toip 1),
    DOWN_RIGHT(1 toip 1);

    val x: Int get() = movementOffset.x
    val y: Int get() = movementOffset.y

    fun getNextDirectionClockwise(): Direction {
        return Direction.values().getCyclic(ordinal + 1)
    }

    fun getNextDirectionCounterClockwise(): Direction {
        return Direction.values().getCyclic(ordinal - 1)
    }
}

fun normalizeIndex(idx: Int, size: Int): Int {
    val mod = idx % size
    return if (mod < 0) mod + size else mod
}

fun <T> Array<T>.getCyclic(idx: Int): T {
    return get(normalizeIndex(idx, size))
}

fun <T> List<T>.getCyclic(idx: Int): T {
    return get(normalizeIndex(idx, size))
}
fun println(vararg eles: Any?) {
    kotlin.io.println(eles.joinToString { it.toString() })
}

val intStringRegex = Regex("(\\d+)(\\D+)")
fun String.splitIntStringPartsOrNull(trimStringPart: Boolean = false): Pair<Int, String>? {
    val matchResult = intStringRegex.matchEntire(this)
        ?: return null
    val (_, intPart, stringPart) = matchResult.groups.map { it!!.value }
    return intPart.toInt() to if (trimStringPart) stringPart.trim() else stringPart
}

val stringIntRegex = Regex("(\\D+)(\\d+)")
fun String.splitStringIntPartsOrNull(trimStringPart: Boolean = false): Pair<String, Int>? {
    val matchResult = stringIntRegex.matchEntire(this)
        ?: return null
    val (_, stringPart, intPart) = matchResult.groups.map { it!!.value }
    return (if (trimStringPart) stringPart.trim() else stringPart) to intPart.toInt()
}

val hexColorRegex = Regex("#(\\d|[a-f]){6}")

fun List<String>.splitByNewLine(): List<List<String>> {
    val builder = mutableListOf<List<String>>()
    val current = mutableListOf<String>()
    forEach {
        if (it.isEmpty() && current.isNotEmpty()) {
            builder.add(current.toList())
            current.clear()
        } else {
            current.add(it)
        }
    }
    if (current.isNotEmpty()) {
        builder.add(current.toList())
    }
    return builder
}

fun List<String>.splitByPredicate(predicate: (String) -> Boolean): List<List<String>> {
    val builder = mutableListOf<List<String>>()
    val current = mutableListOf<String>()
    var isFirst = true
    forEach {
        val matchesPredicate = predicate(it)
        if (matchesPredicate) {
            if (isFirst) {
                current.add(it)
                isFirst = false
            } else {
                builder.add(current.toList())
                current.clear()
                current.add(it)
            }
        } else {
            current.add(it)
        }
    }
    if (current.isNotEmpty()) {
        builder.add(current.toList())
    }
    return builder
}

@OptIn(ExperimentalTypeInference::class)
@OverloadResolutionByLambdaReturnType
fun <K, V> Map<K, V>.sumOf(fn: (Map.Entry<K, V>) -> Int) = map { fn(it) }.sum()
@OptIn(ExperimentalTypeInference::class)
@OverloadResolutionByLambdaReturnType
fun <K, V> Map<K, V>.sumOf(fn: (Map.Entry<K, V>) -> Long) = map { fn(it) }.sum()

fun <E1, E2> Iterable<E1>.cross(other: Iterable<E2>, includeSameIndex: Boolean = true): List<Pair<E1, E2>> {
    val cross = mutableListOf<Pair<E1, E2>>()
    for ((i1, e1) in this.withIndex()) {
        for ((i2, e2) in other.withIndex()) {
            if (includeSameIndex) {
                cross.add(Pair(e1, e2))
            } else {
                if (i1 != i2) {
                    cross.add(Pair(e1, e2))

                }
            }
        }
    }
    return cross
}

fun LongRange.subtractRange(other: LongRange): List<LongRange> {
    val intersectRangeOrNull = intersectRangeOrNull(other)
        ?: return listOf(this)
    if (this.length() == intersectRangeOrNull.length()) return emptyList()
    if (intersectRangeOrNull.first > this.first && intersectRangeOrNull.last < this.last) {
        return listOf(
            this.first..<intersectRangeOrNull.first,
            intersectRangeOrNull.last + 1..this.last
        )
    } else if (this.first == intersectRangeOrNull.first) {
        return listOf(intersectRangeOrNull.last + 1..this.last)
    } else if (this.last == intersectRangeOrNull.last) {
        return listOf(this.first..<intersectRangeOrNull.first)
    }
    TODO()
}

fun LongRange.length() = last - first + 1

fun LongRange.intersectsWith(other: LongRange): Boolean {
    val firstRange = if (first <= other.first) this else other
    val secondRange = if (first <= other.first) other else this
    return secondRange.first in firstRange
}

fun LongRange.intersectRangeOrNull(other: LongRange): LongRange? {
    if (this.intersectsWith(other)) {
        val firstRange = if (first <= other.first) this else other
        val secondRange = if (first <= other.first) other else this
        return secondRange.first..kotlin.math.min(firstRange.last, secondRange.last)
    }
    return null
}