package org.mym.ymlib.compiler.util

import java.util.*

/**
 * 尝试获取某个驼峰字符串的最后一个单词。
 */
fun String.lastWord(): String {
    val startIndex = indexOfLast { it.isUpperCase() }
    //如果是单词且全小写，则直接全词返回
    return if (startIndex < 0) {
        this
    } else {
        substring(startIndex, startIndex + 1).toLowerCase(Locale.getDefault()) +
                substring(startIndex + 1)
    }
}