package org.mym.ymlib.ktx

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.json.JSONArray
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class JsonTest {

    @Test
    fun testMapToJsonWithStringOnly() {
        testToJsonStringInternal(
            mapOf("abc" to "def"), """
                {"abc":"def"}
            """
        )
    }

    @Test
    fun testMapToJsonWithMultipleItems() {
        testToJsonStringInternal(
            mapOf("abc" to "def", "k2" to "v2"), """
                {"abc":"def","k2":"v2"}
            """
        )
    }

    @Test
    fun testMapToJsonWithNullValue() {
        testToJsonStringInternal(
            mapOf("abc" to "def", "nk" to null), """
                {"abc":"def","nk":null}
            """
        )
    }

    @Test
    fun testMapToJsonWithMixedType() {
        testToJsonStringInternal(
            mapOf("StrKey" to "abc", "intKey" to 123), """
            {"StrKey":"abc","intKey":123}
            """
        )
    }

    @Test
    fun testMapToJsonWithDoubleNumbers() {
        testToJsonStringInternal(
            mapOf("value" to 123.45), """
                {"value":123.45}
            """
        )
    }

    private fun testToJsonStringInternal(map: Map<*, *>, expectedString: String) {
        Assert.assertEquals(expectedString.trimIndent(), map.toJsonString())
    }

    @Test
    fun testCollectionToJsonArray() {
        val list = listOf(1, 2, 3, 4, 5)
        val array = JSONArray(list)
        Assert.assertEquals(array, list.toJSONArray())
    }
}