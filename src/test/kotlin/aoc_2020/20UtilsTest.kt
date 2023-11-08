package aoc_2020

import utils.hexColorRegex
import utils.splitIntStringPart
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class `20UtilsTest` {

    @Test
    fun splitIntStringPartTest() {
        assertEquals(1 to "in", "01in".splitIntStringPart())
        assertEquals(0 to "in", "0in".splitIntStringPart())
        assertEquals(177 to "in", "177in".splitIntStringPart())
        assertNull("177".splitIntStringPart())

        assertEquals(177 to " in", "177 in".splitIntStringPart())
        assertEquals(177 to "in", "177 in".splitIntStringPart(trimStringPart = true))
        assertEquals(6 to "dotted black bags", "6 dotted black bags".splitIntStringPart(trimStringPart = true))
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


}