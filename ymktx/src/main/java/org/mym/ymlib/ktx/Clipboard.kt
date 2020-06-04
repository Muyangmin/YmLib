package org.mym.ymlib.ktx

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast

/**
 * 将数据复制到系统剪贴板中。
 *
 * @param[toastText] 复制成功后的提示文本，如果为空则不会弹出 toast。
 * @param[clipDataBuilder] 构造待复制的数据。推荐使用 [ClipData.newPlainText] 系列方法。
 *
 * @since 0.7.0
 */
fun Context.copyToClipboard(
    toastText: String? = getString(R.string.ymktx_msg_content_copied),
    clipDataBuilder: () -> ClipData
) {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboard.setPrimaryClip(clipDataBuilder())
    if (!toastText.isNullOrEmpty()) {
        Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show()
    }
}