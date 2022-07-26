package net.sunniwell.aar.focuscontrol.adapter

import androidx.recyclerview.widget.RecyclerView

/**
 * 焦点控制Adapter
 *
 * @author YSK
 * @date 2021-08-04
 *
 * @param VH ViewHolder
 */
abstract class FocusControlAdapter<VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {
    init {
        setHasStableIds(true)
    }

    final override fun setHasStableIds(hasStableIds: Boolean) = super.setHasStableIds(true)
    final override fun getItemId(position: Int) = position.toLong()
}