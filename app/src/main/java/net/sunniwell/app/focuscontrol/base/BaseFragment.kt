package net.sunniwell.app.focuscontrol.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

/**
 * 基础碎片
 *
 * @author YSK
 * @date 2021-06-01
 *
 * @param T ViewBinding
 */
abstract class BaseFragment<out T : ViewBinding> : Fragment() {
    private var binding: T? = null

    protected val viewBinding
        get() = binding ?: throw IllegalStateException(
            "Fragment $this has not created view or has destroyed view."
        )

    protected open val baseActivity: BaseActivity<ViewBinding>
        get() = requireActivity() as? BaseActivity<ViewBinding>
            ?: throw IllegalStateException(
                "Activity which Fragment $this depend on is not BaseActivity."
            )

    //是否正在展示
    var isShowing = false
        private set

    /**
     * 绑定控件
     *
     * @return ViewBinding
     */
    abstract fun bindView(inflater: LayoutInflater, container: ViewGroup?): T

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = bindView(inflater, container)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initControl()
        initObserve()
        initData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onStart() {
        super.onStart()
        if (isShowing) return
        isShowing = true
        onShow()
    }

    override fun onStop() {
        super.onStop()
        if (!isShowing) return
        isShowing = false
        onHide()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        if (hidden) {
            if (!isShowing) return
            isShowing = false
            onHide()
        } else {
            if (isShowing) return
            isShowing = true
            onShow()
        }
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

    /**
     * 展示
     *
     * 在此编写展示时的业务逻辑
     */
    protected open fun onShow() {}

    /**
     * 隐藏
     *
     * 在此编写隐藏时的业务逻辑
     */
    protected open fun onHide() {}
}