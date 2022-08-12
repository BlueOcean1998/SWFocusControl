package net.sunniwell.aar.focuscontrol.layout

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ScrollView
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import net.sunniwell.aar.focuscontrol.R
import net.sunniwell.aar.focuscontrol.`interface`.FocusControlViewParent
import net.sunniwell.aar.focuscontrol.util.getLogger

/**
 * 焦点控制ScrollView
 *
 * @author YSK
 * @date 2022-05-27
 *
 * @param context      上下文
 * @param attrs        自定义属性
 * @param defStyleAttr 默认属性
 * @param defStyleRes  默认风格
 */
open class FocusControlScrollView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = R.style.FocusControlScrollView
) : ScrollView(context, attrs, defStyleAttr), FocusControlViewParent {
    companion object {
        const val NULL_SEARCH_FOCUS = FocusControlViewParent.NULL_SEARCH_FOCUS
        const val SYSTEM_SEARCH_FOCUS = FocusControlViewParent.SYSTEM_SEARCH_FOCUS
        const val DEF_SEARCH_FOCUS = FocusControlViewParent.DEF_SEARCH_FOCUS
    }

    private val logger = getLogger()

    //是否保持焦点在中间
    var isHoldFocusInCenter: Boolean

    final override var recordFocusEnabled: Boolean
    final override var defFocusId: Int
    final override var lastFocusView: View? = null
    final override var searchFocusLeft: Int
    final override var searchFocusRight: Int
    final override var searchFocusUp: Int
    final override var searchFocusDown: Int

    init {
        val typedArray = context.obtainStyledAttributes(
            attrs, R.styleable.FocusControlScrollView, 0, defStyleRes
        )
        val recordFocusEnabled = typedArray.getBoolean(
            R.styleable.FocusControlScrollView_recordFocusEnabled, false
        )
        val defFocusId = typedArray.getResourceId(
            R.styleable.FocusControlScrollView_defFocusId, NO_ID
        )
        /*获取searchFocus属性值，如果获取不到resourceId则获取int值*/
        val searchFocusLeft = typedArray.getResourceId(
            R.styleable.FocusControlScrollView_searchFocusLeft,
            typedArray.getInt(
                R.styleable.FocusControlScrollView_searchFocusLeft, DEF_SEARCH_FOCUS
            )
        )
        val searchFocusRight = typedArray.getResourceId(
            R.styleable.FocusControlScrollView_searchFocusRight,
            typedArray.getInt(
                R.styleable.FocusControlScrollView_searchFocusRight, DEF_SEARCH_FOCUS
            )
        )
        val searchFocusUp = typedArray.getResourceId(
            R.styleable.FocusControlScrollView_searchFocusUp,
            typedArray.getInt(
                R.styleable.FocusControlScrollView_searchFocusUp, DEF_SEARCH_FOCUS
            )
        )
        val searchFocusDown = typedArray.getResourceId(
            R.styleable.FocusControlScrollView_searchFocusDown,
            typedArray.getInt(
                R.styleable.FocusControlScrollView_searchFocusDown, DEF_SEARCH_FOCUS
            )
        )
        val isHoldFocusInCenter = typedArray.getBoolean(
            R.styleable.FocusControlScrollView_isHoldFocusInCenter, false
        )
        typedArray.recycle()

        this.recordFocusEnabled = recordFocusEnabled
        this.defFocusId = defFocusId
        this.searchFocusLeft = searchFocusLeft
        this.searchFocusRight = searchFocusRight
        this.searchFocusUp = searchFocusUp
        this.searchFocusDown = searchFocusDown
        this.isHoldFocusInCenter = isHoldFocusInCenter
    }

    override fun focusSearch(focused: View?, direction: Int): View? {
        logger.d("--- focusSearch start ---")

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

    override fun requestChildFocus(child: View?, focused: View?) {
        logger.d("--- requestChildFocus start ---")

        super.requestChildFocus(child, focused)

        val focusedChild = findFocus()
        logger.d("focusedChild:$focusedChild")

        //记录焦点
        if (recordFocusEnabled) lastFocusView = focusedChild
        logger.d("lastFocusView:$lastFocusView")

        if (isHoldFocusInCenter) {
            holdFocusInCenter(focusedChild)
        }

        logger.d("--- requestChildFocus end ---")
    }

    /**
     * 保持焦点在中间
     *
     * @param focusedChild 正在聚焦的子控件
     */
    open fun holdFocusInCenter(focusedChild: View?) {
        logger.d("--- holdFocusInCenter start ---")

        logger.d("focusedChild:$focusedChild")
        if (focusedChild != null) {
            /*滚动到正在聚焦的子控件相对该控件的位置*/
            var dx = 0
            var dy = 0
            dx += focusedChild.left - focusedChild.scrollX
            dy += focusedChild.top - focusedChild.scrollY
            var parent = focusedChild
            while (parent != null && parent.parent != this) {
                parent = parent.parent as? View
                logger.d("parent:$parent")
                if (parent != null) {
                    dx += parent.left - parent.scrollX
                    dy += parent.top - parent.scrollY
                }
            }
            dx += (focusedChild.width - width) / 2
            dy += (focusedChild.height - height) / 2
            logger.d("dx:$dx dy:$dy")
            smoothScrollTo(dx, dy)
        }

        logger.d("--- holdFocusInCenter end ---")
    }

    /**
     * 此函数会影响focusSearch函数执行
     * 屏蔽此函数
     *
     * @see focusSearch
     */
    @Deprecated(
        "This function affects function focusSearch execution.",
        ReplaceWith("false")
    )
    override fun arrowScroll(direction: Int) = false
}