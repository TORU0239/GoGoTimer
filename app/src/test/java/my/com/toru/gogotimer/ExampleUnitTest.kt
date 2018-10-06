package my.com.toru.gogotimer

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun calculateTimeInSecond(){
        val hour = 1
        val minute = 10
        val second = 10
        assertNotEquals((hour * 3600) + (minute * 60) + second, 4)
    }
}