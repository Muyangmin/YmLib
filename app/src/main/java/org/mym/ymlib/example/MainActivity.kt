package org.mym.ymlib.example

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_shadow_test.view.*
import org.mym.ymlib.ktx.doOnMainThread
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main_btn_list.setOnClickListener {
            startActivity(Intent(this, ListShadowActivity::class.java))
        }

        main_btn_dynamic.setOnClickListener {
            startActivity(Intent(this, DynamicShadowActivity::class.java))
        }

        main_btn_simple_list.setOnClickListener {
            startActivity(Intent(this, SimpleListActivity::class.java))
        }

        main_btn_mutex.setOnClickListener {
            startActivity(Intent(this, MutexActivity::class.java))
        }
    }
}
