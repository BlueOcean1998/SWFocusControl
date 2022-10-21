package net.sunniwell.aar.focuscontrol.`interface`

import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import net.sunniwell.aar.focuscontrol.util.FocusControlLogger
import net.sunniwell.aar.focuscontrol.util.firstFocusableChild
import net.sunniwell.aar.focuscontrol.util.focusControlViewParents
import net.sunniwell.aar.focuscontrol.util.focusableChildren

/**
 * 焦点控制ViewParent接口
 *
 * @author YSK
 * @date 2021-07-20
 *
 * @see ViewParent
 */
interface FocusControlViewParent : ViewParent {
    companion object {
        //禁止自动寻焦
        const val NULL_SEARCH_FOCUS = -4

        //系统自动寻焦
        const val SYSTEM_SEARCH_FOCUS = -3

        //默认自动寻焦
        const val DEF_SEARCH_FOCUS = -2

        private val logger = FocusControlLogger(FocusControlViewParent::class.java.simpleName)
    }

    //是否记录最后聚焦控件
    var recordFocusEnabled: Boolean

    //默认聚焦控件id
    var defFocusId: Int

    //最后聚焦控件
    var lastFocusView: View?

    //向左寻焦的控件id
    var searchFocusLeft: Int

    //向右寻焦的控件id
    var searchFocusRight: Int

    //向上寻焦的控件id
    var searchFocusUp: Int

    //向下寻焦的控件id
    var searchFocusDown: Int

    /**
     * 寻找下一个聚焦的控件
     *
     * @param systemNextFocus 系统自动寻焦找到的控件
     * @param focused         上一个聚焦的控件
     * @param direction       方向
     * @return View?
     */
    fun findNextFocus(
        systemNextFocus: View?, focused: View?, direction: Int
    ): View? {
        logger.v("--- findNextFocus1 start ---")

        logger.v("findNextFocus1 systemNextFocus:$systemNextFocus")
        logger.v("findNextFocus1 focused:$focused")
        logger.v("findNextFocus1 direction:$direction")

        //系统默认寻焦找到的控件的焦点控制父控件树
        val nextFocusParents = systemNextFocus?.focusControlViewParents
        logger.v("findNextFocus1 nextFocusParents:$nextFocusParents")

        //外层的焦点控制父控件不能干扰内层的焦点控制父控件
        if (nextFocusParents == null || this !in nextFocusParents || focused == systemNextFocus) {
            //根据寻焦方向获取searchFocus属性值
            val searchFocus = when (direction) {
                View.FOCUS_LEFT -> searchFocusLeft
                View.FOCUS_RIGHT -> searchFocusRight
                View.FOCUS_UP -> searchFocusUp
                View.FOCUS_DOWN -> searchFocusDown
                else -> return null
            }
            logger.v("findNextFocus1 searchFocus:$searchFocus")

            /*
             * 根据searchFocus属性选择寻焦的方式
             * NULL_SEARCH_FOCUS：禁止自动寻焦。即焦点到达此布局该方向的边界时，无法再往该方向寻焦
             * SYSTEM_SEARCH_FOCUS：系统自动寻焦。即不进行焦点控制，直接使用系统找到的控件
             * DEF_SEARCH_FOCUS：默认自动寻焦。即使用第2个findNextFocus(...)寻焦
             * else：开发者设置的控件id。即焦点到达此布局该方向的边界时，聚焦到此id的控件上
             */
            when (searchFocus) {
                NULL_SEARCH_FOCUS -> return null
                SYSTEM_SEARCH_FOCUS -> return systemNextFocus
                DEF_SEARCH_FOCUS -> Unit
                else -> (this as? ViewGroup)?.rootView
                    ?.findViewById<View>(searchFocus)?.let { return it }
            }
        }

        val nextFocus = findNextFocus(
            systemNextFocus, nextFocusParents, focused
        )

        logger.v("findNextFocus1 nextFocus:$nextFocus")
        logger.v("--- findNextFocus1 end ---")

        return nextFocus
    }

    /**
     * 寻找下一个聚焦的控件
     *
     * @param systemNextFocus  系统自动寻焦找到的控件
     * @param nextFocusParents 系统默认寻焦找到的控件的焦点控制父控件树
     * @param focused          上一个聚焦的控件
     * @return View?
     */
    fun findNextFocus(
        systemNextFocus: View?,
        nextFocusParents: ArrayList<FocusControlViewParent>?,
        focused: View?
    ): View? {
        logger.v("--- findNextFocus2 start ---")

        if (systemNextFocus == null
            || focused == null
            || nextFocusParents == null
        ) return systemNextFocus

        //上一个聚焦控件的焦点控制父控件树
        val lastFocusParents = focused.focusControlViewParents

        logger.v("findNextFocus2 lastFocusParents:[")
        lastFocusParents.forEach { logger.v("  $it") }
        logger.v("]")

        logger.v("findNextFocus2 nextFocusParents:[")
        nextFocusParents.forEach { logger.v("  $it") }
        logger.v("]")

        //下一个聚焦的控件的焦点控制父控件
        var nextFocusParent: FocusControlViewParent? = null

        /*在下一个聚焦控件的焦点父控件树中寻找
        不与上一个聚焦控件的焦点控制父控件树重叠的最外层的焦点控制父控件
        且该父控件有记录焦点*/
        for (parent in nextFocusParents) {
            if (parent !in lastFocusParents && parent.recordFocusEnabled) {
                nextFocusParent = parent
                break
            }
        }

        /*若找到的焦点控制父控件都不记录焦点，重新寻找，不再要求焦点控制父控件有记录焦点*/
        if (nextFocusParent == null) {
            for (parent in nextFocusParents) {
                if (parent !in lastFocusParents) {
                    nextFocusParent = parent
                    break
                }
            }
        }

        logger.v("findNextFocus2 nextFocusParent:$nextFocusParent")

        //下一个聚焦控件初始化为需要寻焦的控件
        val nextFocus = findNextFocus(nextFocusParent) ?: systemNextFocus
        logger.v("findNextFocus2 nextFocus:$nextFocus")

        logger.v("--- findNextFocus2 end ---")

        return nextFocus
    }

    /**
     * 寻找下一个聚焦的控件
     *
     * @param nextFocusParent 下一个聚焦的控件的焦点控制父控件
     * @return View?
     */
    fun findNextFocus(nextFocusParent: FocusControlViewParent?): View? {
        logger.v("--- findNextFocus3 start ---")
        var nextFocus: View? = null
        if (nextFocusParent is ViewGroup) {
            //获取焦点控制父控件的所有可聚焦的子控件
            val focusableChildren = nextFocusParent.focusableChildren
            //获取焦点控制父控件记录的控件
            val nextLastFocusView = nextFocusParent.lastFocusView
            logger.d("findNextFocus3 nextLastFocusView:$nextLastFocusView")
            /*如果其有记录焦点，且记录的控件在其所有可聚焦的子控件之内
            （有可能首次聚焦没有记录，有可能已无法聚焦，也有可能已被移除但还未销毁）*/
            nextFocus = if (nextFocusParent.recordFocusEnabled
                && nextLastFocusView in focusableChildren
            ) {
                //返回其记录的控件
                nextLastFocusView
            } else {
                //否则返回其默认聚焦控件，如果不在其所有可聚焦的子控件之内则返回其首个可聚焦控件
                val defFocus: View? =
                    nextFocusParent.findViewById(nextFocusParent.defFocusId)
                logger.d("findNextFocus3 defFocus:$defFocus")
                if (defFocus in focusableChildren) {
                    defFocus
                } else {
                    val firstFocusableChild = nextFocusParent.firstFocusableChild
                    logger.d("findNextFocus3 firstFocusableChild:$firstFocusableChild")
                    firstFocusableChild
                }
            }
        }
        logger.v("--- findNextFocus3 end ---")
        return nextFocus
    }
}