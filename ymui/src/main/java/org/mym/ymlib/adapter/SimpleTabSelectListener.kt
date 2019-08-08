package org.mym.ymlib.adapter

import com.google.android.material.tabs.TabLayout

/**
 * Adapter class for [TabLayout.OnTabSelectedListener].
 */
class SimpleTabSelectListener(
    private val onTabReselected: ((tab: TabLayout.Tab?) -> Unit)? = null,
    private val onTabUnselected: ((tab: TabLayout.Tab?) -> Unit)? = null,
    private val onTabSelected: ((tab: TabLayout.Tab?) -> Unit)? = null
) : TabLayout.OnTabSelectedListener {
    override fun onTabReselected(tab: TabLayout.Tab?) {
        onTabReselected?.invoke(tab)
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
        onTabUnselected?.invoke(tab)
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        onTabSelected?.invoke(tab)
    }
}