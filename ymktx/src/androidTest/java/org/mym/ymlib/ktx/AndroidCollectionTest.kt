package org.mym.ymlib.ktx

import androidx.collection.CircularArray
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AndroidCollectionTest {

    @Test
    fun testIsNullOrEmptyForNullArray() {
        val arr: CircularArray<String>? = null
        Assert.assertEquals(true, arr.isNullOrEmpty())
    }

    @Test
    fun testIsNullOrEmptyForEmptyArray() {
        val arr: CircularArray<String>? = CircularArray()
        Assert.assertEquals(true, arr.isNullOrEmpty())
    }

    @Test
    fun testIsNullOrEmptyForNonEmptyArray() {
        val arr = CircularArray<String>()
        arr.addAll("test")
        Assert.assertEquals(false, arr.isNullOrEmpty())
    }

    @Test
    fun testAddAllForEmptyCollection() {
        val arr = CircularArray<String>()
        val source = listOf<String>()
        arr.addAll(source)
        Assert.assertEquals(0, arr.size())
    }

    @Test
    fun testAddAllForCollection() {
        val arr = CircularArray<String>()
        val source = listOf("x", "y")
        arr.addAll(source)
        Assert.assertEquals(2, arr.size())
    }

    @Test
    fun testAddAllForVarargs() {
        val arr = CircularArray<String>()
        arr.addAll("x", "y")
        Assert.assertEquals(2, arr.size())
    }
}