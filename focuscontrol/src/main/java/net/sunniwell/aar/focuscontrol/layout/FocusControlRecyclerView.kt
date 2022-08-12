package net.sunniwell.aar.focuscontrol.layout

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import net.sunniwell.aar.focuscontrol.R
import net.sunniwell.aar.focuscontrol.`interface`.FocusControlLayoutManager
import net.sunniwell.aar.focuscontrol.`interface`.FocusControlViewParent
import net.sunniwell.aar.focuscontrol.util.*
import kotlin.math.max
import kotlin.math.min

/**
 * 焦点控制RecyclerView
 *
 * @author YSK
 * @date 2021-07-20
 *
 * @param context      上下文
 * @param attrs        自定义属性
 * @param defStyleAttr 默认属性
 * @param defStyleRes  默认风格
 */
open class FocusControlRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = R.style.FocusControlRecyclerView
) : RecyclerView(context, attrs, defStyleAttr), FocusControlViewParent {
    companion object {
        const val NULL_SEARCH_FOCUS = FocusControlViewParent.NULL_SEARCH_FOCUS
        const val SYSTEM_SEARCH_FOCUS = FocusControlViewParent.SYSTEM_SEARCH_FOCUS
        const val DEF_SEARCH_FOCUS = FocusControlViewParent.DEF_SEARCH_FOCUS
    }

    private val logger = getLogger()

    final override var recordFocusEnabled: Boolean
    final override var defFocusId = View.NO_ID
    final override var lastFocusView: View? = null
    final override var searchFocusLeft: Int
    final override var searchFocusRight: Int
    final override var searchFocusUp: Int
    final override var searchFocusDown: Int

    //到达边界时是否允许聚焦（开启此功能，可能导致滚动时丢失焦点）
    var isBoundaryFocus: Boolean

    //自动滚动触发的边界item数
    var boundaryItemCount = 0
        //获取时的值不能超过一跨（行\列）显示的item数量的一半-1，向下取整，最后乘跨度数，至少为0
        get() = max(
            0, min(field, visibleChildCount / spanCount / 2 - 1) * spanCount
        )

    //方向
    inline var orientation
        set(value) = layoutManager.run {
            when (this) {
                is GridLayoutManager -> orientation = value
                is LinearLayoutManager -> orientation = value
                is StaggeredGridLayoutManager -> orientation = value
            }
        }
        get() = layoutManager.run {
            when (this) {
                is GridLayoutManager -> orientation
                is LinearLayoutManager -> orientation
                is StaggeredGridLayoutManager -> orientation
                else -> VERTICAL
            }
        }

    //是否反转
    inline var reverseLayout
        set(value) = layoutManager.run {
            when (this) {
                is GridLayoutManager -> reverseLayout = value
                is LinearLayoutManager -> reverseLayout = value
                is StaggeredGridLayoutManager -> reverseLayout = value
            }
        }
        get() = layoutManager.run {
            when (this) {
                is GridLayoutManager -> reverseLayout
                is LinearLayoutManager -> reverseLayout
                is StaggeredGridLayoutManager -> reverseLayout
                else -> false
            }
        }

    //item总数
    inline val itemCount get() = adapter?.itemCount ?: 0

    //跨度（行\列）数
    inline val spanCount
        get() = layoutManager.run {
            when (this) {
                is GridLayoutManager -> spanCount
                is LinearLayoutManager -> 1
                is StaggeredGridLayoutManager -> spanCount
                else -> 1
            }
        }

    //正在聚焦的item在显示的item中的位置
    inline val focusItemVisiblePosition: Int
        get() = focusItemPosition - firstVisibleItemPosition

    //正在聚焦的item在完整显示的item中的位置
    inline val focusItemCompletelyVisiblePosition: Int
        get() = focusItemPosition - firstCompletelyVisibleItemPosition

    //正在聚焦的item在所有的item中的位置
    inline val focusItemPosition: Int
        get() {
            return getChildAdapterPosition(focusedItem ?: return NO_POSITION)
        }

    //获取显示的item数量
    inline val visibleChildCount
        get() = lastVisibleItemPosition - firstVisibleItemPosition + 1

    //第一个显示的item位置
    inline val firstVisibleItemPosition
        get() = layoutManager.run {
            when (this) {
                is GridLayoutManager ->
                    findFirstVisibleItemPosition()
                is LinearLayoutManager ->
                    findFirstVisibleItemPosition()
                is StaggeredGridLayoutManager ->
                    findFirstVisibleItemPositions(null).run { get(0) }
                else -> NO_POSITION
            }
        }

    //最后一个显示的item位置
    inline val lastVisibleItemPosition
        get() = layoutManager.run {
            when (this) {
                is GridLayoutManager ->
                    findLastVisibleItemPosition()
                is LinearLayoutManager ->
                    findLastVisibleItemPosition()
                is StaggeredGridLayoutManager ->
                    findLastVisibleItemPositions(null)
                        .run { get(size - 1) } + itemCount % spanCount
                else -> NO_POSITION
            }
        }

    //获取完整显示的item数量
    inline val completelyVisibleChildCount
        get() = lastCompletelyVisibleItemPosition - firstCompletelyVisibleItemPosition + 1

    //第一个完整显示的item位置
    inline val firstCompletelyVisibleItemPosition
        get() = layoutManager.run {
            when (this) {
                is GridLayoutManager ->
                    findFirstCompletelyVisibleItemPosition()
                is LinearLayoutManager ->
                    findFirstCompletelyVisibleItemPosition()
                is StaggeredGridLayoutManager ->
                    findFirstCompletelyVisibleItemPositions(null).run { get(0) }
                else -> NO_POSITION
            }
        }

    //最后一个完整显示的item位置
    inline val lastCompletelyVisibleItemPosition
        get() = layoutManager.run {
            when (this) {
                is GridLayoutManager ->
                    findLastCompletelyVisibleItemPosition()
                is LinearLayoutManager ->
                    findLastCompletelyVisibleItemPosition()
                is StaggeredGridLayoutManager ->
                    findLastCompletelyVisibleItemPositions(null)
                        .run { get(size - 1) } + itemCount % spanCount
                else -> NO_POSITION
            }
        }

    //获取首个可聚焦的子控件
    inline val firstFocusableChild: View?
        get() {
            for (position in 0 until itemCount) {
                val child = findViewByPosition(position) ?: continue
                if (child.canBeFocus) return child
                if (child is ViewGroup) {
                    child.firstFocusableChild?.let { return it }
                }
            }
            return null
        }

    //获取最后一个可聚焦的子控件
    inline val lastFocusableChild: View?
        get() {
            for (position in itemCount - 1 downTo 0) {
                val child = findViewByPosition(position) ?: continue
                if (child.canBeFocus) return child
                if (child is ViewGroup) {
                    child.firstFocusableChild?.let { return it }
                }
            }
            return null
        }

    //保持的焦点位置
    var holdFocusPosition = NO_POSITION

    /**
     * 根据位置查找item
     *
     * @param position
     * @return item
     */
    fun findViewByPosition(position: Int) = layoutManager.run {
        when (this) {
            is GridLayoutManager -> findViewByPosition(position)
            is LinearLayoutManager -> findViewByPosition(position)
            is StaggeredGridLayoutManager -> findViewByPosition(position)
            else -> null
        }
    }

    init {
        val typedArray = context.obtainStyledAttributes(
            attrs, R.styleable.FocusControlRecyclerView, 0, defStyleRes
        )
        val recordFocusEnabled = typedArray.getBoolean(
            R.styleable.FocusControlRecyclerView_recordFocusEnabled, false
        )
        /*获取searchFocus属性值，如果获取不到resourceId则获取int值*/
        val searchFocusLeft = typedArray.getResourceId(
            R.styleable.FocusControlRecyclerView_searchFocusLeft,
            typedArray.getInt(
                R.styleable.FocusControlRecyclerView_searchFocusLeft, DEF_SEARCH_FOCUS
            )
        )
        val searchFocusRight = typedArray.getResourceId(
            R.styleable.FocusControlRecyclerView_searchFocusRight,
            typedArray.getInt(
                R.styleable.FocusControlRecyclerView_searchFocusRight, DEF_SEARCH_FOCUS
            )
        )
        val searchFocusUp = typedArray.getResourceId(
            R.styleable.FocusControlRecyclerView_searchFocusUp,
            typedArray.getInt(
                R.styleable.FocusControlRecyclerView_searchFocusUp, DEF_SEARCH_FOCUS
            )
        )
        val searchFocusDown = typedArray.getResourceId(
            R.styleable.FocusControlRecyclerView_searchFocusDown,
            typedArray.getInt(
                R.styleable.FocusControlRecyclerView_searchFocusDown, DEF_SEARCH_FOCUS
            )
        )
        val isBoundaryFocus = typedArray.getBoolean(
            R.styleable.FocusControlRecyclerView_isBoundaryFocus, false
        )
        val boundaryItemCount = typedArray.getInt(
            R.styleable.FocusControlRecyclerView_boundaryItemCount, 0
        )
        typedArray.recycle()

        this.recordFocusEnabled = recordFocusEnabled
        this.searchFocusLeft = searchFocusLeft
        this.searchFocusRight = searchFocusRight
        this.searchFocusUp = searchFocusUp
        this.searchFocusDown = searchFocusDown
        this.isBoundaryFocus = isBoundaryFocus
        this.boundaryItemCount = boundaryItemCount
    }

    override fun focusSearch(focused: View?, direction: Int): View? {
        logger.d("--- focusSearch start ---")

        //边界滚动
        val isBoundary = boundaryScroll(direction)
        logger.d("isBoundary:$isBoundary")
        val scrollState = scrollState
        logger.d("scrollState:$scrollState")

        //到达边界且正在滚动时不允许继续寻焦，否则可能丢失焦点
        if (isBoundary && !isBoundaryFocus && scrollState != SCROLL_STATE_IDLE) return null

        //记录焦点
        if (recordFocusEnabled) lastFocusView = focused

        //焦点控制
        val systemNextFocus = super.focusSearch(focused, direction)
        val nextFocus = findNextFocus(systemNextFocus, focused, direction)

        logger.d("lastFocusView:$lastFocusView")
        logger.d("systemNextFocus:$systemNextFocus")
        logger.d("nextFocus:$nextFocus")

        logger.d("--- focusSearch end ---")

        return nextFocus
    }

    /**
     * @see FocusControlViewParent.findNextFocus
     */
    override fun findNextFocus(
        systemNextFocus: View?, focused: View?, direction: Int
    ): View? {
        logger.d("--- findNextFocus1 start ---")

        val orientation = orientation
        logger.d("orientation:" + if (orientation == VERTICAL) "VERTICAL" else "HORIZONTAL")
        val reverseLayout = reverseLayout
        logger.d("reverseLayout:$reverseLayout")

        /*根据寻焦方向、排列方向和是否反向，寻找下一个聚焦的控件*/
        val nextFocus = when (direction) {
            FOCUS_LEFT -> if (orientation == HORIZONTAL)
                findNextFocus(focused, !reverseLayout) else null
            FOCUS_RIGHT -> if (orientation == HORIZONTAL)
                findNextFocus(focused, reverseLayout) else null
            FOCUS_UP -> if (orientation == VERTICAL)
                findNextFocus(focused, !reverseLayout) else null
            FOCUS_DOWN -> if (orientation == VERTICAL)
                findNextFocus(focused, reverseLayout) else null
            else -> null
        }
        logger.d("nextFocus:$nextFocus")

        logger.d("--- findNextFocus1 end ---")

        /*若找不到，则使用原寻焦方式*/
        return nextFocus ?: super.findNextFocus(systemNextFocus, focused, direction)
    }

    /**
     * 寻找下一个聚焦的控件
     *
     * @param focused     上一个聚焦的控件
     * @param isBackwards true:向后 false:向前
     * @return View?
     */
    open fun findNextFocus(focused: View?, isBackwards: Boolean): View? {
        logger.d("--- findNextFocus2 start ---")

        logger.d("focused:$focused isForward:$isBackwards")
        focused ?: return null

        val itemCount = itemCount
        logger.d("itemCount:$itemCount")
        val spanCount = spanCount
        logger.d("spanCount:$spanCount")

        /*向前或向后寻找可聚焦控件*/
        var nextFocus: View? = null
        val focusedItem = getItemByView(nextFocus)
        if (focusedItem != null) {
            var position = getChildAdapterPosition(focusedItem)
            logger.d("last position:$position")
            if (isBackwards) {
                while (position > spanCount - 1) {
                    position -= spanCount
                    val child = findViewByPosition(position)
                    if (child?.canBeFocus == true) {
                        nextFocus = child
                        break
                    }
                }
            } else {
                while (position < itemCount - spanCount) {
                    position += spanCount
                    val child = findViewByPosition(position)
                    if (child?.canBeFocus == true) {
                        nextFocus = child
                        break
                    }
                }
            }
            logger.d("next position:$position")
        }
        logger.d("nextFocus:$nextFocus")

        logger.d("--- findNextFocus2 end ---")

        return nextFocus
    }

    override fun requestChildFocus(child: View?, focused: View?) {
        logger.d("--- requestChildFocus start ---")

        super.requestChildFocus(child, focused)

        if (child != null) {
            //记录要保持的焦点位置
            holdFocusPosition = getChildAdapterPosition(child)
        }

        //记录焦点
        if (recordFocusEnabled) lastFocusView = child
        logger.d("lastFocusView:$lastFocusView")

        logger.d("--- requestChildFocus end ---")
    }

    /**
     * 到达边界时自动滚动
     *
     * @param direction 寻焦方向
     * @return Boolean
     */
    open fun boundaryScroll(direction: Int): Boolean {
        logger.d("--- boundaryScroll1 start ---")

        val orientation = orientation
        logger.d("orientation:" + if (orientation == VERTICAL) "VERTICAL" else "HORIZONTAL")
        val reverseLayout = reverseLayout
        logger.d("reverseLayout:$reverseLayout")

        /*根据寻焦方向、排列方向和是否反向，自动滚动到预定的位置，并计算是否到达边界*/
        when (direction) {
            FOCUS_LEFT -> if (orientation == HORIZONTAL) return boundaryScroll(!reverseLayout)
            FOCUS_RIGHT -> if (orientation == HORIZONTAL) return boundaryScroll(reverseLayout)
            FOCUS_UP -> if (orientation == VERTICAL) return boundaryScroll(!reverseLayout)
            FOCUS_DOWN -> if (orientation == VERTICAL) return boundaryScroll(reverseLayout)
        }

        logger.d("--- boundaryScroll1 end ---")

        return false
    }

    /**
     * 到达边界时自动滚动
     *
     * @param isBackwards true:向后 false:向前
     * @return Boolean
     */
    open fun boundaryScroll(isBackwards: Boolean): Boolean {
        logger.d("--- boundaryScroll2 start ---")

        var isBoundary = false

        val boundaryItemCount = boundaryItemCount
        logger.d("boundaryItemCount:$boundaryItemCount")
        val itemCount = itemCount
        logger.d("itemCount:$itemCount")
        val spanCount = spanCount
        logger.d("spanCount:$spanCount")
        val focusItemPosition = focusItemPosition
        logger.d("focusItemPosition:$focusItemPosition")

        //到达边界时是否自动滚动
        val isBoundaryScroll = layoutManager.run {
            //若保持焦点在中间，此处不再自动滚动
            if (this is FocusControlLayoutManager) !isHoldFocusInCenter else true
        }
        logger.d("isBoundaryScroll:$isBoundaryScroll")

        if (isBackwards) {
            val firstVisibleItemPosition = firstVisibleItemPosition
            logger.d("firstVisibleItemPosition:$firstVisibleItemPosition")

            /*计算是否到达边界*/
            if (focusItemPosition > boundaryItemCount - 1) {
                if (firstVisibleItemPosition
                    > focusItemPosition - boundaryItemCount
                ) isBoundary = true
            } else {
                if (firstVisibleItemPosition > 0) isBoundary = true
            }

            /*到达边界时自动滚动*/
            if (isBoundaryScroll) {
                if (firstVisibleItemPosition
                    >= focusItemPosition - boundaryItemCount - 2 * spanCount
                ) {
                    var scrollPosition = focusItemPosition - boundaryItemCount
                    if (!isBoundary || isBoundaryFocus) scrollPosition -= spanCount
                    if (scrollPosition < 0) scrollPosition = 0
                    logger.d("scrollPosition:$scrollPosition")
                    smoothScrollToPosition(scrollPosition)
                }
            }
        } else {
            val lastVisibleItemPosition = lastVisibleItemPosition
            logger.d("lastVisibleItemPosition:$lastVisibleItemPosition")

            /*计算是否到达边界*/
            if (focusItemPosition < itemCount - boundaryItemCount) {
                if (lastVisibleItemPosition
                    < focusItemPosition + boundaryItemCount
                ) isBoundary = true
            } else {
                if (lastVisibleItemPosition < itemCount - 1) isBoundary = true
            }

            /*到达边界时自动滚动*/
            if (isBoundaryScroll) {
                if (lastVisibleItemPosition
                    <= focusItemPosition + boundaryItemCount + 2 * spanCount
                ) {
                    var scrollPosition = focusItemPosition + boundaryItemCount
                    if (!isBoundary || isBoundaryFocus) scrollPosition += spanCount
                    if (scrollPosition > itemCount - 1) scrollPosition = itemCount - 1
                    logger.d("scrollPosition:$scrollPosition")
                    smoothScrollToPosition(scrollPosition)
                }
            }
        }

        logger.d("--- boundaryScroll2 end ---")

        return isBoundary
    }

    /**
     * 保持焦点
     */
    open fun holdFocus() {
        logger.d("--- holdFocus start ---")

        logger.d("holdFocusPosition:$holdFocusPosition")
        val itemCount = itemCount
        logger.d("itemCount:$itemCount")

        //若保持的聚焦位置大于最后一个item位置，设置保持焦点位置为最后一个item位置
        if (holdFocusPosition > itemCount - 1) holdFocusPosition = itemCount - 1

        //寻找保持的焦点位置的可聚焦的控件
        var child = findViewByPosition(holdFocusPosition)
        if (child != null) {
            child = when {
                child.canBeFocus -> child
                child is FocusControlViewParent -> child.lastFocusView
                else -> null
            }
        }
        logger.d("child:$child")
        if (child != null) {
            //保持焦点位置
            if (hasFocus()) child.requestFocus()
            //记录焦点
            if (recordFocusEnabled) lastFocusView = child
        }

        logger.d("--- holdFocus end ---")
    }
}