package org.mym.ymlib.ktx

import android.content.Context
import android.util.TypedValue
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DimensionTest {

    private lateinit var context: Context

    @Before
    fun setup() {
        context = InstrumentationRegistry.getInstrumentation().context
    }

    @Test
    fun testDpConvertWithZero() {
        testDimensionConvertInternal(TypedValue.COMPLEX_UNIT_DIP, 0F, 0F.dp)
    }

    @Test
    fun testDpConvertWithNonZero() {
        testDimensionConvertInternal(TypedValue.COMPLEX_UNIT_DIP, 10.toFloat(), 10.dp)
    }

    @Test
    fun testDpConvertWithNonZeroFloat() {
        testDimensionConvertInternal(TypedValue.COMPLEX_UNIT_DIP, 10.3F, 10.3F.dp)
    }

    @Test
    fun testSpConvertWithZero() {
        testDimensionConvertInternal(TypedValue.COMPLEX_UNIT_SP, 0F, 0F.sp)
    }

    @Test
    fun testSpConvertWithNonZero() {
        testDimensionConvertInternal(TypedValue.COMPLEX_UNIT_SP, 10.toFloat(), 10.sp)
    }

    @Test
    fun testSpConvertWithNonZeroFloat() {
        testDimensionConvertInternal(TypedValue.COMPLEX_UNIT_SP, 10.3F, 10.3F.sp)
    }

    private fun testDimensionConvertInternal(unit: Int, value: Float, computed: Float) {
        val displayMetrics = context.resources.displayMetrics
        val expected = TypedValue.applyDimension(unit, value, displayMetrics)
        Assert.assertEquals(expected, computed)
    }
}