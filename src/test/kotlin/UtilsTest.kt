import com.kietyo.ktruth.assertThat
import utils.ind
import utils.intersectRangeOrNull
import utils.intersectsWith
import utils.splitByPredicate
import utils.splitByPredicateIndexed
import utils.subtractRange
import kotlin.test.Test

internal class UtilsTest {

    @Test
    fun splitByPredicate() {
        val res = "11.123..11".splitByPredicate { !it.isDigit() }

        println(res)

        assertThat(res).isEqualTo(listOf("11", ".", "123", ".", ".", "11"))
    }

    @Test
    fun splitByPredicateIndexed() {
        val res = "11.123..11".splitByPredicateIndexed { !it.isDigit() }

        println(res)

        assertThat(res).isEqualTo(listOf(
            0 ind "11",
            2 ind ".",
            3 ind "123",
            6 ind ".",
            7 ind ".",
            8 ind "11"))
    }

    @Test
    fun longIntersectsWith() {
        assertThat((0L..5L).intersectsWith(5L..7L)).isTrue()
        assertThat((0L..5L).intersectsWith(4L..7L)).isTrue()
        assertThat((0L..5L).intersectsWith(6L..7L)).isFalse()

        assertThat((5L..7L).intersectsWith(0L..5L)).isTrue()
        assertThat((4L..7L).intersectsWith(0L..5L)).isTrue()
        assertThat((6L..7L).intersectsWith(0L..5L)).isFalse()
    }

    @Test
    fun longIntersectRangeOrNull() {
        assertThat((0L..5L).intersectRangeOrNull(5L..7L)).isEqualTo(5L..5L)
        assertThat((0L..5L).intersectRangeOrNull(4L..7L)).isEqualTo(4L..5L)
        assertThat((0L..5L).intersectRangeOrNull(6L..7L)).isNull()

        assertThat((5L..7L).intersectRangeOrNull(0L..5L)).isEqualTo(5L..5L)
        assertThat((4L..7L).intersectRangeOrNull(0L..5L)).isEqualTo(4L..5L)
        assertThat((6L..7L).intersectRangeOrNull(0L..5L)).isNull()
    }

    @Test
    fun longRangeSubtractRange() {
        assertThat((10L..40L).subtractRange(50L..60L)).isEqualTo(
            listOf(
                10L..40L
            )
        )
        assertThat((10L..40L).subtractRange(20L..30L)).isEqualTo(
            listOf(
                10L..19L, 31L..40L
            )
        )
        assertThat((10L..40L).subtractRange(10L..30L)).isEqualTo(
            listOf(
                31L..40L
            )
        )
        assertThat((10L..40L).subtractRange(30L..40L)).isEqualTo(
            listOf(
                10L..29L
            )
        )

        assertThat((10L..40L).subtractRange(40L..40L)).isEqualTo(
            listOf(
                10L..39L
            )
        )
        assertThat((10L..40L).subtractRange(10L..10L)).isEqualTo(
            listOf(
                11L..40L
            )
        )
        assertThat((10L..40L).subtractRange(30L..30L)).isEqualTo(
            listOf(
                10L..29L, 31L..40L
            )
        )

        assertThat((10L..40L).subtractRange(10L..40L)).isEmpty()
        assertThat((10L..40L).subtractRange(0L..50L)).isEmpty()
    }

    @Test
    fun calculateLeastCommonMultiple() {
        assertThat(utils.calculateLeastCommonMultiple(
            listOf(6)
        )).isEqualTo(6L)
        assertThat(utils.calculateLeastCommonMultiple(
            listOf(6, 8)
        )).isEqualTo(24L)
        assertThat(utils.calculateLeastCommonMultiple(
            listOf(16043, 20777, 13939, 18673, 11309, 17621)
        )).isEqualTo(13740108158591L)
    }
}

