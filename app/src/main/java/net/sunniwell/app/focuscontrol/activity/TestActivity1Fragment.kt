package net.sunniwell.app.focuscontrol.activity

import net.sunniwell.app.focuscontrol.base.BaseActivity
import net.sunniwell.app.focuscontrol.databinding.ActivityTest1FragmentBinding

/**
 * 测试活动1Fragment
 *
 * @author YSK
 * @date 2021-07-13
 */
class TestActivity1Fragment : BaseActivity<ActivityTest1FragmentBinding>() {
    override fun bindView() = ActivityTest1FragmentBinding.inflate(layoutInflater)
}