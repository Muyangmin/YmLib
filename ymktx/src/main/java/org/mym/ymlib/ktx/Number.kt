@file:Suppress("unused")

package org.mym.ymlib.ktx

import java.math.BigDecimal
import java.math.BigInteger

fun Int?.orZero(): Int = this ?: 0
fun Long?.orZero(): Long = this ?: 0
fun Double?.orZero(): Double = this ?: 0.0
fun BigInteger?.orZero(): BigInteger = this ?: BigInteger.ZERO
fun BigDecimal?.orZero(): BigDecimal = this ?: BigDecimal.ZERO

/**
 * 将当前字符串转换为 Int，如果转换不成功则返回 `0`.
 */
fun String?.toIntOrZero(): Int = this?.toIntOrNull() ?: 0

/**
 * 将当前字符串转换为 Double，如果转换不成功则返回 `0.0`.
 */
fun String?.toDoubleOrZero(): Double = this?.toDoubleOrNull() ?: 0.0

/**
 * 将当前字符串转换为 BigInteger，如果转换不成功则返回 `0`.
 */
fun String?.toBigIntegerOrZero(): BigInteger {
    return if (this.isNullOrEmpty()) {
        BigInteger.ZERO
    } else try {
        BigInteger(this)
    } catch (e: NumberFormatException) {
        BigInteger.ZERO
    }
}

/**
 * 将当前字符串转换为 BigDecimal，如果转换不成功则返回 `0`.
 */
fun String?.toBigDecimalOrZero(): BigDecimal {
    return if (this.isNullOrEmpty()) {
        BigDecimal.ZERO
    } else try {
        BigDecimal(this)
    } catch (e: NumberFormatException) {
        BigDecimal.ZERO
    }
}

/**
 * 判断当前 [Double] 对象是否为通常意义上的整数。注意，`-0.0` 也会被视为整数。
 */
fun Double.isPureInt(): Boolean {
    return this.compareTo(this.toInt()) == 0 || this.compareTo(-0.0) == 0
}

/**
 * 判断当前 [BigDecimal] 对象是否为通常意义上的整数。
 */
fun BigDecimal.isPureInt(): Boolean {
    //Java 8b100 以前的版本存在 bug, 对零做 stripTrailingZeros 会得到原始的 scale（实际上应该为0）
    // 参见：1) https://bugs.java.com/bugdatabase/view_bug.do?bug_id=6480539
    // 2) https://stackoverflow.com/a/34414905/4944176
    //例如 (0.0).stripTrailingZeros().scale() = 1
    //为了保证兼容性，虽然 8b100 修复了这个问题，但仍然使用旧的 workaround
    return this.stripTrailingZeros().scale() <= 0 || this.compareTo(BigDecimal.ZERO) == 0
}

/**
 * Returns the sum of all values produced by [selector] function applied to each element in the collection.
 */
inline fun <T> Iterable<T>.sumByBigDecimal(selector: (T) -> BigDecimal): BigDecimal {
    var sum: BigDecimal = BigDecimal.ZERO
    for (element in this) {
        sum += selector(element)
    }
    return sum
}

/**
 * Returns the sum of all values produced by [selector] function applied to each element in the collection.
 */
inline fun <T> Iterable<T>.sumByBigInteger(selector: (T) -> BigInteger): BigInteger {
    var sum: BigInteger = BigInteger.ZERO
    for (element in this) {
        sum += selector(element)
    }
    return sum
}