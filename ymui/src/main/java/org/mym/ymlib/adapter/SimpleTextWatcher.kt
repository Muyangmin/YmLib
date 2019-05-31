package org.mym.ymlib.adapter

import android.text.Editable
import android.text.TextWatcher

/**
 * Adapter class for [TextWatcher].
 */
class SimpleTextWatcher(
    private val afterChanged: ((Editable?) -> Unit)? = null,
    private val beforeChanged: ((s: CharSequence?, start: Int, count: Int, after: Int) -> Unit)? = null,
    private val onChanged: ((s: CharSequence?, start: Int, before: Int, count: Int) -> Unit)? = null
) : TextWatcher {
    override fun afterTextChanged(s: Editable?) {
        afterChanged?.invoke(s)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        beforeChanged?.invoke(s, start, count, after)
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        onChanged?.invoke(s, start, before, count)
    }
}