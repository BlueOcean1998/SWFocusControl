package net.sunniwell.aar.focuscontrol.util

import android.util.Log

/**
 * 日志工具类
 *
 * @author YSK
 * @date 2021-07-18
 */
object FocusControlLogUtil {
    const val ALL = 0
    const val VERBOSE = 1
    const val DEBUG = 2
    const val INFO = 3
    const val WARN = 4
    const val ERROR = 5
    const val NONE = 6

    var level = NONE
    var tag = "focuscontrol"

    fun v(msg: String, tag: String = FocusControlLogUtil.tag) {
        if (level <= VERBOSE) Log.v(tag, msg)
    }

    fun d(msg: String, tag: String = FocusControlLogUtil.tag) {
        if (level <= DEBUG) Log.d(tag, msg)
    }

    fun i(msg: String, tag: String = FocusControlLogUtil.tag) {
        if (level <= INFO) Log.i(tag, msg)
    }

    fun w(msg: String, tag: String = FocusControlLogUtil.tag) {
        if (level <= WARN) Log.w(tag, msg)
    }

    fun e(msg: String, tag: String = FocusControlLogUtil.tag) {
        if (level <= ERROR) Log.e(tag, msg)
    }
}

internal fun Any.getLogger() = FocusControlLogger(javaClass.simpleName)

class FocusControlLogger(private val tag: String) {
    fun v(msg: String) = FocusControlLogUtil.v(msg, tag)
    fun d(msg: String) = FocusControlLogUtil.d(msg, tag)
    fun i(msg: String) = FocusControlLogUtil.i(msg, tag)
    fun w(msg: String) = FocusControlLogUtil.w(msg, tag)
    fun e(msg: String) = FocusControlLogUtil.e(msg, tag)
}
