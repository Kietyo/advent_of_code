import com.kietyo.ktruth.assertThat
import org.junit.jupiter.api.Test
import utils.Range
import utils.toip

internal class RangeTest {

    @Test
    fun createRange1() {
        assertThat(Range(1 toip 0, 0 toip 0).range).isEqualTo(2)
    }

    @Test
    fun createRange2a() {
        val r = Range(1 toip 0, 0 toip 0)
        assertThat(r.p1).isEqualTo(0 toip 0)
        assertThat(r.p2).isEqualTo(1 toip 0)
        assertThat(r.range).isEqualTo(2)
    }

    @Test
    fun createRange2b() {
        val r = Range(0 toip 0, 1 toip 0)
        assertThat(r.p1).isEqualTo(0 toip 0)
        assertThat(r.p2).isEqualTo(1 toip 0)
        assertThat(r.range).isEqualTo(2)
    }

    @Test
    fun createRange3a() {
        val r = Range(0 toip 1, 0 toip 0)
        assertThat(r.p1).isEqualTo(0 toip 0)
        assertThat(r.p2).isEqualTo(0 toip 1)
        assertThat(r.range).isEqualTo(2)
    }

    @Test
    fun createRange3b() {
        val r = Range(0 toip 0, 0 toip 1)
        assertThat(r.p1).isEqualTo(0 toip 0)
        assertThat(r.p2).isEqualTo(0 toip 1)
        assertThat(r.range).isEqualTo(2)
    }

    @Test
    fun rangeContains() {
        val r = Range(0 toip 0, 0 toip 1)
        assertThat(0 toip -1 in r).isFalse()
        assertThat(0 toip 0 in r).isTrue()
        assertThat(0 toip 1 in r).isTrue()
        assertThat(0 toip 2 in r).isFalse()

        assertThat(-1 toip -1 in r).isFalse()
        assertThat(-1 toip 0 in r).isFalse()
        assertThat(-1 toip 1 in r).isFalse()
        assertThat(-1 toip 2 in r).isFalse()

        assertThat(1 toip -1 in r).isFalse()
        assertThat(1 toip 0 in r).isFalse()
        assertThat(1 toip 1 in r).isFalse()
        assertThat(1 toip 2 in r).isFalse()
    }

    @Test
    fun intersectsWith() {
        // Point
        assertThat(Range(0 toip 0, 0 toip 0)
            .intersectsWith(Range(0 toip 0, 0 toip 0))).isTrue()

        // Vertical and vertical
        assertThat(Range(0 toip 0, 0 toip 4)
            .intersectsWith(Range(0 toip 0, 0 toip 4))).isTrue()
        assertThat(Range(0 toip 0, 0 toip 4)
            .intersectsWith(Range(0 toip 4, 0 toip 4))).isTrue()
        assertThat(Range(0 toip 0, 0 toip 4)
            .intersectsWith(Range(0 toip 5, 0 toip 6))).isFalse()
        assertThat(Range(0 toip 0, 0 toip 4)
            .intersectsWith(Range(0 toip 0, 0 toip 4))).isTrue()
        assertThat(Range(0 toip 4, 0 toip 4)
            .intersectsWith(Range(0 toip 0, 0 toip 4))).isTrue()
        assertThat(Range(0 toip 5, 0 toip 6)
            .intersectsWith(Range(0 toip 0, 0 toip 4))).isFalse()

        // Vertical and horizontal
        assertThat(Range(0 toip 0, 0 toip 4)
            .intersectsWith(Range(-2 toip 2, 2 toip 2))).isTrue()
        assertThat(Range(-2 toip 2, 2 toip 2)
            .intersectsWith(Range(0 toip 0, 0 toip 4))).isTrue()
        assertThat(Range(0 toip 0, 0 toip 4)
            .intersectsWith(Range(1 toip 2, 2 toip 2))).isFalse()
    }

    @Test
    fun isTouching() {
        // vertical line
        assertThat(Range(0 toip 0, 0 toip 4).isTouchingButNotIntersecting(
            Range(1 toip 0, 1 toip 4)
        )).isTrue()
        assertThat(Range(0 toip 0, 0 toip 4).isTouchingButNotIntersecting(
            Range(-1 toip 0, -1 toip 4)
        )).isTrue()

        assertThat(Range(1 toip 0, 1 toip 4).isTouchingButNotIntersecting(
            Range(0 toip 0, 0 toip 4)
        )).isTrue()
        assertThat(Range(-1 toip 0, -1 toip 4).isTouchingButNotIntersecting(
            Range(0 toip 0, 0 toip 4)
        )).isTrue()

        assertThat(Range(0 toip 0, 0 toip 4).isTouchingButNotIntersecting(
            Range(1 toip 0, 1 toip 0)
        )).isTrue()
        assertThat(Range(0 toip 0, 0 toip 4).isTouchingButNotIntersecting(
            Range(1 toip 0, 1 toip 1)
        )).isTrue()

        assertThat(Range(0 toip 0, 0 toip 4).isTouchingButNotIntersecting(
            Range(-1 toip 0, -1 toip 0)
        )).isTrue()

        assertThat(Range(0 toip 0, 0 toip 4).isTouchingButNotIntersecting(
            Range(-2 toip 0, -2 toip 0)
        )).isFalse()
        assertThat(Range(0 toip 0, 0 toip 4).isTouchingButNotIntersecting(
            Range(2 toip 0, 2 toip 0)
        )).isFalse()

        assertThat(Range(0 toip 0, 0 toip 4).isTouchingButNotIntersecting(
            Range(0 toip 0, 0 toip 4)
        )).isFalse()

        // horizontal line
        assertThat(Range(0 toip 0, 4 toip 0).isTouchingButNotIntersecting(
            Range(0 toip 1, 4 toip 1)
        )).isTrue()
        assertThat(Range(0 toip 0, 4 toip 0).isTouchingButNotIntersecting(
            Range(0 toip -1, 4 toip -1)
        )).isTrue()
    }
}