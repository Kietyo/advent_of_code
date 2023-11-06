package utils

fun List<String>.toIntList() = map { it.toInt() }

infix fun Int.toip(y: Int) = MutableIntPoint(this to y)

fun List<String>.toGrid(): Grid<Char> {
    return Grid(this.map { it.toCharArray().toTypedArray() })
}

enum class Direction(
    val movementOffset: IntPoint,
) {
    RIGHT(1 toip 0),
    DOWN(0 toip 1),
    LEFT(-1 toip 0),
    UP(0 toip -1);

    fun getNextDirectionClockwise(): Direction {
        return Direction.values().getCyclic(ordinal + 1)
    }

    fun getNextDirectionCounterClockwise(): Direction {
        return Direction.values().getCyclic(ordinal - 1)
    }
}

fun normalizeIndex(idx: Int, size: Int): Int {
    val mod = idx % size
    return if (mod < 0) mod + size else mod
}

fun <T> Array<T>.getCyclic(idx: Int): T {
    return get(normalizeIndex(idx, size))
}

fun <T> List<T>.getCyclic(idx: Int): T {
    return get(normalizeIndex(idx, size))
}
