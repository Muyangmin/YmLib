package org.mym.ymlib.annotation

/**
 * 用于标记某些元素（主要是方法）是有序的。
 *
 * 按照惯例，顺序应当为正整数并按照自然数顺序递增。
 *
 * 例如 order 为 1 的元素应当优先于 order 为 2 的元素。
 *
 * @param[order] 该元素的处理顺序，默认为 [Int.MAX_VALUE]。
 */
annotation class Ordered(val order: Int = Int.MAX_VALUE)