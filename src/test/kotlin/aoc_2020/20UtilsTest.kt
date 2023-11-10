package aoc_2020

import com.kietyo.ktruth.assertThat
import utils.Direction
import utils.MutableGrid
import utils.PointWithData
import utils.hexColorRegex
import utils.splitIntStringPartsOrNull
import utils.splitStringIntPartsOrNull
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
}