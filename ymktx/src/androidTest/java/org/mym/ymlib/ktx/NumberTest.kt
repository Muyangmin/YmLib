package org.mym.ymlib.ktx

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.math.BigDecimal
import java.math.BigInteger

@RunWith(AndroidJUnit4::class)
class NumberTest {

    @Test
    fun testNumberOrZeroWithIntNull() {
        val x: Int? = null
        Assert.assertEquals(0, x.orZero())
    }

    @Test
    fun testNumberOrZeroWithIntNonNullValue() {
        val x: Int? = 3
        Assert.assertEquals(3, x.orZero())
    }

    @Test
    fun testNumberOrZeroWithIntNonNullType() {
        val x = 5
        Assert.assertEquals(5, x.orZero())
    }

    @Test
    fun testNumberOrZeroWithLongNull() {
        val x: Long? = null
        Assert.assertEquals(0L, x.orZero())
    }

    @Test
    fun testNumberOrZeroWithLongNonNullValue() {
        val x: Long? = 3
        Assert.assertEquals(3L, x.orZero())
    }

    @Test
    fun testNumberOrZeroWithLongNonNullType() {
        val x = 5L
        Assert.assertEquals(5L, x.orZero())
    }

    @Test
    fun testNumberOrZeroWithDoubleNull() {
        val x: Double? = null
        assertDoubleEqualsWithDefaultDelta(0.0, x.orZero())
    }

    @Test
    fun testNumberOrZeroWithDoubleNonNullValue() {
        val x: Double? = 3.0
        assertDoubleEqualsWithDefaultDelta(3.0, x.orZero())
    }

    @Test
    fun testNumberOrZeroWithDoubleNonNullType() {
        val x = 5.0
        assertDoubleEqualsWithDefaultDelta(5.0, x.orZero())
    }

    @Test
    fun testNumberOrZeroWithBigIntegerNull() {
        val x: BigInteger? = null
        Assert.assertEquals(BigInteger.ZERO, x.orZero())
    }

    @Test
    fun testNumberOrZeroWithBigIntegerNonNullValue() {
        val x: BigInteger? = BigInteger.valueOf(3)
        Assert.assertEquals(BigInteger.valueOf(3), x.orZero())
    }

    @Test
    fun testNumberOrZeroWithBigIntegerNonNullType() {
        val x: BigInteger = BigInteger.valueOf(3)
        Assert.assertEquals(BigInteger.valueOf(3), x.orZero())
    }

    @Test
    fun testNumberOrZeroWithBigDecimalNull() {
        val x: BigDecimal? = null
        Assert.assertEquals(BigDecimal.ZERO, x.orZero())
    }

    @Test
    fun testNumberOrZeroWithBigDecimalNonNullValue() {
        val x: BigDecimal? = BigDecimal.valueOf(3)
        Assert.assertEquals(BigDecimal.valueOf(3), x.orZero())
    }

    @Test
    fun testNumberOrZeroWithBigDecimalNonNullType() {
        val x: BigDecimal = BigDecimal.valueOf(3)
        Assert.assertEquals(BigDecimal.valueOf(3), x.orZero())
    }

    @Test
    fun testStringToIntOrZeroWithNull() {
        val str: String? = null
        Assert.assertEquals(0, str.toIntOrZero())
    }

    @Test
    fun testStringToIntOrZeroWithEmpty() {
        val str: String? = ""
        Assert.assertEquals(0, str.toIntOrZero())
    }

    @Test
    fun testStringToIntOrZeroWithAcceptable() {
        val str: String? = "123"
        Assert.assertEquals(123, str.toIntOrZero())
    }

    @Test
    fun testStringToIntOrZeroWithMalFormed() {
        val str: String? = "xyz"
        Assert.assertEquals(0, str.toIntOrZero())
    }

    @Test
    fun testStringToIntOrZeroWithNegative() {
        val str: String? = "-2147483648"
        Assert.assertEquals(-2147483648, str.toIntOrZero())
    }

    @Test
    fun testStringToIntOrZeroWithOverflow() {
        val str: String? = "2147483648"
        Assert.assertEquals(0, str.toIntOrZero())
    }

    @Test
    fun testStringToDoubleOrZeroWithNull() {
        val str: String? = null
        assertDoubleEqualsWithDefaultDelta(0.0, str.toDoubleOrZero())
    }

    @Test
    fun testStringToDoubleOrZeroWithEmpty() {
        val str: String? = ""
        assertDoubleEqualsWithDefaultDelta(0.0, str.toDoubleOrZero())
    }

    @Test
    fun testStringToDoubleOrZeroWithAcceptable() {
        val str: String? = "123"
        assertDoubleEqualsWithDefaultDelta(123.0, str.toDoubleOrZero())
    }

    @Test
    fun testStringToDoubleOrZeroWithMalFormed() {
        val str: String? = "xyz"
        assertDoubleEqualsWithDefaultDelta(0.0, str.toDoubleOrZero())
    }

    @Test
    fun testStringToDoubleOrZeroWithNegative() {
        val str: String? = "-2147483648"
        assertDoubleEqualsWithDefaultDelta(-2147483648.0, str.toDoubleOrZero())
    }

    @Test
    fun testStringToDoubleOrZeroWithOverflow() {
        //这是整形的溢出值，但是 double 可以存储
        val str: String? = "2147483648"
        assertDoubleEqualsWithDefaultDelta(2147483648.0, str.toDoubleOrZero())
    }

    @Test
    fun testStringToBigIntegerOrZeroWithNull() {
        val str: String? = null
        Assert.assertEquals(BigInteger.ZERO, str.toBigIntegerOrZero())
    }

    @Test
    fun testStringToBigIntegerOrZeroWithEmpty() {
        val str: String? = ""
        Assert.assertEquals(BigInteger.ZERO, str.toBigIntegerOrZero())
    }

    @Test
    fun testStringToBigIntegerOrZeroWithAcceptable() {
        val str: String? = "233"
        Assert.assertEquals(BigInteger.valueOf(233), str.toBigIntegerOrZero())
    }

    @Test
    fun testStringToBigIntegerOrZeroWithMalformed() {
        val str: String? = "xyz"
        Assert.assertEquals(BigInteger.ZERO, str.toBigIntegerOrZero())
    }

    @Test
    fun testStringToBigIntegerOrZeroWithNegative() {
        val str: String? = "-1024"
        Assert.assertEquals(BigInteger.valueOf(-1024), str.toBigIntegerOrZero())
    }

    @Test
    fun testStringToBigDecimalOrZeroWithNull() {
        val str: String? = null
        Assert.assertEquals(BigDecimal.ZERO, str.toBigDecimalOrZero())
    }

    @Test
    fun testStringToBigDecimalOrZeroWithEmpty() {
        val str: String? = ""
        Assert.assertEquals(BigDecimal.ZERO, str.toBigDecimalOrZero())
    }

    @Test
    fun testStringToBigDecimalOrZeroWithAcceptable() {
        val str: String? = "233.0"
        Assert.assertEquals(BigDecimal.valueOf(233.0), str.toBigDecimalOrZero())
    }

    @Test
    fun testStringToBigDecimalOrZeroWithMalformed() {
        val str: String? = "xyz"
        Assert.assertEquals(BigDecimal.ZERO, str.toBigDecimalOrZero())
    }

    @Test
    fun testStringToBigDecimalOrZeroWithNegative() {
        val str: String? = "-1024"
        Assert.assertEquals(BigDecimal.valueOf(-1024), str.toBigDecimalOrZero())
    }

    @Test
    fun testDoubleIsPureIntWithPositiveInt() {
        val x = 3.0
        Assert.assertEquals(true, x.isPureInt())
    }

    @Test
    fun testDoubleIsPureIntWithPositiveDecimal() {
        val x = 3.05
        Assert.assertEquals(false, x.isPureInt())
    }

    @Test
    fun testDoubleIsPureIntWithNegativeInt() {
        val x = -3.0
        Assert.assertEquals(true, x.isPureInt())
    }

    @Test
    fun testDoubleIsPureIntWithNegativeDecimal() {
        val x = -3.05
        Assert.assertEquals(false, x.isPureInt())
    }

    @Test
    fun testDoubleIsPureIntWithPositiveZero() {
        val x = 0.0
        Assert.assertEquals(true, x.isPureInt())
    }

    @Test
    fun testDoubleIsPureIntWithNegativeZero() {
        val x: Double = -0.0000
        Assert.assertEquals(true, x.isPureInt())
    }

    @Test
    fun testBigDecimalIsPureIntWithPositiveInt() {
        val x = BigDecimal.valueOf(3.0)
        Assert.assertEquals(true, x.isPureInt())
    }

    @Test
    fun testBigDecimalIsPureIntWithPositiveDecimal() {
        val x = BigDecimal.valueOf(3.05)
        Assert.assertEquals(false, x.isPureInt())
    }

    @Test
    fun testBigDecimalIsPureIntWithNegativeInt() {
        val x = BigDecimal.valueOf(-3.0)
        Assert.assertEquals(true, x.isPureInt())
    }

    @Test
    fun testBigDecimalIsPureIntWithNegativeDecimal() {
        val x = BigDecimal.valueOf(-3.05)
        Assert.assertEquals(false, x.isPureInt())
    }

    @Test
    fun testBigDecimalIsPureIntWithZero() {
        val x = BigDecimal.ZERO
        Assert.assertEquals(true, x.isPureInt())
    }

    @Test
    fun testBigDecimalIsPureIntWithScalingZero() {
        val x = BigDecimal("0.0")
        Assert.assertEquals(true, x.isPureInt())
    }

    @Test
    fun testSumByBigIntegerWithEmptyCollection() {
        val c = emptyList<BigInteger>()
        Assert.assertEquals(BigInteger.ZERO, c.sumByBigInteger { it })
    }

    @Test
    fun testSumByBigIntegerWithBigIntegerCollection() {
        val c = listOf<BigInteger>(BigInteger.ONE, BigInteger.TEN)
        Assert.assertEquals(BigInteger.valueOf(11), c.sumByBigInteger { it })
    }

    @Test
    fun testSumByBigIntegerWithComplicatedDataStructure() {
        val c = listOf(
            "first" to BigInteger.ONE,
            "second" to BigInteger.TEN
        )
        Assert.assertEquals(BigInteger.valueOf(11), c.sumByBigInteger { it.second })
    }

    @Test
    fun testSumByBigDecimalWithEmptyCollection() {
        val c = emptyList<BigDecimal>()
        Assert.assertEquals(BigDecimal.ZERO, c.sumByBigDecimal { it })
    }

    @Test
    fun testSumByBigDecimalWithBigDecimalCollection() {
        val c = listOf<BigDecimal>(BigDecimal.ONE, BigDecimal.TEN)
        Assert.assertEquals(BigDecimal.valueOf(11), c.sumByBigDecimal { it })
    }

    @Test
    fun testSumByBigDecimalWithComplicatedDataStructure() {
        val c = listOf(
            "first" to BigDecimal.ONE,
            "second" to BigDecimal.TEN
        )
        Assert.assertEquals(BigDecimal.valueOf(11), c.sumByBigDecimal { it.second })
    }

    private fun assertDoubleEqualsWithDefaultDelta(expected: Double, actual: Double) =
        Assert.assertEquals(expected, actual, 0.0001)
}