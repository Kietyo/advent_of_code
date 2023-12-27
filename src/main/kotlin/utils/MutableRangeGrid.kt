package utils

class MutableRangeGrid: Grid<Boolean> {
    val ranges = mutableListOf<Range>()

    override val minX: Int
        get() = ranges.minOf { it.minX }
    override val maxX: Int
        get() = ranges.maxOf { it.maxX }
    override val minY: Int
        get() = ranges.minOf { it.minY }
    override val maxY: Int
        get() = ranges.maxOf { it.maxY }

    override fun getOrNull(x: Int, y: Int): Boolean {
        return ranges.any { it.contains(x, y) }
    }

    fun add(r: Range) {
        ranges.add(r)
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