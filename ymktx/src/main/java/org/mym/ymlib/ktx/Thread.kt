package org.mym.ymlib.ktx

import android.os.Handler
import android.os.Looper

/**
 * 指定在主线程执行特定代码，并可以指定延时。
 *
 * @param[delayMillis] 需要延迟的毫秒数，默认为 0
 * @param[action] 要执行的具体操作。
 */
fun doOnMainThread(delayMillis: Long = 0, action: () -> Unit) {
    Handler(Looper.getMainLooper()).postDelayed(action, delayMillis)
}

/**
 * 指定在新线程内执行代码。
 */
fun doOnNewThread(action: () -> Unit) {
    Thread(action).start()
}