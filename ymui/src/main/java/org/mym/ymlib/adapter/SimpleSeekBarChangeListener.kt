package org.mym.ymlib.adapter

import android.widget.SeekBar

/**
 * Adapter class for [SeekBar.OnSeekBarChangeListener].
 */
class SimpleSeekBarChangeListener(
    private val onProgressChange: ((seekBar: SeekBar?, progress: Int, fromUser: Boolean) -> Unit)? = null,
    private val onStartTracking: ((seekBar: SeekBar?) -> Unit)? = null,
    private val onStopTracking: ((seekBar: SeekBar?) -> Unit)? = null
) : SeekBar.OnSeekBarChangeListener {
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        onProgressChange?.invoke(seekBar, progress, fromUser)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        onStartTracking?.invoke(seekBar)
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        onStopTracking?.invoke(seekBar)
    }
}