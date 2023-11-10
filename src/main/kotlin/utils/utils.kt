package utils

fun List<String>.toIntList() = map { it.toInt() }

infix fun Int.toip(y: Int) = MutableIntPoint(this to y)

fun List<String>.toGrid(): MutableGrid<Char> {
    return MutableGrid(this.map { it.toCharArray().toTypedArray() })
}

enum class Direction(
    val movementOffset: IntPoint,
) {
    RIGHT(1 toip 0),
    LEFT(-1 toip 0),
    DOWN(0 toip 1),
    UP(0 toip -1),
    UP_LEFT(-1 toip -1),
    UP_RIGHT(1 toip -1),
    DOWN_LEFT(-1 toip 1),
    DOWN_RIGHT(1 toip 1);

    val x: Int get() = movementOffset.x
    val y: Int get() = movementOffset.y

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
fun println(vararg eles: Any?) {
    kotlin.io.println(eles.joinToString { it.toString() })
}

val intStringRegex = Regex("(\\d+)(\\D+)")
fun String.splitIntStringPartsOrNull(trimStringPart: Boolean = false): Pair<Int, String>? {
    val matchResult = intStringRegex.matchEntire(this)
        ?: return null
    val (_, intPart, stringPart) = matchResult.groups.map { it!!.value }
    return intPart.toInt() to if (trimStringPart) stringPart.trim() else stringPart
}

val stringIntRegex = Regex("(\\D+)(\\d+)")
fun String.splitStringIntPartsOrNull(trimStringPart: Boolean = false): Pair<String, Int>? {
    val matchResult = stringIntRegex.matchEntire(this)
        ?: return null
    val (_, stringPart, intPart) = matchResult.groups.map { it!!.value }
    return (if (trimStringPart) stringPart.trim() else stringPart) to intPart.toInt()
}

val hexColorRegex = Regex("#(\\d|[a-f]){6}")

fun List<String>.splitByNewLine(): List<List<String>> {
    val builder = mutableListOf<List<String>>()
    val current = mutableListOf<String>()
    forEach {
        if (it.isEmpty() && current.isNotEmpty()) {
            builder.add(current.toList())
            current.clear()
        } else {
            current.add(it)
        }
    }
    if (current.isNotEmpty()) {
        builder.add(current.toList())
    }
    return builder
}

fun <K, V> Map<K, V>.sumOf(fn: (Map.Entry<K, V>) -> Int) = map {
    fn(it)
}.sum()

fun <E1, E2> Iterable<E1>.cross(other: Iterable<E2>, includeSameIndex: Boolean = true): List<Pair<E1, E2>> {
    val cross = mutableListOf<Pair<E1, E2>>()
    for ((i1, e1) in this.withIndex()) {
        for ((i2, e2) in other.withIndex()) {
            if (includeSameIndex) {
                cross.add(Pair(e1, e2))
            } else {
                if (i1 != i2) {
                    cross.add(Pair(e1, e2))

                }
            }
        }
    }
    return cross
}