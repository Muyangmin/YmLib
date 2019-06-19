package org.mym.ymlib.example

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.activity_simple_list.*
import kotlinx.android.synthetic.main.item_simple_list.view.*
import org.mym.ymlib.widget.YmuiDividerItemDecoration

class SimpleListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_list)

        simple_list_recycler.layoutManager = LinearLayoutManager(this)
        val adapter = SimpleListAdapter()
        simple_list_recycler.adapter = adapter
        simple_list_recycler.addItemDecoration(YmuiDividerItemDecoration(
            this,
            LinearLayoutManager.VERTICAL
        ) { position: Int, childCount: Int ->
            val draw = position != 2 && position != 5 && (position != childCount - 1)
            Log.v("SimpleListActivity", " position=$position, childCount=$childCount, draw?$draw")
            draw
        })

        val list = (0 until 20).map { "Item $it" }
        adapter.setNewData(list)
    }

    class SimpleListAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_simple_list) {
        override fun convert(helper: BaseViewHolder, item: String) {
            helper.itemView.item_simple_list_tv.text = item
        }
    }
}