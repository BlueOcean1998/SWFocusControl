package net.sunniwell.app.focuscontrol.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.sunniwell.aar.focuscontrol.adapter.FocusControlAdapter
import net.sunniwell.app.focuscontrol.base.BaseViewHolder
import net.sunniwell.app.focuscontrol.databinding.AdapterTest1Binding

/**
 * 测试适配器1
 *
 * @author YSK
 * @date 2021-07-17
 */
class TestAdapter1(inline val stringList: ArrayList<String>) :
    FocusControlAdapter<TestAdapter1.ViewHolder>() {
    var onItemClick: (
        position: Int, v: View
    ) -> Unit = { _, _ -> }

    inner class ViewHolder(val viewBinding: AdapterTest1Binding) : BaseViewHolder(viewBinding)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val viewBinding = AdapterTest1Binding.inflate(layoutInflater, parent, false)
        val viewHolder = ViewHolder(viewBinding)
        viewBinding.btn2AdapterTest.run {
            setOnClickListener {
                val position = viewHolder.bindingAdapterPosition
                if (position in 0 until stringList.size) {
                    onItemClick(position, it)
                }
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val string = stringList[position]
        holder.viewBinding.btn1AdapterTest.text = string
    }

    override fun getItemCount() = stringList.size
}