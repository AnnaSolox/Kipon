package android.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

object LiveDataTestUtils {
    fun <T> LiveData<T>.getOrAwaitValue(
        time: Long = 2,
        timeUnit: TimeUnit = TimeUnit.SECONDS
    ): T {
        var data: T? = null
        val latch = CountDownLatch(1)

        val observer = object : Observer<T> {
            override fun onChanged(value: T) {
                data = value
                latch.countDown()
                this@getOrAwaitValue.removeObserver(this)
            }
        }

        this.observeForever(observer)

        // Si no se recibe el valor en el tiempo esperado, lanza excepción
        if (!latch.await(time, timeUnit)) {
            this.removeObserver(observer)
            throw TimeoutException("LiveData value was never set.")
        }

        return data ?: throw NullPointerException("LiveData value was null")
    }

    fun <T> LiveData<List<T>>.getOrAwaitNonEmptyValue(
        time: Long = 2,
        timeUnit: TimeUnit = TimeUnit.SECONDS
    ): List<T> {
        var data: List<T>? = null
        val latch = CountDownLatch(1)

        val observer = object : Observer<List<T>> {
            override fun onChanged(value: List<T>) {
                if (value != null && value.isNotEmpty()) {
                    data = value
                    latch.countDown()
                    this@getOrAwaitNonEmptyValue.removeObserver(this)
                }
            }
        }

        this.observeForever(observer)

        if (!latch.await(time, timeUnit)) {
            this.removeObserver(observer)
            throw TimeoutException("LiveData value was never set to non-empty.")
        }

        return data ?: throw NullPointerException("LiveData value was null")
    }
}