package net.sunniwell.app.focuscontrol.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import net.sunniwell.app.focuscontrol.base.BaseFragment
import net.sunniwell.app.focuscontrol.databinding.FragmentTest1Binding

/**
 * 测试碎片1
 *
 * @author YSK
 * @date 2021-07-13
 */
class TestFragment1 : BaseFragment<FragmentTest1Binding>() {
    override fun bindView(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentTest1Binding.inflate(inflater, container, false)
}