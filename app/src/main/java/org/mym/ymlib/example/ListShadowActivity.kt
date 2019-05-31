package org.mym.ymlib.example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.activity_list_shadow.*
import kotlinx.android.synthetic.main.item_shadow_test.view.*
import org.mym.ymlib.ktx.doOnMainThread
import kotlin.random.Random

class ListShadowActivity : AppCompatActivity(R.layout.activity_list_shadow) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        list_shadow_recycler.layoutManager = LinearLayoutManager(this)
        val adapter = ShadowTestAdapter()
        list_shadow_recycler.adapter = adapter

        doOnMainThread(30) {
            adapter.setNewData(generateData())
        }
    }

    private fun generateData(): List<SampleProfile> {
        val list = mutableListOf<SampleProfile>()
        @Suppress("SpellCheckingInspection")
        val lorem =
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam vitae sollicitudin felis. Donec lacinia eros ac arcu suscipit, porttitor convallis turpis porta. Fusce quis fringilla odio. Sed eleifend vel libero et blandit. Nam tempor sit amet sapien sit amet rhoncus. Quisque tempus feugiat nulla. Nullam tincidunt quis turpis sit amet euismod."
        val random = Random(System.currentTimeMillis())
        for (x in 1 until 20) {
            list.add(SampleProfile("Item $x", lorem.substring(0, random.nextInt(5, 100))))
        }
        return list
    }

    private data class SampleProfile(
        val name: String,
        val signature: String
    )

    private class ShadowTestAdapter : BaseQuickAdapter<SampleProfile, BaseViewHolder>(R.layout.item_shadow_test) {

        override fun convert(helper: BaseViewHolder, item: SampleProfile) {
            helper.itemView.apply {
                item_shadow_tv_title.text = item.name
                item_shadow_tv_random.text = item.signature
            }
        }
    }
}