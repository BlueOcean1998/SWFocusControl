package net.sunniwell.app.focuscontrol.activity

import android.os.Bundle
import net.sunniwell.app.focuscontrol.base.BaseActivity
import net.sunniwell.app.focuscontrol.databinding.ActivityTestBinding
import net.sunniwell.app.focuscontrol.util.openActivity

/**
 * 测试活动
 *
 * @author YSK
 * @date 2021-07-17
 */
class TestActivity : BaseActivity<ActivityTestBinding>() {
    override fun bindView() = ActivityTestBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activity = this
        viewBinding.run {
            btnToTestActivity1.setOnClickListener {
                openActivity<TestActivity1>(activity)
            }

            btnToTestActivity1Java.setOnClickListener {
                openActivity<TestActivity1Java>(activity)
            }

            btnToTestActivity1Fragment.setOnClickListener {
                openActivity<TestActivity1Fragment>(activity)
            }

            btnToTestActivity2.setOnClickListener {
                openActivity<TestActivity2>(activity)
            }
        }
    }
}