package utils

class Range(
    t1: IntPoint,
    t2: IntPoint
) {
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

    operator fun contains(p: IntPoint) = contains(p.x, p.y)
    fun contains(x: Int, y: Int) = x in p1.x..p2.x && y in p1.y..p2.y

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
}