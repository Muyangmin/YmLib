package org.mym.ymlib.example

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_dynamic_shadow.*
import org.mym.ymlib.adapter.SimpleSeekBarChangeListener
import org.mym.ymlib.widget.YmuiShadowLayout

/**
 * 动态修改 [YmuiShadowLayout] 的阴影效果。
 */
class DynamicShadowActivity : AppCompatActivity(R.layout.activity_dynamic_shadow) {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ds_seek_radius.setOnSeekBarChangeListener(SimpleSeekBarChangeListener(onProgressChange = { _, progress: Int, _ ->
            ds_tv_radius_value.text = "${progress}px"
            ds_shadow_layout.setShadowRadius(progress)
        }))

        ds_seek_dx.setOnSeekBarChangeListener(SimpleSeekBarChangeListener(onProgressChange = { _, progress: Int, _ ->
            ds_tv_dx_value.text = "${progress}px"
            ds_shadow_layout.setShadowDx(progress)
        }))

        ds_seek_dy.setOnSeekBarChangeListener(SimpleSeekBarChangeListener(onProgressChange = { _, progress: Int, _ ->
            ds_tv_dy_value.text = "${progress}px"
            ds_shadow_layout.setShadowDy(progress)
        }))


        arrayOf(ds_ckb_left, ds_ckb_top, ds_ckb_right, ds_ckb_bottom).forEach {
            it.setOnCheckedChangeListener { _, _ ->
                val left = if (ds_ckb_left.isChecked) YmuiShadowLayout.SIDE_LEFT else 0
                val top = if (ds_ckb_top.isChecked) YmuiShadowLayout.SIDE_TOP else 0
                val right = if (ds_ckb_right.isChecked) YmuiShadowLayout.SIDE_RIGHT else 0
                val bottom = if (ds_ckb_bottom.isChecked) YmuiShadowLayout.SIDE_BOTTOM else 0
                val flag = left or top or right or bottom
                ds_shadow_layout.setShadowSide(flag)
            }
        }

        (0 until ds_ll_color.childCount).forEach {
            val view = ds_ll_color.getChildAt(it) as TextView
            val colorInt = Color.parseColor(view.text.toString())
            view.setOnClickListener {
                ds_shadow_layout.setShadowColor(colorInt)
            }
        }
    }
}