package utils

import java.util.*
import kotlin.Comparator

data class PointWithDirection(
    override val x: Int, override val y: Int,
    val direction: Direction
): IntPoint {
    fun toIntPoint() = MutableIntPoint(x, y)
    fun move() = PointWithDirection(x + direction.x, y + direction.y, direction)
    fun rotateClockwise() = PointWithDirection(x, y, direction.getNextDirectionClockwise())
    fun rotateCounterClockwise() = PointWithDirection(x, y, direction.getNextDirectionCounterClockwise())
}

data class PointWithData<T>(
    val data: T, override val x: Int, override val y: Int,
    val relativeDirection: Direction
): IntPoint {
    fun toIntPoint() = MutableIntPoint(x, y)
}

class MutableArrayGrid<T : Any>(
    val data: List<Array<T>>
): Grid<T>, Iterable<PointWithData<T>> {
    override val minX: Int get() = 0
    override val maxX: Int get() = data.maxOf { it.size }-1
    override val minY: Int get() = 0
    override val maxY: Int get() = data.size-1

    init {
//        println("numRows: $numRows, numColumns: $numColumns")
    }

    override fun iterator(): Iterator<PointWithData<T>> =  object : Iterator<PointWithData<T>> {
        private var currentRow = 0
        private var currentCol = 0

        override fun hasNext(): Boolean {
            return currentRow < data.size && currentCol < data[currentRow].size
        }

        override fun next(): PointWithData<T> {
            if (!hasNext()) throw NoSuchElementException()

            val pointData = PointWithData(
                data = data[currentRow][currentCol],
                x = currentCol,
                y = currentRow,
                relativeDirection = Direction.UP // Or another default direction
            )

            currentCol++
            if (currentCol >= data[currentRow].size) {
                currentCol = 0
                currentRow++
            }

            return pointData
        }
    }

    override fun toString(): String {
        val sb = StringBuilder()
        data.forEach {
            sb.appendLine(it.joinToString(""))
        }
        return sb.toString()
    }

    fun contentEquals(other: MutableArrayGrid<T>): Boolean {
        if (data.size != other.data.size) return false
        for (i in data.indices) {
            if (!data[i].contentEquals(other.data[i])) return false
        }
        return true
    }

    fun print() {
        println(this)
    }

    fun getStrideFrom(x: Int, y: Int, direction: Direction): List<PointWithData<T>> {
        val datas = mutableListOf<PointWithData<T>>()
        var i = 1
        while (true) {
            val newX = x + direction.x * i
            val newY = y + direction.y * i
            val data = getOrNull(newX, newY)
            if (data == null) {
                break
            } else {
                datas.add(PointWithData(data, newX, newY, direction))
            }
            i++
        }
        return datas
    }

    override operator fun get(point: IntPoint): T = get(point.x, point.y)
    override operator fun get(x: Int, y: Int): T {
        return data[y][x]
    }

    operator fun set(x: Int, y: Int, newData: T) {
        data[y][x] = newData
    }

    fun getCyclic(x: Int, y: Int): T {
        val yNormalize = normalizeIndex(y, numRows)
        val xNormalize = normalizeIndex(x, numColumns)
        return get(xNormalize, yNormalize)
    }
    fun getCyclicOrDefault(x: Int, y: Int, default: () -> T): T {
        val yNormalize = normalizeIndex(y, numRows)
        val xNormalize = normalizeIndex(x, numColumns)
        return getOrDefault(xNormalize, yNormalize, default)
    }

    override fun getOrNull(x: Int, y: Int): T? {
        return data.getOrNull(y)?.getOrNull(x)
    }

    override fun getOrDefault(x: Int, y: Int, default: () -> T): T {
        return data.getOrNull(y)?.getOrNull(x)
            ?: default()
    }

    override fun getOrDefault(point: IntPoint, default: () -> T): T {
        return getOrDefault(point.x, point.y, default)
    }

    fun getAllPoints(value: T): List<IntPoint> {
        val allPoints = mutableListOf<IntPoint>()
        forEach { x, y, v, gotNextRow ->
            if (v == value) {
                allPoints.add(MutableIntPoint(x, y))
            }
        }
        return allPoints
    }


    fun copy() = MutableArrayGrid(data.map { it.clone() })

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
    ) {
        fun getPathToPoint(point: MutableIntPoint): List<MutableIntPoint> {
            val path = mutableListOf<MutableIntPoint>()
            var curr = point
            while (true) {
                path.add(curr)
                if (curr == source) {
                    break
                }
                curr = pointToPrev[curr]!!
            }
            return path
        }
    }

    data class PointWithCost(val point: MutableIntPoint, val cost: Int)
    fun dijkstra(source: MutableIntPoint,
                 nextStatesFn: MutableArrayGrid<T>.(point: MutableIntPoint) -> List<PointWithCost>): DijkstraResult {

        data class DNode(val point: MutableIntPoint, val distance: Int)

        val pointToMinLengthFromSource = mutableMapOf<MutableIntPoint, Int>()
        val pointToPrev = mutableMapOf<MutableIntPoint, MutableIntPoint>()

        val statesToExplore = PriorityQueue(object : Comparator<DNode> {
            override fun compare(o1: DNode, o2: DNode): Int {
                return o1.distance.compareTo(o2.distance)
            }
        })

        //        forEach { x, y, value, gotNextRow ->
        //            if (x to y != source) {
        //                statesToExplore.add(DNode(x to y, Int.MAX_VALUE))
        //            }
        //        }

        pointToMinLengthFromSource.put(source, 0)
        statesToExplore.add(DNode(source, 0))

        while (statesToExplore.isNotEmpty()) {
            val currMinNode = statesToExplore.poll()!!

            val nextStates = this.nextStatesFn(currMinNode.point)
            nextStates.forEach loop@{curr ->
                val it = curr.point
                val currBest = pointToMinLengthFromSource.getOrDefault(it, Int.MAX_VALUE)
                val alt = currMinNode.distance + curr.cost
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

    fun bfs(source: MutableIntPoint, nextStatesFn: MutableArrayGrid<T>.(point: MutableIntPoint) -> List<MutableIntPoint>): DijkstraResult {
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

    companion object {
        inline fun <reified T : Any> create(
            width: Int,
            height: Int,
            function: () -> T
        ): MutableArrayGrid<T> {
            val data = mutableListOf<Array<T>>()
            repeat(height) { y ->
                data.add(Array(width) { function() })
            }
            return MutableArrayGrid(data)
        }
    }
}

