package utils

class Range(
    t1: IntPoint,
    t2: IntPoint
) {
    val p1: IntPoint
    val p2: IntPoint
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