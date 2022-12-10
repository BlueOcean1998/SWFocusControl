package net.sunniwell.app.focuscontrol.base

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.viewbinding.ViewBinding
import java.util.concurrent.CopyOnWriteArrayList

/**
 * 基础活动
 *
 * @author YSK
 * @date 2021-07-20
 *
 * @param T ViewBinding
 */
abstract class BaseActivity<out T : ViewBinding> : FragmentActivity() {
    companion object {
        val activities = CopyOnWriteArrayList<BaseActivity<ViewBinding>>()

        /**
         * 获取指定活动
         *
         * @param R 基础活动类
         * @return 基础活动
         */
        inline fun <reified R : BaseActivity<ViewBinding>>
                findActivity(): BaseActivity<ViewBinding>? {
            activities.forEach { if (it::class == R::class) return it }
            return null
        }

        /**
         * 退出程序
         */
        fun finishAll() {
            activities.forEach {
                if (!it.isFinishing) it.finish()
            }
            activities.clear()
        }
    }

    protected val viewBinding by lazy { bindView() }

    /**
     * 绑定控件
     *
     * @return ViewBinding
     */
    abstract fun bindView(): T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activities.add(this)

        setContentView(viewBinding.root)
        onViewCreated()
    }

    /**
     * View创建
     */
    protected open fun onViewCreated() {
        initControl()
        initObserve()
        initData()
    }

    override fun onDestroy() {
        super.onDestroy()
        activities.remove(this)
    }

    /**
     * 初始化控制
     *
     * 在此编写用户操作
     * 例：btnStart.setOnClickListener { ... }
     */
    protected open fun initControl() {}

    /**
     * 初始化观察者
     *
     * 在此编写LiveData变化UI反馈
     * 例：textLiveData.observe(this) { ... }
     */
    protected open fun initObserve() {}

    /**
     * 初始化数据
     *
     * 在此初始化ViewModel数据
     * 例：viewModel.initData()
     */
    protected open fun initData() {}
}