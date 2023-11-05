package utils

class IntPointRange(p1: MutableIntPoint, p2: MutableIntPoint) : ClosedRange<MutableIntPoint> {
    override val start: MutableIntPoint
    override val endInclusive: MutableIntPoint

    init {
        require(p1.x == p2.x || p1.y == p2.y)
        when {
            p1.x == p2.x -> {
                val topMostY = minOf(p1.y, p2.y)
                val bottomMostY = maxOf(p1.y, p2.y)
                start = MutableIntPoint(p1.x, topMostY)
                endInclusive = MutableIntPoint(p1.x, bottomMostY)
            }

            p1.y == p2.y -> {
                val leftMostX = minOf(p1.x, p2.x)
                val rightMostX = maxOf(p1.x, p2.x)
                start = MutableIntPoint(leftMostX, p1.y)
                endInclusive = MutableIntPoint(rightMostX, p1.y)
            }

            else -> TODO()
        }
    }

    private val isXAligned = start.x == endInclusive.x
    private val isYAligned = start.y == endInclusive.y
    val yRange = start.y..endInclusive.y
    val xRange = start.x..endInclusive.x

    override fun contains(value: MutableIntPoint): Boolean {
        when {
            isXAligned -> {
                if (value.x != start.x) return false
                return value.y in yRange
            }

            isYAligned -> {
                if (value.y != start.y) return false
                return value.x in xRange
            }
        }
        return false
    }

    override fun toString(): String {
        return "IntPointRange(start=$start, endInclusive=$endInclusive)"
    }
}