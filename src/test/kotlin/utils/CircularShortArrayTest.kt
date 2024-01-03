package utils

import com.kietyo.ktruth.assertThat
import com.kietyo.ktruth.testAssertFails
import kotlin.test.Test
import kotlin.test.assertFails

internal class CircularShortArrayTest {

    @Test
    fun createArray() {
        val arr = CircularIntArray(64)

        assertThat(arr.maxSize).isEqualTo(64)
        assertThat(arr.size).isEqualTo(0)

        testAssertFails<NoSuchElementException> {
            arr.removeFirst()
        }
    }
}