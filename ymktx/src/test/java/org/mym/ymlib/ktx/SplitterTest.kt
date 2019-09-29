package org.mym.ymlib.ktx

import org.junit.Assert
import org.junit.Test

class SplitterTest {
    @Test
    fun testSplitEmptyString() = testSplitFixedStart("", "")

    @Test
    fun testSplitShortChunk() = testSplitFixedStart("1", "1")

    @Test
    fun testSplitFixedChunk() = testSplitFixedStart("1234", "1234")

    @Test
    fun testSplitMultiChunk() = testSplitFixedEnd("123456", "12 3456")

    @Test
    fun testSplitMultiChunkFixedStart() = testSplitFixedStart("123456", "1234 56")

    @Test
    fun testSplitSpaces() = testSplitFixedEnd("123  456", "123   456")

    @Test
    fun testSplitComplexString() =
        testSplitFixedEnd("ABC43658O9[-0)(**%^", "ABC 4365 8O9[ -0)( **%^")

    @Test
    fun testRealCaseBankCard() =
        testSplitFixedStart("621660310000****741", "6216 6031 0000 **** 741")

    @Test
    fun testSplitToListWithoutSeparator() =
        testSplitToListInternal("abc", listOf("abc"))

    @Test
    fun testSplitToListWithSeparator() =
        testSplitToListInternal("abc, def", listOf("abc", "def"))

    @Test
    fun testSplitToListWithMultipleSeparator() =
        testSplitToListInternal("abc, def, ghi, jklmn", listOf("abc", "def", "ghi", "jklmn"))

    @Test
    fun testSplitToListWithNonDefaultParams() =
        testSplitToListInternal(
            "--->abc|def, gh|ijk|...<---", listOf("abc", "def, gh", "ijk", ""),
            separator = "|", prefix = "--->", postfix = "<---", truncated = "..."
        )

    private fun testSplitFixedStart(source: String, expected: String) {
        Assert.assertEquals(expected, source.splitToChunkByFixedStart(4).joinToString(" "))
    }

    private fun testSplitFixedEnd(source: String, expected: String) {
        Assert.assertEquals(expected, source.splitToChunkByFixedEnd(4).joinToString(" "))
    }

    private fun testSplitToListInternal(
        source: String,
        expected: List<String>,
        separator: String = ", ",
        prefix: String = "",
        postfix: String = "",
        truncated: String = "..."
    ) {
        val list = source.splitToList(separator, prefix, postfix, truncated)
        Assert.assertEquals(expected, list)
    }
}