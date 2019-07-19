package org.mym.ymlib.widget

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.StringRes
import java.lang.Exception
import java.lang.reflect.Field

/**
 * 这是一个对原生 Toast API 做的封装增强API，解决了一些已知问题。
 *
 * ## API 25 BadTokenException
 * * [方案一](https://github.com/drakeet/ToastCompat)：对 wms 做封装，在实际 addView 处做 try-catch （Google 后续版本方案），但代码量较大。
 * * [方案二](https://www.jianshu.com/p/e6f69182107d)：对 handler 做封装，需要使用反射 hook，但实现简单。
 *
 * 本实现采用方案二。
 *
 * ## 非主线程调用 Toast API，可能引发崩溃或不显示
 * 本实现在内部做了保护，确保最终的调用操作发生在主线程。
 */
object YmuiToast {
    private val impl: ToastCompatImpl by lazy {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1) {
            ToastCompatApi25Impl()
        } else {
            ToastCompatBaseImpl()
        }
    }

    fun showToast(context: Context, @StringRes msgId: Int, duration: Int = Toast.LENGTH_SHORT) {
        showToast(context, context.getString(msgId), duration)
    }

    fun showToast(context: Context, message: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
        runOnMainThread {
            impl.showToast(context, message, duration)
        }
    }

    private fun runOnMainThread(action: () -> Unit) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            action.invoke()
        } else {
            Handler(Looper.getMainLooper()).post { action.invoke() }
        }
    }
}

private interface ToastCompatImpl {
    fun showToast(context: Context, message: CharSequence, duration: Int)
}

private class ToastCompatBaseImpl : ToastCompatImpl {
    override fun showToast(context: Context, message: CharSequence, duration: Int) {
        Toast.makeText(context.applicationContext, message, duration).show()
    }
}

private class ToastCompatApi25Impl : ToastCompatImpl {
    private var fieldTN: Field? = null
    private var fieldTNHandler: Field? = null
    private var toast: Toast? = null

    init {
        try {
            fieldTN = Toast::class.java.getDeclaredField("mTN")
            fieldTNHandler = fieldTN!!.type.getDeclaredField("mHandler")
            fieldTN!!.isAccessible = true
            fieldTNHandler!!.isAccessible = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("ShowToast")
    override fun showToast(context: Context, message: CharSequence, duration: Int) {
        if (toast == null) {
            toast = Toast.makeText(context.applicationContext, message, duration)
            toast!!.hook()
        } else {
            toast!!.setText(message)
            toast!!.duration = duration
        }
        toast!!.show()
    }

    private fun Toast.hook() {
        try {
            val tn = fieldTN!!.get(this)
            val preHandler = fieldTNHandler!!.get(tn) as Handler
            fieldTNHandler!!.set(tn, GuardHandler(preHandler))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private class GuardHandler(private val actualHandler: Handler) : Handler() {

        //This is the core thing this guard does.
        override fun dispatchMessage(msg: Message?) {
            try {
                super.dispatchMessage(msg)
            } catch (e: WindowManager.BadTokenException) {
                //ignore
            }
        }

        override fun handleMessage(msg: Message?) {
            //delegate to original handler
            actualHandler.handleMessage(msg)
        }
    }
}