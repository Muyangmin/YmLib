package org.mym.ymlib.ktx

import androidx.collection.CircularArray
import kotlin.contracts.contract

/**
 * 如果 [value] 不为 `null` 则加入该 kv 对；否则什么也不做。
 *
 * Put this k-v pair if [value] is not `null`; otherwise no-op.
 * @see [MutableMap.putIfAbsent]
 */
fun <K, V> MutableMap<K, V>.putIfValueNotNull(key: K, value: V?) {
    if (value != null) {
        this[key] = value
    }
}

/**
 * 判断这个 [CircularArray] 是否为 `null` 或空。
 *
 * Determine whether this circular array is null or empty.
 */
fun <E> CircularArray<E>?.isNullOrEmpty(): Boolean {
    contract {
        returns(false) implies (this@isNullOrEmpty != null)
    }
    return this == null || this.isEmpty
}

/**
 * 将所有元素依次添加到队列末尾。
 *
 * Add all elements in [c] at the end of this circular array.
 */
fun <E> CircularArray<E>.addAll(c: Collection<E>) {
    c.forEach {
        addLast(it)
    }
}


/**
 * 将所有参数依次添加到队列末尾。
 *
 * Add all arguments at the end of this circular array.
 */
fun <E> CircularArray<E>.addAll(vararg e: E) {
    e.forEach {
        addLast(it)
    }
}