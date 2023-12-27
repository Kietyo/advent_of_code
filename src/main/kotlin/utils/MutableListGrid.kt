package utils

class MutableListGrid<T : Any>(
    val defaultValue: T,
): Grid<T> {
    private val points = mutableMapOf<IntPoint, T>()

    override val minX: Int get() = points.keys.minOf { it.x }
    override val maxX: Int get() = points.keys.maxOf { it.x }
    override val minY: Int get() = points.keys.minOf { it.y }
    override val maxY: Int get() = points.keys.maxOf { it.y }

    fun put(x: Int, y: Int, data: T) {
        points.put(IntPoint(x, y), data)
    }

    override fun getOrNull(x: Int, y: Int): T? {
        return points[IntPoint(x, y)]
    }

    override fun toString(): String {
        val sb = StringBuilder()
        for (y in minY..maxY) {
            val currLineSb = StringBuilder()
            for (x in minX..maxX) {
                currLineSb.append(getOrDefault(x, y) {defaultValue})
            }
            sb.appendLine(currLineSb)
        }
        return sb.toString()
    }
}