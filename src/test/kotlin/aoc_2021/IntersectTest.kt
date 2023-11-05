package aoc_2021

import intersectOrNull
import kotlin.test.Test
import kotlin.test.assertEquals

internal class IntersectTest {

    @Test
    fun intersect1() {
        assertEquals(
            7..8,
            intersectOrNull(0..8, 7..12)
        )
        assertEquals(
            7..8,
            intersectOrNull(7..12, 0..8)
        )

        assertEquals(
            8..8,
            intersectOrNull(0..8, 8..12)
        )
        assertEquals(
            null,
            intersectOrNull(0..8, 9..12)
        )

        assertEquals(
            1..7,
            intersectOrNull(0..8, 1..7)
        )
    }
}