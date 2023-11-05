package utils

import intersectOrNull
import length

enum class Action {
    ON,
    OFF
}

data class Cube(
    var action: Action,
    val xRange: IntRange,
    val yRange: IntRange,
    val zRange: IntRange
) {
    val x0 = xRange.first
    val x1 = xRange.last
    val y0 = yRange.first
    val y1 = yRange.last
    val z0 = zRange.first
    val z1 = zRange.last
    val volume = xRange.length().toLong() * yRange.length().toLong() * zRange.length().toLong()

    init {
        require(x1 >= x0)
        require(y1 >= y0)
        require(z1 >= z0)
    }

    fun intersect(other: Cube): Cube? {
        val xIntersect = intersectOrNull(xRange, other.xRange) ?: return null
        val yIntersect = intersectOrNull(yRange, other.yRange) ?: return null
        val zIntersect = intersectOrNull(zRange, other.zRange) ?: return null
        return Cube(other.action, xIntersect, yIntersect, zIntersect)
    }
}
