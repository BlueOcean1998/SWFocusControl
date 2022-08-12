package net.sunniwell.aar.focuscontrol.`interface`

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import net.sunniwell.aar.focuscontrol.layout.FocusControlRecyclerView
import net.sunniwell.aar.focuscontrol.util.FocusControlLogger

/**
 * 焦点控制LayoutManager接口
 *
 * @author YSK
 * @date 2022-07-01
 */
interface FocusControlLayoutManager {
    companion object {
        /*滚动速度*/
        const val SCROLL_SPEED_SLOW = 0.5f
        const val SCROLL_SPEED_NORMAL = 0.7f
        const val SCROLL_SPEED_FAST = 1f
        const val SCROLL_SPEED_IMMEDIATELY = Float.MAX_VALUE

        private val logger = FocusControlLogger(FocusControlLayoutManager::class.java.simpleName)
    }

    //是否保持焦点在中间
    var isHoldFocusInCenter: Boolean

    //滚动速度（仅使用smoothScrollToPosition时生效）
    var scrollSpeed: Float

    /**
     * item聚焦时自动滚动到中间
     *
     * @param parent    RecyclerView
     * @param child     item
     * @param immediate 是否立即滚动到中间
     * @return 是否有滚动
     */
    fun requestChildRectangleInCenter(
        parent: RecyclerView, child: View, immediate: Boolean
    ): Boolean {
        val dx = child.left - child.scrollX - (parent.width - child.width) / 2
        val dy = child.top - child.scrollY - (parent.height - child.height) / 2
        logger.d("dx:$dx dy:$dy")
        if (immediate) {
            parent.scrollBy(dx, dy)
        } else {
            parent.smoothScrollBy(dx, dy)
        }
        return dx != 0 || dy != 0
    }

    /**
     * 保持焦点
     *
     * @param recyclerView RecyclerView
     */
    fun holdFocus(recyclerView: RecyclerView) {
        //当item数量发生改变时，保持焦点
        if (recyclerView is FocusControlRecyclerView) recyclerView.holdFocus()
    }
}