package org.mym.ymlib.ktx

import android.os.Looper
import org.junit.Assert
import org.junit.Test

class ThreadTest {

    @Test
    fun testDoOnMainThread() {
        doOnMainThread {
            Assert.assertEquals(true, Looper.myLooper() == Looper.getMainLooper())
        }
    }

    @Test
    fun testDoOnNewThread() {
        doOnNewThread {
            Assert.assertEquals(false, Looper.myLooper() == Looper.getMainLooper())
        }
    }
}