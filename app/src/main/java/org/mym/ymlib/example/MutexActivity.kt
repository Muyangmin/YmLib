package org.mym.ymlib.example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_mutex.*
import org.mym.ymlib.util.check
import org.mym.ymlib.util.mutexViewGroupOf

class MutexActivity : AppCompatActivity(R.layout.activity_mutex) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val optionGroup = mutexViewGroupOf(mutex_ckb_a, mutex_ckb_b, mutex_ckb_c)
        //默认选择
        optionGroup.check(mutex_ckb_a)
        mutex_tv_option_indicator.text = getString(R.string.mutex_option_hint, 1)

        optionGroup.asList().forEachIndexed { index, view ->
            view.setOnClickListener {
                mutex_tv_option_indicator.text = getString(R.string.mutex_option_hint, index + 1)
                optionGroup.check(view)
            }
        }

        val lineGroup = mutexViewGroupOf(mutex_ll_1, mutex_ll_2, mutex_ll_3, mutex_cs_4)
        //默认选择
        lineGroup.select(mutex_ll_1)
        mutex_tv_line_indicator.text = getString(R.string.mutex_line_hint, 1)
        lineGroup.asList().forEachIndexed { index, view ->
            view.setOnClickListener {
                mutex_tv_line_indicator.text = getString(R.string.mutex_line_hint, index + 1)
                lineGroup.select(view)
            }
        }
    }
}