package utils

public interface Grid<T> {
    val numRows: Int
    val numColumns: Int
    val width: Int get() = numColumns
    val height: Int get() = numRows
    operator fun get(point: IntPoint): T
    operator fun get(x: Int, y: Int): T
    fun getOrDefault(x: Int, y: Int, default: () -> T): T
    fun getOrDefault(point: IntPoint, default: () -> T): T
    fun getOrNull(x: Int, y: Int): T?
    fun getRow(y: Int): List<T> {
        require(y in 0 until numRows)
        val data = mutableListOf<T>()
        repeat(numColumns) {
            data.add(get(it, y))
        }
        return data
    }

    fun getColumn(x: Int): List<T> {
        require(x in 0 until numColumns)
        val data = mutableListOf<T>()
        repeat(numRows) {
            data.add(get(x, it))
        }
        return data
    }

    fun forEachColumn(function: (x: Int, columnData: List<T>) -> Unit) {
        repeat(numColumns) { x ->
            val currColumnData = mutableListOf<T>()
            repeat(numRows) { y ->
                currColumnData.add(get(x, y))
            }
            function(x, currColumnData)
        }
    }
    fun forEachRow(function: (y: Int, rowData: List<T>) -> Unit) {
        repeat(numRows) { y ->
            val currRowData = mutableListOf<T>()
            repeat(numColumns) { x ->
                currRowData.add(get(x, y))
            }
            function(y, currRowData)
        }
    }

    operator fun contains(point: IntPoint): Boolean {
        return point.x in 0..<width && point.y in 0..<height
    }

    private fun getAdjacentInternal(x: Int, y: Int, direction: Direction): PointWithData<T>? {
        val data = getOrNull(x, y) ?: return null
        return PointWithData(data, x, y, direction)
    }

    fun getAdjacents(x: Int, y: Int, includeDiagonals: Boolean = true): List<PointWithData<T>> {
        return buildList {
            addIfNotNull(getAdjacentInternal(x - 1, y, Direction.LEFT))
            addIfNotNull(getAdjacentInternal(x + 1, y, Direction.RIGHT))
            addIfNotNull(getAdjacentInternal(x, y - 1, Direction.UP))
            addIfNotNull(getAdjacentInternal(x, y + 1, Direction.DOWN))
            if (includeDiagonals) {
                addIfNotNull(getAdjacentInternal(x - 1, y - 1, Direction.UP_LEFT))
                addIfNotNull(getAdjacentInternal(x - 1, y + 1, Direction.DOWN_LEFT))
                addIfNotNull(getAdjacentInternal(x + 1, y - 1, Direction.UP_RIGHT))
                addIfNotNull(getAdjacentInternal(x + 1, y + 1, Direction.DOWN_RIGHT))
            }

        }
    }
}

inline fun <T> Grid<T>.forEach(fn: (x: Int, y: Int, value: T, isFirstElementInNewRow: Boolean) -> Unit) {
    repeat(numRows) { y ->
        var isFirst = true
        repeat(numColumns) { x ->
            fn(x, y, get(x, y), isFirst)
            isFirst = false
        }
    }
}

inline fun <T> Grid<T>.forEachReversed(fn: (x: Int, y: Int, value: T, isFirstElementInNewRow: Boolean) -> Unit) {
    repeat(numRows) { y ->
        val translatedY = (width - 1 - y)
        var isFirst = true
        repeat(numColumns) { x ->
            val translatedX = (height - 1 - x)
            fn(translatedX, translatedY, get(translatedX, translatedY), isFirst)
            isFirst = false
        }
    }
}