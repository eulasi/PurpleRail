package com.example.weatherapp.utilities

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

object LiveDataTestUtil {
    fun <T> getValue(liveData: LiveData<T>): T {
        val data = mutableListOf<T>()
        val latch = CountDownLatch(1)
        val observer = Observer<T> { value ->
            data.add(value)
            latch.countDown()
        }
        liveData.observeForever(observer)
        latch.await(2, TimeUnit.SECONDS)
        liveData.removeObserver(observer)
        return data[0]
    }
}
