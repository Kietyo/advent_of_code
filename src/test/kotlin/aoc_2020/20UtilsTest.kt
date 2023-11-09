package aoc_2020

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


}