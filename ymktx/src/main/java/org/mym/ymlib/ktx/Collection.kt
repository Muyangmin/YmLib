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
 * 类似于标准库的 mapOf 方法，但会自动过滤掉为 `null` 的 kv 对。
 */
fun <K, V> mapOfNonNullValues(vararg pairs: Pair<K, V?>): Map<K, V> {
    val map = mutableMapOf<K, V>()
    for (pair in pairs) {
        map.putIfValueNotNull(pair.first, pair.second)
    }
    return map
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

/**
 * 构造一个列表，内容为给定的元素重复 [times] 次。
 *
 * @since 0.8.0
 */
fun <T> repeatElement(element: T, times: Int): List<T> {
    val mutableList = mutableListOf<T>()
    repeat(times) {
        mutableList.add(element)
    }
    return mutableList
}

// <editor-fold default-state="collapsed" desc="解构辅助方法">
operator fun <T> List<T>.component6() = this[5]
operator fun <T> List<T>.component7() = this[6]
operator fun <T> List<T>.component8() = this[7]
operator fun <T> List<T>.component9() = this[8]
operator fun <T> List<T>.component10() = this[9]
operator fun <T> List<T>.component11() = this[10]
operator fun <T> List<T>.component12() = this[11]
operator fun <T> List<T>.component13() = this[12]
operator fun <T> List<T>.component14() = this[13]
operator fun <T> List<T>.component15() = this[14]
// </editor-fold>

/**
 * 尝试取出列表中唯一一个元素。如果列表元素不足或超过1个，则抛出一个 [IllegalStateException]。
 *
 * 作为对比，标准库的 [first] 函数将在列表为空时抛出 NPE，且在列表多于 1 个元素时不会抛出异常。
 *
 * @since 0.8.0
 */
fun <E> List<E>.takeTheOnlyElement(): E {
    check(size == 1) {
        "Expected a list with only one element, but got a size ${size}."
    }
    return this[0]
}

/**
 * 将列表按照指定的属性排序。
 *
 * 与标准库的 [sortBy] 不同，这里不要求 [K] 是 [Comparable] 的实现类，因此需要手动指定 comparator。
 *
 * @param[E] 列表元素类型。
 * @param[K] 排序属性类型。
 *
 * @since 0.8.0
 */
fun <E, K> MutableList<E>.sortWith(comparator: Comparator<K>, selector: (E) -> K) {
    sortWith(Comparator { o1: E, o2: E ->
        comparator.compare(selector(o1), selector(o2))
    })
}

/**
 * 返回一个按照指定属性排序的列表。
 *
 * 与标准库的 [sortedBy] 不同，这里不要求 [K] 是 [Comparable] 的实现类，因此需要手动指定 comparator。
 *
 * @param[E] 列表元素类型。
 * @param[K] 排序属性类型。
 *
 * @since 0.8.0
 */
fun <E, K> Iterable<E>.sortedWith(comparator: Comparator<K>, selector: (E) -> K): List<E> {
    return sortedWith(Comparator { o1: E, o2: E ->
        comparator.compare(selector(o1), selector(o2))
    })
}