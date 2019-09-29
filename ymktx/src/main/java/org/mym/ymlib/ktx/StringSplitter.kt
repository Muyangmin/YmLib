package org.mym.ymlib.ktx

import kotlin.math.max
import kotlin.math.min

/**
 * [joinToString] 方法的逆运算。将给定的字符串分隔并转换为特定类型。具体返回类型由 [transform] 指定。
 *
 * 如果只需要分割后的字符串列表，推荐直接使用 [splitToList] 方法。
 */
fun <R> String.splitToList(
    separator: String = ", ",
    prefix: String = "",
    postfix: String = "",
    truncated: String = "...",
    transform: ((String) -> R)
): List<R> {
    var buffer = this
    // 如果对空串执行 substringBefore/After 操作，会将末端的第一个字符删掉
    if (postfix.isNotEmpty() && buffer.endsWith(postfix)) {
        buffer = buffer.substringBeforeLast(postfix)
    }
    if (truncated.isNotEmpty() && buffer.endsWith(truncated)) {
        buffer = buffer.substringBeforeLast(truncated)
    }
    if (prefix.isNotEmpty() && buffer.startsWith(prefix)) {
        buffer = buffer.substringAfter(prefix)
    }
    return buffer.split(separator).map(transform)
}

/**
 * [joinToString] 方法的逆运算。将给定的字符串分隔并转换为字符串列表。
 */
fun String.splitToList(
    separator: String = ", ",
    prefix: String = "",
    postfix: String = "",
    truncated: String = "..."
) = splitToList(separator, prefix, postfix, truncated) { it }

/**
 * 将字符串按照固定位数分割为一个字符串列表，适合用于分隔手机号、银行卡号等数据。
 *
 * 该方法优先保证起始的子串长度为 [chunkLength]，即视觉上的左对齐效果。如需右对齐，请使用 [splitToChunkByFixedEnd]。
 */
fun String.splitToChunkByFixedStart(chunkLength: Int): List<String> {
    val source = this
    if (isNullOrEmpty()) {
        return emptyList()
    }
    val list = mutableListOf<String>()
    var index = 0
    while (index < source.length) {
        val end = min(index + chunkLength, source.length)
        list.add(source.substring(index, end))
        index = end
    }
    return list
}

/**
 * 将字符串按照固定位数分割为一个字符串列表，适合用于分隔手机号、银行卡号等数据。
 *
 * 该方法优先保证起始的子串长度为 [chunkLength]，即视觉上的右对齐效果。如需左对齐，请使用 [splitToChunkByFixedStart]。
 */
fun String.splitToChunkByFixedEnd(chunkLength: Int): List<String> {
    val source = this
    if (isNullOrEmpty()) {
        return emptyList()
    }
    val list = mutableListOf<String>()
    var index = source.length
    while (index > 0) {
        val start = max(index - chunkLength, 0)
        list.add(source.substring(start, index))
        index = start
    }
    return list.asReversed()
}