package net.sunniwell.app.focuscontrol.base

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * 基础控件持有者
 *
 * @author YSK
 * @date 2022-04-14
 *
 * @param viewBinding ViewBinding
 */
abstract class BaseViewHolder(
    viewBinding: ViewBinding
) : RecyclerView.ViewHolder(viewBinding.root)