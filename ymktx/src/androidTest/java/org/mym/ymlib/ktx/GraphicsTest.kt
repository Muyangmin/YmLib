package org.mym.ymlib.ktx

import android.graphics.Rect
import android.graphics.RectF
import org.junit.Assert
import org.junit.Test

class GraphicsTest {

    @Test
    fun testSetIntValuesToRectF() {
        val rectF = RectF()
        rectF.set(2, 3, 4, 5)
        Assert.assertEquals(RectF(2.0F, 3.0F, 4.0F, 5.0F), rectF)
    }

    @Test
    fun testSetFloatValuesToRect() {
        val rect = Rect()
        rect.set(2.0F, 3.0F, 4.0F, 5.0F)
        Assert.assertEquals(Rect(2, 3, 4, 5), rect)
    }

    @Test
    fun testSetRectFtoRect() {
        val rectF = RectF(2.0F, 3.2F, 4.3F, 5.8F)
        val rect = Rect()
        rect.set(rectF)
        Assert.assertEquals(Rect(2, 3, 4, 5), rect)
    }
}