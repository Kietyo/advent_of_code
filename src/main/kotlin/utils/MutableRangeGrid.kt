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

    fun getHorizontalRanges(y: Int) {
        val horizontalRanges = mutableListOf<Range>()
        val ranges = ranges.filter { it.containsY(y) }.sortedBy { it.minX }

        var currRange = ranges.first()
        if (currRange.isVerticalRange) {
            currRange = currRange.getPointRangeY(y)
        }
    }

    override fun toString(): String {
        val sb = StringBuilder()
        for (y in minY..maxY) {
            val currLineSb = StringBuilder()
            for (x in minX..maxX) {
                val char = if (getOrDefault(x, y) {false}) '#' else '.'
                currLineSb.append(char)
            }
            sb.appendLine(currLineSb)
        }
        return sb.toString()
    }
}