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

        arr.add(123)

        assertThat(arr.size).isEqualTo(1)
        assertThat(arr.removeFirst()).isEqualTo(123)
        assertThat(arr.size).isEqualTo(0)
    }

    @Test
    fun createArray2() {
        val arr = CircularIntArray(2)

        assertThat(arr.maxSize).isEqualTo(2)
        assertThat(arr.size).isEqualTo(0)

        arr.add(11)
        assertThat(arr.size).isEqualTo(1)
        arr.add(22)
        assertThat(arr.size).isEqualTo(2)

        testAssertFails<UnsupportedOperationException> {
            arr.add(33)
        }
        assertThat(arr.size).isEqualTo(2)

        assertThat(arr.removeFirst()).isEqualTo(11)
        assertThat(arr.size).isEqualTo(1)

        assertThat(arr.removeFirst()).isEqualTo(22)
        assertThat(arr.size).isEqualTo(0)

        arr.add(33)
        assertThat(arr.size).isEqualTo(1)
        arr.add(44)
        assertThat(arr.size).isEqualTo(2)

        arr.clear()
        assertThat(arr.size).isEqualTo(0)
        testAssertFails<NoSuchElementException> {
            arr.removeFirst()
        }
    }
}