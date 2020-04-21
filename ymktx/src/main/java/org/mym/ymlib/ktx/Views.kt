package org.mym.ymlib.ktx

import android.view.View

/**
 * 将参数 view 的可见性全部设置为 [View.VISIBLE]。
 *
 * @since 0.7.0
 */
fun makeViewsVisible(vararg view: View) = view.setAllVisibility(View.VISIBLE)

/**
 * 将参数 view 的可见性全部设置为 [View.INVISIBLE]。
 *
 * @since 0.7.0
 */
fun makeViewsInvisible(vararg view: View) = view.setAllVisibility(View.INVISIBLE)

/**
 * 将参数 view 的可见性全部设置为 [View.GONE]。
 *
 * @since 0.7.0
 */
fun makeViewsGone(vararg view: View) = view.setAllVisibility(View.GONE)

/**
 * 批量修改 View 的可见性。
 *
 * @since 0.7.0
 */
fun Collection<View>.setAllVisibility(visibility: Int) = forEach {
    it.visibility = visibility
}

/**
 * 批量修改 View 的可见性。
 *
 * @since 0.7.0
 */
fun Array<out View>.setAllVisibility(visibility: Int) = forEach {
    it.visibility = visibility
}

/**
 * 根据 [predicate] 来判断应当为此视图设置何种可见性。
 *
 * @param[predicate] 判断条件。该函数的执行不应该产生 side effect.
 * @param[otherwise] 条件不成立时应设置为何种可见性，默认为 [View.GONE]。
 */
inline fun View.visibleIf(otherwise: Int = View.GONE, predicate: () -> Boolean) {
    visibility = if (predicate.invoke()) {
        View.VISIBLE
    } else {
        otherwise
    }
}

/**
 * 设置带有时间限流（即防重复）功能的点击监听。
 *
 * @param[throttleMillis] 允许点击的最小时间间隔。
 * @param[listener] 原始点击逻辑。
 *
 * @since 0.7.0
 */
fun View.setThrottledOnClickListener(throttleMillis: Long, listener: (View?) -> Unit) {
    val wrapped = object : View.OnClickListener {
        var lastClick: Long = 0

        override fun onClick(v: View?) {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastClick > throttleMillis) {
                lastClick = currentTime
                listener(v)
            }
        }
    }
    setOnClickListener(wrapped)
}