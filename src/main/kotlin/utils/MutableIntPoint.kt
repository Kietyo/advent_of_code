package utils

import kotlin.math.abs

interface IntPoint {
    val x: Int
    val y: Int

    operator fun plus(other: IntPoint) = MutableIntPoint(x + other.x, y + other.y)
    operator fun times(multiplier: Int) = MutableIntPoint(x * multiplier, y * multiplier)

    fun clone(): MutableIntPoint {
        return MutableIntPoint(x, y)
    }

    fun directionTo(other: IntPoint): Direction {
        require((abs(x - other.x) == 1) xor (abs(y - other.y) == 1))
        return when {
            other.x - x == 1 -> Direction.RIGHT
            other.x - x == -1 -> Direction.LEFT
            other.y - y == 1 -> Direction.DOWN
            other.y - y == -1 -> Direction.UP
            else -> TODO()
        }
    }
}

data class MutableIntPoint(
    override var x: Int,
    override var y: Int
): Comparable<MutableIntPoint>, IntPoint {
    constructor(pair: Pair<Int, Int>): this(pair.first, pair.second)
    fun copy2(first: Int = x,
              second: Int = y) = MutableIntPoint(first, second)

    val first get() = x
    val second get() = y

    val oneDown get() = MutableIntPoint(x, y + 1)
    val oneDownOneLeft get() = MutableIntPoint(x - 1, y + 1)
    val oneDownOneRight get() = MutableIntPoint(x + 1, y + 1)

    fun inPlaceAdd(other: IntPoint) {
        x += other.x
        y += other.y
    }

    fun manhattanDistance(other: MutableIntPoint): Int {
        return manhattanDistance(other.x, other.y)
    }

    fun manhattanDistance(otherX: Int, otherY: Int): Int {
        return abs(x - otherX) + abs(y - otherY)
    }

    override fun compareTo(other: MutableIntPoint): Int {
        if (first == other.first) {
            return second.compareTo(other.second)
        }
        if (second == other.second) {
            return first.compareTo(other.first)
        }

        val xComp = first.compareTo(other.first)
        if (xComp == 0) {
            return second.compareTo(other.second)
        }
        return xComp
    }

}