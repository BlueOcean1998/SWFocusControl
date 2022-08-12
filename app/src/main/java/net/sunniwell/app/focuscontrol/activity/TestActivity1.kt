package net.sunniwell.app.focuscontrol.activity

import net.sunniwell.app.focuscontrol.base.BaseActivity
import net.sunniwell.app.focuscontrol.databinding.ActivityTest1Binding

/**
 * 测试活动1
 *
 * @author YSK
 * @date 2021-06-01
 */
class TestActivity1 : BaseActivity<ActivityTest1Binding>() {
    override fun bindView() = ActivityTest1Binding.inflate(layoutInflater)
}