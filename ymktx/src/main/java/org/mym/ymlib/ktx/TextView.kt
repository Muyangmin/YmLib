package org.mym.ymlib.ktx

import android.widget.EditText

/**
 * 等价于调用 `(this.)text.toString().trim()`，返回一个不为空的字符串。
 */
fun EditText.trimmedString(): String = text.toString().trim()