package aoc_2023

import com.kietyo.ktruth.assertThat
import utils.Direction
import utils.Grid
import utils.IntPoint
import utils.MutableGrid
import utils.MutableIntPoint
import utils.PointWithDirection
import utils.toGrid
import utils.toIntGrid
import kotlin.math.min
import kotlin.test.Test

internal class `23day17` {
    private val fileName = "day17"
    private val testFileName = "${fileName}_test"

    private fun List<String>.convertToDataObjectList() = run {
        this
    }

    data class WalkPoint(
        val pointWithDirection: PointWithDirection,
        val stride: Int
    ) {
        val x get() = pointWithDirection.x
        val y get() = pointWithDirection.y
        val direction get() = pointWithDirection.direction
        init {
            require(stride >= 1 && stride <= 3)
        }
    }

    private fun numLast3MatchingLastDirection(path: List<WalkPoint>): Int {
        val lastDir = path.last().direction
        var num = 1
        for (i in (path.size-2) downTo maxOf(0, path.size-1-3)) {
            if (path[i].direction == lastDir) {
                num++
            } else {
                return num
            }
        }
        return num
    }

    class Solver(val grid: Grid<Int>) {
        var leastHeatLoss = Int.MAX_VALUE
        val pointToLeastTraveler = mutableMapOf<WalkPoint, Traveler>()

        inner class Traveler(
            val path: List<WalkPoint>,
            val currHeatLoss: Int
        ) {
            val last get() = path.last()
            private fun isLast3DirectionsSame(): Boolean {
                val last3 = path.takeLast(3)
                return last3.all { it.direction == last3.first().direction }
            }


            fun getNextStates(): MutableList<WalkPoint> {
                val last = last
                val states = mutableListOf<WalkPoint>()
                if (last.stride < 3) {
                    val next = last.pointWithDirection.move()
                    if (next in grid) {
                        states.add(WalkPoint(next, last.stride+1))
                    }
                }

                run {
                    val next = last.pointWithDirection.rotateClockwise().move()
                    if (next in grid) {
                        states.add(WalkPoint(next, 1))
                    }
                }

                run {
                    val next = last.pointWithDirection.rotateCounterClockwise().move()
                    if (next in grid) {
                        states.add(WalkPoint(next, 1))
                    }
                }
                return states
            }

            fun addToPath(p: WalkPoint): Traveler {
                return Traveler(path + p, currHeatLoss + grid[p.pointWithDirection])
            }
        }

        fun solve() {
            var travelers = mutableListOf(
                Traveler(
                    listOf(
                        WalkPoint(PointWithDirection(0, 0, Direction.RIGHT), 1),
                    ), grid[0, 0]
                ),
                Traveler(
                    listOf(
                        WalkPoint(PointWithDirection(0, 0, Direction.DOWN), 1),
                    ), grid[0, 0]
                )
            )

            while (travelers.isNotEmpty()) {
                val newTravelers = mutableListOf<Traveler>()
                for (traveler in travelers) {
                    if (traveler.last.x == grid.width - 1 && traveler.last.y == grid.height - 1) {
                        leastHeatLoss = min(leastHeatLoss, traveler.currHeatLoss)
                        println("leastHeatLoss: $leastHeatLoss")
                        continue
                    }
                    if (traveler.currHeatLoss > leastHeatLoss) {
                        continue
                    }
                    val lastPoint = traveler.last
                    val leastTraveler = pointToLeastTraveler.get(lastPoint)
                    if (leastTraveler != null) {
                        if (traveler.currHeatLoss >= leastTraveler.currHeatLoss) {
                            // Already exists a path to this pos that is better or the same
                            continue
                        } else {
                            // Current traveler is better
                            pointToLeastTraveler[lastPoint] = traveler
                        }
                    } else {
                        pointToLeastTraveler[lastPoint] = traveler
                    }
                    for (state in traveler.getNextStates()) {
                        newTravelers.add(traveler.addToPath(state))
                    }
                }
                travelers = newTravelers
            }

            val res = leastHeatLoss - grid[0,0]

            println("res: $res")
        }
    }

    private fun part1Calculation(input: List<String>): Int {
        val converted = input.convertToDataObjectList()
        println(converted)

        val grid = converted.toIntGrid()
        println(grid)

        val solver = Solver(grid)
        solver.solve()

        return 0
    }

    private fun part2Calculation(input: List<String>): Int {
        val converted = input.convertToDataObjectList()
        println(converted)

        return 0
    }

    @Test
    fun part1Test() {
        val input = readInput(testFileName)
        assertThat(part1Calculation(input)).isEqualTo(0)
    }

    @Test
    fun part1() {
        val input = readInput(fileName)
        assertThat(part1Calculation(input)).isEqualTo(0)
    }

    @Test
    fun part2Test() {
        val input = readInput(testFileName)
        assertThat(part2Calculation(input)).isEqualTo(0)
    }

    @Test
    fun part2() {
        val input = readInput(fileName)
        assertThat(part2Calculation(input)).isEqualTo(0)
    }
}