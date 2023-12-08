import com.kietyo.ktruth.assertThat
import utils.ind
import utils.splitByPredicate
import utils.splitByPredicateIndexed
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
}

