package utils

public interface Grid<T> {
    val numRows: Int
    val numColumns: Int
    operator fun get(point: MutableIntPoint): T
    operator fun get(x: Int, y: Int): T
    fun getOrDefault(x: Int, y: Int, default: () -> T): T
    fun getOrDefault(point: IntPoint, default: () -> T): T
    fun forEach(fn: (x: Int, y: Int, value: T, isFirstElementInNewRow: Boolean) -> Unit): Unit
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