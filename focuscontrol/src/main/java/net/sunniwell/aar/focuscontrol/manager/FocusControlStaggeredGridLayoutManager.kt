package net.sunniwell.aar.focuscontrol.manager

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.View
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import net.sunniwell.aar.focuscontrol.`interface`.FocusControlLayoutManager
import net.sunniwell.aar.focuscontrol.util.getLogger

/**
 * 焦点控制StaggeredGridLayoutManager
 *
 * @author YSK
 * @date 2021-08-06
 */
open class FocusControlStaggeredGridLayoutManager : StaggeredGridLayoutManager,
    FocusControlLayoutManager {
    companion object {
        const val SCROLL_SPEED_SLOW = FocusControlLayoutManager.SCROLL_SPEED_SLOW
        const val SCROLL_SPEED_NORMAL = FocusControlLayoutManager.SCROLL_SPEED_NORMAL
        const val SCROLL_SPEED_FAST = FocusControlLayoutManager.SCROLL_SPEED_FAST
        const val SCROLL_SPEED_IMMEDIATELY = FocusControlLayoutManager.SCROLL_SPEED_IMMEDIATELY
    }

    private val logger = getLogger()

    final override var isHoldFocusInCenter = true
    final override var scrollSpeed = SCROLL_SPEED_NORMAL

    @JvmOverloads
    constructor(
        spanCount: Int, @RecyclerView.Orientation orientation: Int,
        reverseLayout: Boolean = false
    ) : super(spanCount, orientation) {
        this.reverseLayout = reverseLayout
    }

    constructor(
        context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun requestChildRectangleOnScreen(
        parent: RecyclerView, child: View, rect: Rect, immediate: Boolean
    ) = requestChildRectangleOnScreen(
        parent, child, rect, immediate, false
    )

    override fun requestChildRectangleOnScreen(
        parent: RecyclerView,
        child: View,
        rect: Rect,
        immediate: Boolean,
        focusedChildVisible: Boolean
    ) = if (isHoldFocusInCenter) requestChildRectangleInCenter(
        parent, child, immediate
    ) else super.requestChildRectangleOnScreen(
        parent, child, rect, immediate, focusedChildVisible
    )

    override fun smoothScrollToPosition(
        recyclerView: RecyclerView?, state: RecyclerView.State?, position: Int
    ) {
        recyclerView?.let {
            if (scrollSpeed == SCROLL_SPEED_IMMEDIATELY) {
                super.scrollToPosition(position)
            } else {
                startSmoothScroll(object : LinearSmoothScroller(recyclerView.context) {
                    override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics?) =
                        super.calculateSpeedPerPixel(displayMetrics) / scrollSpeed
                }.apply { targetPosition = position })
            }
        }
    }

    override fun onItemsChanged(recyclerView: RecyclerView) {
        super.onItemsChanged(recyclerView)
        logger.d("onItemsChanged")
        holdFocus(recyclerView)
    }

    override fun onItemsAdded(recyclerView: RecyclerView, positionStart: Int, itemCount: Int) {
        super.onItemsAdded(recyclerView, positionStart, itemCount)
        logger.d("onItemsAdded")
        holdFocus(recyclerView)
    }

    override fun onItemsRemoved(recyclerView: RecyclerView, positionStart: Int, itemCount: Int) {
        super.onItemsRemoved(recyclerView, positionStart, itemCount)
        logger.d("onItemsRemoved")
        holdFocus(recyclerView)
    }

    override fun onItemsUpdated(recyclerView: RecyclerView, positionStart: Int, itemCount: Int) {
        super.onItemsUpdated(recyclerView, positionStart, itemCount)
        logger.d("onItemsUpdated")
        holdFocus(recyclerView)
    }

    override fun onItemsUpdated(
        recyclerView: RecyclerView, positionStart: Int, itemCount: Int, payload: Any?
    ) {
        super.onItemsUpdated(recyclerView, positionStart, itemCount, payload)
        logger.d("onItemsUpdated")
        holdFocus(recyclerView)
    }

    override fun onItemsMoved(recyclerView: RecyclerView, from: Int, to: Int, itemCount: Int) {
        super.onItemsMoved(recyclerView, from, to, itemCount)
        logger.d("onItemsMoved")
        holdFocus(recyclerView)
    }
}