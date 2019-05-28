package org.mym.ymlib.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import org.mym.ymlib.widget.YmuiDividerItemDecoration.Companion.ALL
import org.mym.ymlib.widget.YmuiDividerItemDecoration.Companion.MIDDLE

/**
 * 可自由设置何种位置显示的 ItemDecoration。
 *
 * 这个类常用于列表最后一条 divider 的显示控制；但为通用起见，可控制所有位置的显示和隐藏。提供 [MIDDLE] 和 [ALL] 两种内置方案：
 *   * [ALL] 为默认，与系统自带的方案相同，会绘制最后一个 item。
 *   * [MIDDLE] 只显示中间位置，最后一条不会展示 decoration。
 *
 *
 * 此外，还可以在代码中动态访问和修改 [divider] 和 [orientation] 属性。
 */
class YmuiDividerItemDecoration(
    context: Context, orientation: Int,
    private val showDividers: (position: Int, childCount: Int) -> Boolean = ALL
) : RecyclerView.ItemDecoration() {

    companion object {
        const val HORIZONTAL = LinearLayout.HORIZONTAL
        const val VERTICAL = LinearLayout.VERTICAL

        private const val TAG = "DividerItemDecoration"
        private val ATTRS = intArrayOf(android.R.attr.listDivider)

        val MIDDLE: (Int, Int) -> Boolean = { position, childCount ->
            position < childCount - 1
        }

        val ALL: (Int, Int) -> Boolean = { position, childCount ->
            position < childCount
        }
    }

    /**
     * Typically should not be null.
     */
    var divider: Drawable? = null

    /**
     * Must be one of [HORIZONTAL] and [VERTICAL].
     */
    var orientation: Int = VERTICAL
        set(value) {
            if (value != HORIZONTAL && value != VERTICAL) {
                throw IllegalArgumentException("Invalid orientation. It should be either HORIZONTAL or VERTICAL")
            }
            field = value
        }
    private val mBounds = Rect()

    init {
        val a = context.obtainStyledAttributes(ATTRS)
        divider = a.getDrawable(0)
        if (divider == null) {
            Log.w(
                TAG, "@android:attr/listDivider was not set in the theme used for this DividerItemDecoration. " +
                        "Please set that attribute all call setDrawable()"
            )
        }
        a.recycle()
        this.orientation = orientation
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.layoutManager == null || divider == null) {
            return
        }
        when (orientation) {
            HORIZONTAL -> drawHorizontal(c, parent)
            VERTICAL -> drawVertical(c, parent)
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val localDivider = divider
        val position = parent.getChildAdapterPosition(view)
        val childCount = parent.childCount
        when {
            localDivider == null -> outRect.setEmpty()
            !showDividers(position, childCount) -> outRect.setEmpty()
            orientation == VERTICAL -> outRect.set(0, 0, 0, localDivider.intrinsicHeight)
            else -> outRect.set(0, 0, localDivider.intrinsicWidth, 0)
        }
    }

    private fun drawVertical(canvas: Canvas, parent: RecyclerView) {
        canvas.save()
        val left: Int
        val right: Int
        if (parent.clipToPadding) {
            left = parent.paddingLeft
            right = parent.width - parent.paddingRight
            canvas.clipRect(left, parent.paddingTop, right, parent.height - parent.paddingBottom)
        } else {
            left = 0
            right = parent.width
        }

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            if (showDividers(i, childCount)) {
                val child = parent.getChildAt(i)
                parent.getDecoratedBoundsWithMargins(child, mBounds)
                val bottom = mBounds.bottom + Math.round(child.translationY)
                val top = bottom - divider!!.intrinsicHeight
                divider!!.setBounds(left, top, right, bottom)
                divider!!.draw(canvas)
            }
        }
        canvas.restore()
    }

    private fun drawHorizontal(canvas: Canvas, parent: RecyclerView) {
        canvas.save()
        val top: Int
        val bottom: Int
        if (parent.clipToPadding) {
            top = parent.paddingTop
            bottom = parent.height - parent.paddingBottom
            canvas.clipRect(parent.paddingLeft, top, parent.width - parent.paddingRight, bottom)
        } else {
            top = 0
            bottom = parent.height
        }

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            if (showDividers(i, childCount)) {
                val child = parent.getChildAt(i)
                parent.getDecoratedBoundsWithMargins(child, mBounds)
                val right = mBounds.right + Math.round(child.translationX)
                val left = right - divider!!.intrinsicWidth
                divider!!.setBounds(left, top, right, bottom)
                divider!!.draw(canvas)
            }
        }
        canvas.restore()
    }
}