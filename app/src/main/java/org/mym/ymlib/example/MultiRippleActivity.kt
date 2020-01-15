package org.mym.ymlib.example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_multi_ripple.*

class MultiRippleActivity: AppCompatActivity(R.layout.activity_multi_ripple) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mr_btn_start.setOnClickListener {
            multi_ripple_view.startAnimation()
        }

        mr_btn_pause.setOnClickListener {
            multi_ripple_view.pauseAnimation()
        }

        mr_btn_resume.setOnClickListener {
            multi_ripple_view.resumeAnimation()
        }

        mr_btn_stop.setOnClickListener {
            multi_ripple_view.stopAnimation()
        }
    }
}