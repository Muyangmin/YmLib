package org.mym.ymlib.util

import android.view.View
import android.widget.Checkable

/**
 * 代表一个逻辑互斥的 `View` 组。适用于多个 View 中的一个与其他状态不同的场景。
 *
 * 示例用法：
 *
 * ```Kotlin
 * //v1, v2, v3 are views.
 * val mvg = mutexViewGroupOf(v1, v2, v3)
 *
 * //set v1 as enabled and others are disabled.
 * mvg.enable(v1)
 * //set v2 as disabled and others are enabled.
 * mvg.disable(v2)
 * //set v3 as selected and others not.
 * mvg.select(v3)
 * ```
 */
@Suppress("unused")
class MutexViewGroup<V : View>(private val views: Array<out V>) {

    fun enable(v: V) {
        executeCmdInternal(v, { isEnabled = true }) { isEnabled = false }
    }

    fun disable(v: V) {
        executeCmdInternal(v, { isEnabled = false }) { isEnabled = true }
    }

    fun select(v: V) {
        executeCmdInternal(v, { isSelected = true }) { isSelected = false }
    }

    fun unSelect(v: V) {
        executeCmdInternal(v, { isSelected = false }) { isSelected = true }
    }

    fun clickable(v: V) {
        executeCmdInternal(v, { isClickable = true }) { isClickable = false }
    }

    fun focusable(v: V) {
        executeCmdInternal(v, { isFocusable = true }) { isFocusable = false }
    }

    /**
     * 返回一个由组内元素组成的 [List]，列表元素顺序与传入构造函数时相同。
     */
    fun asList(): List<V> = views.toList()

    /**
     * 对 [target] 执行 [cmd]， 其他元素执行 [oppositeCmd]。
     */
    internal inline fun executeCmdInternal(target: V, cmd: V.() -> Unit, oppositeCmd: V .() -> Unit) {
        if (!views.contains(target)) {
            throw IllegalArgumentException("Target view is not in the group, please check again!")
        }
        cmd.invoke(target)

        views.forEach {
            if (it != target) {
                oppositeCmd.invoke(it)
            }
        }
    }
}

fun <V> MutexViewGroup<V>.check(v: V) where V : View, V : Checkable {
    executeCmdInternal(v, { isChecked = true }) { isChecked = false }
}

/**
 * 使用指定的 `View` 组成一个逻辑互斥组。
 */
fun <V : View> mutexViewGroupOf(vararg views: V): MutexViewGroup<V> = MutexViewGroup(views)