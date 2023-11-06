package utils

import java.util.*
import kotlin.Comparator

class Grid<T : Any>(
    val data: List<Array<T>>
)
{
    val maxRows = data.size
    val maxColumns = data.maxOf { it.size }

    init {
        println("maxRows: $maxRows, maxColumns: $maxColumns")
    }

    override fun toString(): String {
        val sb = StringBuilder()
        data.forEach {
            sb.appendLine(it.joinToString(""))
        }
        return sb.toString()
    }

    fun print() {
        println(this)
    }

    operator fun get(point: MutableIntPoint): T = get(point.first, point.second)
    operator fun get(x: Int, y: Int): T {
        return data[y][x]
    }
    fun getCyclic(x: Int, y: Int): T {
        val yNormalize = normalizeIndex(y, maxRows)
        val xNormalize = normalizeIndex(x, maxColumns)
        return get(xNormalize, yNormalize)
    }
    fun getCyclicOrDefault(x: Int, y: Int, default: () -> T): T {
        val yNormalize = normalizeIndex(y, maxRows)
        val xNormalize = normalizeIndex(x, maxColumns)
        return getOrDefault(xNormalize, yNormalize, default)
    }

    fun getRow(y: Int): Array<T> {
        require(y in 0 until maxRows)
        return data[y]
    }

    fun getOrDefault(x: Int, y: Int, default: () -> T): T {
        return data.getOrNull(y)?.getOrNull(x)
            ?: default()
    }

    fun getOrDefault(point: IntPoint, default: () -> T): T {
        return getOrDefault(point.x, point.y, default)
    }

    fun forEach(fn: (x: Int, y: Int, value: T, gotNextRow: Boolean) -> Unit) {
        data.forEachIndexed { y, chars ->
            var isFirst = true
            chars.forEachIndexed { x, v ->
                fn(x, y, v, isFirst)
                isFirst = false
            }
        }
    }

    fun find(v: T): MutableIntPoint {
        data.forEachIndexed { y, chars ->
            chars.forEachIndexed { x, c ->
                if (c == v) return x toip y
            }
        }
        TODO()
    }

    data class DijkstraResult(
        val source: MutableIntPoint,
        val pointToMinDist: Map<MutableIntPoint, Int>,
        val pointToPrev: Map<MutableIntPoint, MutableIntPoint>
    )

    fun dijkstra(source: MutableIntPoint, nextStatesFn: Grid<T>.(point: MutableIntPoint) -> List<MutableIntPoint>): DijkstraResult {
        data class DNode(val point: MutableIntPoint, val distance: Int)

        val pointToMinLengthFromSource = mutableMapOf<MutableIntPoint, Int>()
        val pointToPrev = mutableMapOf<MutableIntPoint, MutableIntPoint>()

        val statesToExplore = PriorityQueue<DNode>(object : Comparator<DNode> {
            override fun compare(o1: DNode, o2: DNode): Int {
                return o1.distance.compareTo(o2.distance)
            }
        })

        //        forEach { x, y, value, gotNextRow ->
        //            if (x to y != source) {
        //                statesToExplore.add(DNode(x to y, Int.MAX_VALUE))
        //            }
        //        }

        statesToExplore.add(DNode(source, 0))

        while (statesToExplore.isNotEmpty()) {
            val currMinNode = statesToExplore.poll()!!

            val nextStates = this.nextStatesFn(currMinNode.point)
            nextStates.forEach loop@{

                val currBest = pointToMinLengthFromSource.getOrDefault(it, Int.MAX_VALUE)
                val alt = currMinNode.distance + 1
                if (alt < currBest) {
                    pointToPrev[it] = currMinNode.point
                    pointToMinLengthFromSource[it] = alt
                    statesToExplore.add(DNode(it, alt))
                }
            }
        }

        println(pointToMinLengthFromSource)
        println(pointToPrev)
        forEach { x, y, value, gotNextRow ->
            if (gotNextRow) {
                println()
                print("${y.toString().padStart(3, '0')}: ")
            }
            if (pointToMinLengthFromSource.containsKey(x toip y)) {
                print('X')
            } else {
                print('O')
            }
        }
        println()
        return DijkstraResult(
            source, pointToMinLengthFromSource, pointToPrev
        )
    }

    fun bfs(source: MutableIntPoint, nextStatesFn: Grid<T>.(point: MutableIntPoint) -> List<MutableIntPoint>): DijkstraResult {
        val pointToMinLengthFromSource = mutableMapOf<MutableIntPoint, Int>()
        val pointToPrev = mutableMapOf<MutableIntPoint, MutableIntPoint>()

        val queue = LinkedList<MutableIntPoint>()
        queue.add(source)

        pointToMinLengthFromSource[source] = 0

        while (queue.isNotEmpty()) {
            val currPoint = queue.removeFirst()
            val currDist = pointToMinLengthFromSource[currPoint]!!
            val nextStates = this.nextStatesFn(currPoint)
            nextStates.forEach loop@{
                val currBest = pointToMinLengthFromSource.getOrDefault(it, Int.MAX_VALUE)
                val alt = currDist + 1
                if (alt < currBest) {
                    pointToPrev[it] = currPoint
                    pointToMinLengthFromSource[it] = alt
                    queue.add(it)
                }
            }
        }

        println(pointToMinLengthFromSource)
        println(pointToPrev)

        return DijkstraResult(
            source, pointToMinLengthFromSource, pointToPrev
        )
    }

    fun count(predicate: (x: Int, y: Int, value: T) -> Boolean): Int {
        var count = 0
        forEach { x, y, value, gotNextRow ->
            if (predicate(x, y, value)) count++
        }
        return count
    }

    fun filter(predicate: (x: Int, y: Int, value: T) -> Boolean): List<GridElement<T>> {
        val elements = mutableListOf<GridElement<T>>()
        forEach { x, y, value, gotNextRow ->
            if (predicate(x, y, value)) elements.add(GridElement(x, y, value))
        }
        return elements
    }

    data class GridElement<T> (val x: Int, val y: Int, val value: T) {
        val point: IntPoint get() = MutableIntPoint(x, y)
        val asNewMutablePoint: MutableIntPoint get() = MutableIntPoint(x, y)
    }

    fun elements(): List<GridElement<T>> {
        val elements = mutableListOf<GridElement<T>>()

        forEach { x, y, value, _ ->
            elements.add(GridElement(x, y, value))
        }

        return elements
    }
}