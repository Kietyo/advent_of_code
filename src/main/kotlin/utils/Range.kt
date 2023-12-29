package utils

class Range(
    t1: IntPoint,
    t2: IntPoint
) {
    constructor(p: IntPoint): this(p, p)
    val p1: IntPoint
    val p2: IntPoint
    val range: Int get() = (p2.x - p1.x) + (p2.y - p1.y) + 1
    val minX get() = p1.x
    val maxX get() = p2.x
    val minY get() = p1.y
    val maxY get() = p2.y
    val xRange get() = p1.x..p2.x
    val yRange get() = p1.y..p2.y
    val isVerticalRange get() = p1.x == p2.x
    val isHorizontalRange get() = p1.y == p2.y
    init {
        require(t1.x == t2.x || t1.y == t2.y)
        if (t1.x == t2.x) {
            if (t1.y <= t2.y) {
                p1 = t1
                p2 = t2
            } else {
                p1 = t2
                p2 = t1
            }
        } else {
            if (t1.x <= t2.x) {
                p1 = t1
                p2 = t2
            } else {
                p1 = t2
                p2 = t1
            }
        }
    }

    fun getPointRange(x: Int, y: Int): Range {
        require(contains(x, y))
        return Range(x toip y, x toip y)
    }
    fun getPointRangeY(y: Int): Range {
        require(isVerticalRange && containsY(y))
        return Range(minX toip y, minX toip y)
    }
    operator fun contains(p: IntPoint) = contains(p.x, p.y)
    fun contains(x: Int, y: Int) = containsX(x) && containsY(y)
    fun containsX(x: Int) = x in p1.x..p2.x
    fun containsY(y: Int) = y in p1.y..p2.y

    fun intersectsWith(other: Range): Boolean {
        return when {
            isVerticalRange -> {
                when {
                    other.isVerticalRange -> {
                        p1.x == other.p1.x && yRange.intersectsWith(other.yRange)
                    }
                    other.isHorizontalRange -> {
                        other.p1.y in yRange && p1.x in other.xRange
                    }
                    else -> TODO()
                }
            }
            isHorizontalRange -> {
                when {
                    other.isHorizontalRange -> {
                        p1.y == other.p1.y && xRange.intersectsWith(other.xRange)
                    }
                    other.isVerticalRange -> {
                        other.p1.x in xRange && p1.y in other.yRange
                    }
                    else -> TODO()
                }
            }
            else -> TODO()
        }
    }

    fun isTouchingButNotIntersectingVertically(other: Range): Boolean {
        require(isVerticalRange)
        if (!other.isVerticalRange) return false
        return (this.yRange.intersectsWith(other.yRange) &&
                (other.minX == minX + 1 || other.minX == minX - 1)) ||
                (minX == other.minX && (
                        maxY + 1 == other.minY ||
                                minY - 1 == other.maxY
                        ))
    }

    fun isTouchingButNotIntersectingHorizontally(other: Range): Boolean {
        require(isHorizontalRange && other.isHorizontalRange)
        return (this.xRange.intersectsWith(other.xRange) &&
                (other.minY == minY + 1 || other.minY == minY - 1)) ||
                (minY == other.minY && (
                        maxX + 1 == other.minX ||
                                minX - 1 == other.maxX
                        ))
    }

    fun isTouchingButNotIntersecting(other: Range): Boolean {
        return when {
            isVerticalRange && isHorizontalRange ->
                isTouchingButNotIntersectingVertically(other) || isTouchingButNotIntersectingHorizontally(other)
            isVerticalRange -> {
                when {
                    other.isVerticalRange -> {
                        isTouchingButNotIntersectingVertically(other)
                    }
                    other.isHorizontalRange -> TODO()
                    else -> TODO()
                }
            }

            isHorizontalRange -> {
                when {
                    other.isHorizontalRange -> {
                        isTouchingButNotIntersectingHorizontally(other)
                    }
                    other.isVerticalRange -> TODO()
                    else -> TODO()
                }
            }

            else -> TODO()
        }
    }


    override fun toString(): String = "(${p1.x},${p1.y})..(${p2.x},${p2.y})"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Range

        if (p1 != other.p1) return false
        if (p2 != other.p2) return false

        return true
    }

    override fun hashCode(): Int {
        var result = p1.hashCode()
        result = 31 * result + p2.hashCode()
        return result
    }

    fun combine(other: Range): Range {
        if (isHorizontalRange && other.isHorizontalRange && isTouchingButNotIntersectingHorizontally(
                other
            ) &&
            minY == other.minY
        ) {
            return Range(
                minOf(minX, other.minX) toip minY,
                maxOf(maxX, other.maxX) toip minY
            )
        }
        TODO()
    }
}