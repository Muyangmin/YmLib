package org.mym.ymlib.ktx

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

/**
 * 等价于调用 `(this.)text.toString().trim()`，返回一个不为空的字符串。
 */
fun EditText.trimmedString(): String = text.toString().trim()

/**
 * 将光标移动到末尾。
 */
fun EditText.moveSelectionToEnd() {
    setSelection(text.length)
}

/**
 * 隐藏软键盘。
 */
fun View.hideSoftInput(flag: Int = 0) {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.hideSoftInputFromWindow(windowToken, flag)
}

/**
 * 隐藏软键盘。
 */
fun Activity.hideSoftInput(flag: Int = 0) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.hideSoftInputFromWindow(window?.decorView?.windowToken, flag)
}