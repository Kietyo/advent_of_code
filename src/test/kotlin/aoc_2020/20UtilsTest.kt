package aoc_2020

import com.kietyo.ktruth.assertThat
import utils.Direction
import utils.Matrix
import utils.MutableGrid
import utils.PointWithData
import utils.createRotationMatrix
import utils.hexColorRegex
import utils.splitIntStringPartsOrNull
import utils.splitStringIntPartsOrNull
import utils.toRadians
import utils.toip
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class `20UtilsTest` {

    @Test
    fun splitIntStringPartTest() {
        assertEquals(1 to "in", "01in".splitIntStringPartsOrNull())
        assertEquals(0 to "in", "0in".splitIntStringPartsOrNull())
        assertEquals(177 to "in", "177in".splitIntStringPartsOrNull())
        assertNull("177".splitIntStringPartsOrNull())

        assertEquals(177 to " in", "177 in".splitIntStringPartsOrNull())
        assertEquals(177 to "in", "177 in".splitIntStringPartsOrNull(trimStringPart = true))
        assertEquals(6 to "dotted black bags", "6 dotted black bags".splitIntStringPartsOrNull(trimStringPart = true))
    }

    @Test
    fun splitStringIntPartTest() {
        assertEquals("+" to 1, "+1".splitStringIntPartsOrNull())
        assertEquals("+" to 0, "+0".splitStringIntPartsOrNull())
        assertEquals("+" to 12, "+012".splitStringIntPartsOrNull())
        assertEquals("+" to 12, "+00012".splitStringIntPartsOrNull())
        assertNull("0".splitStringIntPartsOrNull())
        assertNull("1".splitStringIntPartsOrNull())
    }

    @Test
    fun matchRegex() {
        val answer = hexColorRegex.matchEntire("#602927")
        println(answer)
    }

    @Test
    fun understandMath() {
        println(127 / 2)

        println((127 + 64) / 2)

        println((32 + 63) / 2)
    }

    @Test
    fun copyGridTest() {
        val grid = MutableGrid(listOf(
            arrayOf(1, 2, 3),
            arrayOf(4, 5, 6),
        ))

        assertEquals(1, grid[0, 0])

        val gridCopy = grid.copy()
        gridCopy.set(0, 0, 11)

        assertEquals(1, grid[0, 0])
        assertEquals(11, gridCopy[0, 0])
    }

    @Test
    fun gridGetStrideTest() {
        val grid = MutableGrid(listOf(
            arrayOf(1, 2, 3, 4, 5),
            arrayOf(6, 7, 8, 9, 10),
            arrayOf(11, 12, 13, 14, 15),
            arrayOf(16, 17, 18, 19, 20),
            arrayOf(21, 22, 23, 24, 25),
        ))

        assertThat(grid.getStrideFrom(0, 0, Direction.LEFT)).isEmpty()
        assertThat(grid.getStrideFrom(0, 0, Direction.UP)).isEmpty()
        assertThat(grid.getStrideFrom(0, 0, Direction.UP_LEFT)).isEmpty()
        assertThat(grid.getStrideFrom(0, 0, Direction.UP_RIGHT)).isEmpty()
        assertThat(grid.getStrideFrom(0, 0, Direction.RIGHT)).isEqualTo(listOf(
            PointWithData(2, 1, 0),
            PointWithData(3, 2, 0),
            PointWithData(4, 3, 0),
            PointWithData(5, 4, 0),
        ))
        assertThat(grid.getStrideFrom(0, 0, Direction.DOWN_RIGHT)).isEqualTo(listOf(
            PointWithData(7, 1, 1),
            PointWithData(13, 2, 2),
            PointWithData(19, 3, 3),
            PointWithData(25, 4, 4),
        ))
        assertThat(grid.getStrideFrom(0, 0, Direction.DOWN)).isEqualTo(listOf(
            PointWithData(6, 0, 1),
            PointWithData(11, 0, 2),
            PointWithData(16, 0, 3),
            PointWithData(21, 0, 4),
        ))
    }

    @Test
    fun understandAngles() {
        println(sin(45.toRadians()))
        println(cos(45.toRadians()))
        println(PI / 4)
        println(sqrt(2.0) / 2.0)

        val eastDir = 1 toip 0
        val matrix = createRotationMatrix(90)
        val multiplied = matrix * eastDir

        println(multiplied)
        assertThat(multiplied.toIntPoint()).isEqualTo(0 toip 1)

        val wp1 = 3 toip -6
        val m1 = createRotationMatrix(180)
        val r1 = m1 * wp1

        println(r1)
        println(r1.toIntPoint())

        assertThat(r1.toIntPoint()).isEqualTo(-3 toip 6)
    }

    @Test
    fun day13Scrap() {
//        curr: 69, matches i: 13, interval: 41
//        curr: 1012, matches i: 13, interval: 41
//        curr: 1955, matches i: 13, interval: 41
//        curr: 2898, matches i: 13, interval: 41
//        curr: 3841, matches i: 13, interval: 41
//        curr: 4784, matches i: 13, interval: 41
//        curr: 5727, matches i: 13, interval: 41
//        curr: 6670, matches i: 13, interval: 41
//        curr: 7613, matches i: 13, interval: 41

        println(1012 - 69)
        println(1955 - 1012)
        println(2898 - 1955)

        // +13

        println("===69===")
        println(69 / 23)
        println((69 + 13) / 41)

        println("===1012===")
        println(1012 / 23)
        println((1012 + 13) / 41)

        println("===1955===")
        println(1955 / 23)
        println((1955 + 13) / 41)

        println("===2898===")
        println(2898 / 23)
        println((2898 + 13) / 41)

//        count: 571, curr: 539465, matches i: 23, interval: 733
//        count: 1304, curr: 1230684, matches i: 23, interval: 733
//        count: 2037, curr: 1921903, matches i: 23, interval: 733
//        count: 2770, curr: 2613122, matches i: 23, interval: 733
//        count: 3503, curr: 3304341, matches i: 23, interval: 733
//        count: 4236, curr: 3995560, matches i: 23, interval: 733

//        i: 52, curr: 21133177952
//        Largest i reached: 52, intervals[i]: 29, curr: 21133177952
//        i: 52, curr: 105303606801
//        i: 52, curr: 189474035650

        println("=======")
        println(21133177952L / 23)
        println(105303606801L / 23)
        println(189474035650L / 23)
        println("=======")

        println(8238001550L - 4578417687L)
        println(4578417687L - 918833824L)
        println("=======")

        println(21133177952L + 3659583863L * 23L)

        // Largest i reached: 52, intervals[i]: 29, curr: 21133177952
        println(21133177952L + 918833824L*23)

        println((42266355904 + 52) % 29)

    }

    @Test
    fun day13Scrap2() {
        println(539465 / 23)
        println(1230684 / 23)
    }



    @Test
    fun day14Scrap1() {
        println(8.toString(2))

        println(1 shl 0)
        println(1 shl 1)
        println(1 shl 2)
        println(1 shl 3)

        println("11X10X0X11X0X0010110XX01100X0110X011".length)

        // 0X1001X
        // 1100100
        println(100.toString(2))

        // 101010
        // X1001X
        println(42.toString(2))

//        println(expandNumbers("000000000000000000000000000000X1101X"))
//        println(expandNumbers("00000000000000000000000000000001X0XX"))

        // 000000000000000000000000000000101010
        // 000000000000000000000000000000101010
        println(42.toString(2).padStart(36, '0'))
    }
}

