package org.mym.ymlib.ktx

/**
 * 如果 [value] 不为 `null` 则加入该 kv 对；否则什么也不做。
 * @see [MutableMap.putIfAbsent]
 */
fun <K, V> MutableMap<K, V>.putIfValueNotNull(key: K, value: V?) {
    if (value != null) {
        this[key] = value
    }
}