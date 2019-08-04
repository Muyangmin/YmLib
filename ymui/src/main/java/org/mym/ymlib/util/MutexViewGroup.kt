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
 *
 * ### Event Hook
 * 如果只需要修改互斥组中的 View 的背景图、文字颜色等，在XML上使用 selector，然后调用相关方法即可完成。
 * 但如果需要修改诸如 textAppearance 之类无法使用 selector 完成的属性，则可以使用 [registerEventHook]，它相当于给互斥事件做额外的监听。
 */
@Suppress("unused")
class MutexViewGroup<V : View>(private val views: Array<out V>) {

    companion object {
        const val ACTION_ENABLE = "enable"
        const val ACTION_SELECT = "select"
        const val ACTION_CLICKABLE = "clickable"
        const val ACTION_FOCUSABLE = "focusable"
        const val ACTION_CHECK = "check"
    }

    private val actionMap: MutableMap<String, (V, Boolean) -> Unit> = mutableMapOf()

    init {
        actionMap[ACTION_ENABLE] = { v, value -> v.isEnabled = value }
        actionMap[ACTION_SELECT] = { v, value -> v.isSelected = value }
        actionMap[ACTION_CLICKABLE] = { v, value -> v.isClickable = value }
        actionMap[ACTION_FOCUSABLE] = { v, value -> v.isFocusable = value }
        actionMap[ACTION_CHECK] = { v, value -> (v as? Checkable)?.isChecked = value }
    }

    fun enable(v: V) {
        executeCmdInternal(v, ACTION_ENABLE, valueForTarget = true, valueForOthers = false)
    }

    fun disable(v: V) {
        executeCmdInternal(v, ACTION_ENABLE, valueForTarget = false, valueForOthers = true)
    }

    fun select(v: V) {
        executeCmdInternal(v, ACTION_SELECT, valueForTarget = true, valueForOthers = false)
    }

    fun unSelect(v: V) {
        executeCmdInternal(v, ACTION_SELECT, valueForTarget = false, valueForOthers = true)
    }

    fun clickable(v: V) {
        executeCmdInternal(v, ACTION_CLICKABLE, valueForTarget = true, valueForOthers = false)
    }

    fun focusable(v: V) {
        executeCmdInternal(v, ACTION_FOCUSABLE, valueForTarget = true, valueForOthers = false)
    }

    /**
     * 返回一个由组内元素组成的 [List]，列表元素顺序与传入构造函数时相同。
     */
    fun asList(): List<V> = views.toList()

    /**
     * 为指定事件注册监听。重复添加监听可能导致代码重复执行。
     */
    fun registerEventHook(event: String, doOnEvent: (V, Boolean) -> Unit) {
        val originalAction = actionMap[event] ?: return
        //Wrap original action and hooks
        actionMap[event] = { view, value ->
            originalAction.invoke(view, value)
            doOnEvent(view, value)
        }
    }

    /**
     * 向 [target] 的 [event] 事件对应函数传递 [valueForTarget]， 其他元素传递 [valueForOthers]。
     */
    internal fun executeCmdInternal(target: V, event: String, valueForTarget: Boolean, valueForOthers: Boolean) {
        if (!views.contains(target)) {
            throw IllegalArgumentException("Target view is not in the group, please check again!")
        }
        val action = actionMap[event] ?: return
        action.invoke(target, valueForTarget)

        views.forEach {
            if (it != target) {
                action.invoke(it, valueForOthers)
            }
        }
    }
}

fun <V> MutexViewGroup<V>.check(v: V) where V : View, V : Checkable {
    executeCmdInternal(v, MutexViewGroup.ACTION_CHECK, valueForTarget = true, valueForOthers = false)
}

/**
 * 使用指定的 `View` 组成一个逻辑互斥组。
 */
fun <V : View> mutexViewGroupOf(vararg views: V): MutexViewGroup<V> = MutexViewGroup(views)