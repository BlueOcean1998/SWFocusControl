package net.sunniwell.aar.focuscontrol.layout

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import net.sunniwell.aar.focuscontrol.R
import net.sunniwell.aar.focuscontrol.`interface`.FocusControlViewParent
import net.sunniwell.aar.focuscontrol.util.getLogger

/**
 * 焦点控制RelativeLayout
 *
 * @author YSK
 * @date 2021-07-20
 *
 * @param context      上下文
 * @param attrs        自定义属性
 * @param defStyleAttr 默认属性
 * @param defStyleRes  默认风格
 */
open class FocusControlRelativeLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = R.style.FocusControlRelativeLayout
) : RelativeLayout(context, attrs, defStyleAttr), FocusControlViewParent {
    companion object {
        const val NULL_SEARCH_FOCUS = FocusControlViewParent.NULL_SEARCH_FOCUS
        const val SYSTEM_SEARCH_FOCUS = FocusControlViewParent.SYSTEM_SEARCH_FOCUS
        const val DEF_SEARCH_FOCUS = FocusControlViewParent.DEF_SEARCH_FOCUS
    }

    private val logger = getLogger()

    final override var recordFocusEnabled: Boolean
    final override var defFocusId: Int
    final override var lastFocusView: View? = null
    final override var searchFocusLeft: Int
    final override var searchFocusRight: Int
    final override var searchFocusUp: Int
    final override var searchFocusDown: Int

    init {
        val typedArray = context.obtainStyledAttributes(
            attrs, R.styleable.FocusControlRelativeLayout, 0, defStyleRes
        )
        val recordFocusEnabled = typedArray.getBoolean(
            R.styleable.FocusControlRelativeLayout_recordFocusEnabled, false
        )
        val defFocusId = typedArray.getResourceId(
            R.styleable.FocusControlLinearLayout_defFocusId, NO_ID
        )
        /*获取searchFocus属性值，如果获取不到resourceId则获取int值*/
        val searchFocusLeft = typedArray.getResourceId(
            R.styleable.FocusControlRelativeLayout_searchFocusLeft,
            typedArray.getInt(
                R.styleable.FocusControlRelativeLayout_searchFocusLeft, DEF_SEARCH_FOCUS
            )
        )
        val searchFocusRight = typedArray.getResourceId(
            R.styleable.FocusControlRelativeLayout_searchFocusRight,
            typedArray.getInt(
                R.styleable.FocusControlRelativeLayout_searchFocusRight, DEF_SEARCH_FOCUS
            )
        )
        val searchFocusUp = typedArray.getResourceId(
            R.styleable.FocusControlRelativeLayout_searchFocusUp,
            typedArray.getInt(
                R.styleable.FocusControlRelativeLayout_searchFocusUp, DEF_SEARCH_FOCUS
            )
        )
        val searchFocusDown = typedArray.getResourceId(
            R.styleable.FocusControlRelativeLayout_searchFocusDown,
            typedArray.getInt(
                R.styleable.FocusControlRelativeLayout_searchFocusDown, DEF_SEARCH_FOCUS
            )
        )
        typedArray.recycle()

        this.recordFocusEnabled = recordFocusEnabled
        this.defFocusId = defFocusId
        this.searchFocusLeft = searchFocusLeft
        this.searchFocusRight = searchFocusRight
        this.searchFocusUp = searchFocusUp
        this.searchFocusDown = searchFocusDown
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

        //记录焦点
        if (recordFocusEnabled) lastFocusView = child
        logger.d("lastFocusView:$lastFocusView")

        logger.d("--- requestChildFocus end ---")
    }
}