package utils

public interface Grid<T> {
    val numRows: Int
    val numColumns: Int
    val width: Int get() = numColumns
    val height: Int get() = numRows
    operator fun get(point: MutableIntPoint): T
    operator fun get(x: Int, y: Int): T
    fun getOrDefault(x: Int, y: Int, default: () -> T): T
    fun getOrDefault(point: IntPoint, default: () -> T): T
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