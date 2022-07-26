package net.sunniwell.aar.focuscontrol.util

import android.view.View
import android.view.ViewGroup
import net.sunniwell.aar.focuscontrol.`interface`.FocusControlViewParent

/**
 * FocusControl框架使用的View工具类
 *
 * @author YSK
 * @date 2021-07-20
 */

/**
 * 获取正在聚焦的item
 * 使用getFocusedChild获取到的不一定是此布局的子控件，也有可能是子控件的子控件，或更多嵌套
 * 该方法意在获取focusedChild最外层的item
 *
 * @return View?
 */
inline val ViewGroup.focusedItem get() = getItemByView(focusedChild)

/**
 * 根据控件获取item（item是ViewGroup的直接子控件）
 * 该控件必须是item的子控件或item本身，如果都不是则返回空
 *
 * @param view 控件
 * @return View?
 */
fun ViewGroup.getItemByView(view: View?): View? {
    var item = view
    while (item != null && item.parent != this) {
        item = item.parent as? View
    }
    return item
}

/**
 * 获取首个可聚焦的子控件
 *
 * @return View?
 */
val ViewGroup.firstFocusableChild: View?
    get() {
        for (childIndex in 0 until childCount) {
            val child = getChildAt(childIndex) ?: continue
            if (child.canBeFocus) return child
            if (child is ViewGroup) {
                child.firstFocusableChild?.let { return it }
            }
        }
        return null
    }

/**
 * 获取全部可聚焦的子控件
 *
 * @return ArrayList<View>
 */
val ViewGroup.focusableChildren: ArrayList<View>
    get() = ArrayList<View>().apply {
        for (childIndex in 0 until childCount) {
            val child = getChildAt(childIndex) ?: continue
            if (child.canBeFocus) add(child)
            if (child is ViewGroup) {
                addAll(child.focusableChildren)
            }
        }
    }

/**
 * 控件是否可被聚焦
 *
 * @return Boolean
 */
inline val View.canBeFocus get() = isFocusable && isEnabled && visibility == View.VISIBLE

/**
 * 获取继承了FocusControlViewParent接口的直接父控件
 *
 * @return FocusControlViewParent?
 */
inline val View.focusControlViewParent: FocusControlViewParent?
    get() {
        var focusControlViewParent = parent
        while (focusControlViewParent !is FocusControlViewParent) {
            focusControlViewParent = focusControlViewParent?.parent ?: return null
        }
        return focusControlViewParent
    }

/**
 * 获取继承了FocusControlViewParent接口的焦点父控件树（所有焦点父控件）
 * 按树结构排列，[0]为根父控件，[size - 1]为直接父控件
 *
 * @return ArrayList<FocusControlViewParent>
 */
inline val View.focusControlViewParents
    get() = ArrayList<FocusControlViewParent>().apply {
        var focusControlViewParent = parent
        while (focusControlViewParent != null) {
            if (focusControlViewParent is FocusControlViewParent)
                add(focusControlViewParent)
            focusControlViewParent = focusControlViewParent.parent
        }
        reverse()
    }
