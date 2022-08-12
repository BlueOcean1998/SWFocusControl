package net.sunniwell.app.focuscontrol.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import net.sunniwell.app.focuscontrol.base.BaseFragment
import net.sunniwell.app.focuscontrol.databinding.FragmentTest2Binding

/**
 * 测试碎片2
 *
 * @author YSK
 * @date 2021-07-13
 */
class TestFragment2 : BaseFragment<FragmentTest2Binding>() {
    override fun bindView(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentTest2Binding.inflate(inflater, container, false)
}