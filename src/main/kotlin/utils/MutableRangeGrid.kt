package utils

class MutableRangeGrid: Grid<Boolean> {
    private val _ranges = mutableListOf<Range>()
    val ranges: List<Range> get() = _ranges

    override val minX: Int
        get() = _ranges.minOf { it.minX }
    override val maxX: Int
        get() = _ranges.maxOf { it.maxX }
    override val minY: Int
        get() = _ranges.minOf { it.minY }
    override val maxY: Int
        get() = _ranges.maxOf { it.maxY }

    override fun getOrNull(x: Int, y: Int): Boolean {
        return _ranges.any { it.contains(x, y) }
    }

    fun add(r: Range) {
        _ranges.add(r)
    }

    fun getHorizontalRanges(y: Int): List<Range> {
        val horizontalRanges = mutableListOf<Range>()
        val ranges = ranges.filter { it.containsY(y) }.sortedBy { it.minX }

        var currRange: Range? = null
        for (range in ranges) {
            if (range.isHorizontalRange) {
                if (currRange == null) {
                    currRange = range
                    continue
                }
                if (currRange.isTouchingButNotIntersecting(range)) {
                    currRange = currRange.combine(range)
                } else {
                    horizontalRanges.add(currRange)
                    currRange = range
                }
            } else {
                val pointRangeY = range.getPointRangeY(y)
                if (currRange == null) {
                    currRange = pointRangeY
                    continue
                }
                if (pointRangeY.isTouchingButNotIntersecting(currRange)) {
                    currRange = currRange.combine(pointRangeY)
                } else {
                    horizontalRanges.add(currRange)
                    currRange = pointRangeY
                }
            }
        }
        if (currRange != null) {
            horizontalRanges.add(currRange)
        }
        return horizontalRanges
    }

    override fun toString(): String {
        val sb = StringBuilder()
        for (y in minY..maxY) {
            sb.appendLine(getLineString(y))
        }
        return sb.toString()
    }

    fun getLineString(y: Int): String {
        val currLineSb = StringBuilder()
        for (x in minX..maxX) {
            val char = if (getOrDefault(x, y) {false}) '#' else '.'
            currLineSb.append(char)
        }
        return currLineSb.toString()
    }
}