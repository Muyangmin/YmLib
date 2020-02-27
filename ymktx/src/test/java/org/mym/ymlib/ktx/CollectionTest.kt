package org.mym.ymlib.ktx

import org.junit.Assert
import org.junit.Test

class CollectionTest {

    @Test
    fun testPutIfNull() {
        val map = mutableMapOf("abc" to "def")
        map.putIfValueNotNull("xyz", null)

        Assert.assertEquals(1, map.size)
    }

    @Test
    fun testPutIfNotNull() {
        val map = mutableMapOf("abc" to "def")
        map.putIfValueNotNull("xyz", "aaa")

        Assert.assertEquals(2, map.size)
    }

    @Test
    fun testNonNullValueMapCase1() {
        val map = mapOfNonNullValues<String, Any>(
            "abc" to "def",
            "def" to null
        )
        Assert.assertEquals(1, map.size)
    }

    @Test
    fun testNonNullValueMapCase2() {
        val map = mapOfNonNullValues(
            "abc" to "def",
            "def" to 123
        )
        Assert.assertEquals(2, map.size)
    }
}