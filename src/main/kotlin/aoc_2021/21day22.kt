import utils.Action
import utils.Cube
import kotlin.math.max
import kotlin.math.min

fun IntRange.clamp(lowInclusive: Int, highInclusive: Int) =
    max(this.first, -50)..min(this.last, 50)

fun IntRange.length() = this.last - this.first + 1

fun intersectOrNull(r1: IntRange, r2: IntRange): IntRange? {
    val (minRange, highRange) = if (r1.first <= r2.first) {
        Pair(r1, r2)
    } else Pair(r2, r1)

    if (minRange.last in highRange || highRange.last in minRange) {
        return max(minRange.first, highRange.first)..min(minRange.last, highRange.last)
    }
    return null
}

fun main() {
    fun part1(inputs: List<String>) {
        println(inputs)
        val regex =
            """(on|off) x=(-?\d+)..(-?\d+),y=(-?\d+)..(-?\d+),z=(-?\d+)..(-?\d+)""".toRegex()
        val processedInputs = inputs.map {
            val (action, x0, x1, y0, y1, z0, z1) = regex.matchEntire(it)!!.destructured
            utils.Cube(
                if (action == "on") Action.ON else Action.OFF,
                x0.toInt()..x1.toInt(),
                z0.toInt()..z1.toInt(),
                y0.toInt()..y1.toInt(),
            )
        }

        println(processedInputs.joinToString("\n"))

        val cubes = mutableSetOf<Triple<Int, Int, Int>>()

        processedInputs.forEach {
            for (x in it.xRange.clamp(-50, 50)) {
                for (y in it.yRange.clamp(-50, 50)) {
                    for (z in it.zRange.clamp(-50, 50)) {
                        when (it.action) {
                            Action.ON -> cubes.add(Triple(x, y, z))
                            Action.OFF -> cubes.remove(Triple(x, y, z))
                        }
                    }
                }
            }
        }

        println(cubes.size)
    }

    fun part2(inputs: List<String>) {
        println(inputs)

        val regex =
            """(on|off) x=(-?\d+)..(-?\d+),y=(-?\d+)..(-?\d+),z=(-?\d+)..(-?\d+)""".toRegex()
        val processedInputs = inputs.map {
            val (action, x0, x1, y0, y1, z0, z1) = regex.matchEntire(it)!!.destructured
            utils.Cube(
                if (action == "on") Action.ON else Action.OFF,
                x0.toInt()..x1.toInt(),
                z0.toInt()..z1.toInt(),
                y0.toInt()..y1.toInt(),
            )
        }

        val outputCubes = mutableListOf<Cube>()

        println(processedInputs.joinToString("\n"))

        for (input in processedInputs) {
            val intersects = outputCubes.mapNotNull {
                input.intersect(it)
            }

            if (input.action == Action.ON) {
                outputCubes.add(input)
            }

            for (intersect in intersects) {
                when (input.action) {
                    Action.ON -> {
                        when (intersect.action) {
                            Action.ON -> outputCubes.add(intersect.apply {
                                action = Action.OFF
                            })
                            Action.OFF -> outputCubes.add(intersect.apply {
                                action = Action.ON
                            })
                        }
                    }
                    Action.OFF -> {
                        when (intersect.action) {
                            Action.ON -> outputCubes.add(intersect.apply {
                                action = Action.OFF
                            })
                            Action.OFF -> outputCubes.add(intersect.apply {
                                action = Action.ON
                            })
                        }
                    }
                }
            }
        }

        println(outputCubes.sumOf {
            when (it.action) {
                Action.ON -> it.volume
                Action.OFF -> -it.volume
            }
        })
    }

    val testInput = readInput("day22_test")
    val mainInput = readInput("day22")

    //    part1(testInput)
    //        part1(mainInput)
    //
    part2(testInput)
    //    part2(mainInput)


}