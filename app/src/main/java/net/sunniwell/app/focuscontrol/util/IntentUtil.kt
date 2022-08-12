package net.sunniwell.app.focuscontrol.util

import android.app.Activity
import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import net.sunniwell.app.focuscontrol.base.baseApplication

/**
 * intent工具类
 *
 * @author YSK
 * @date 2021-07-20
 */
object IntentUtil {
    /**
     * 解析包类名
     *
     * @param packageClassName 包类名，有如下4种格式：
     * 1：包名/完整类名
     * 2：包名/.类名（自动补全包名）
     * 3：包名（返回类名为空）
     * 4：.类名（自动补全包名）
     * @return ComponentName
     */
    fun parsePackageClassName(packageClassName: String): ComponentName {
        if (packageClassName.isBlank()) return ComponentName("", "")
        val packageName: String
        val className: String
        if (packageClassName.startsWith('.')) {
            packageName = baseApplication.packageName
            className = packageName + packageClassName
        } else {
            val split = packageClassName.split('/')
            packageName = split[0]
            className = if (split.size > 1) {
                if (split[1].startsWith(".")) {
                    packageName + split[1]
                } else split[1]
            } else ""
        }
        return ComponentName(packageName, className)
    }
}

/**
 * 启动活动
 *
 * @param T       Activity类
 * @param context 上下文
 * @param block   代码块，用于设置启动的intent
 */
inline fun <reified T : Activity> openActivity(
    context: Context, block: Intent.() -> Unit = {}
) = context.startActivity(
    Intent(context, T::class.java).apply { block() })

/**
 * 启动活动
 *
 * @param context 上下文
 * @param packageClassName 包类名
 * @see IntentUtil.parsePackageClassName
 * @param block 代码块，用于设置启动的intent
 * @return 是否启动成功
 */
inline fun openActivity(
    context: Context, packageClassName: String, block: Intent.() -> Unit = {}
): Boolean {
    val componentName = IntentUtil.parsePackageClassName(packageClassName)

    val intent: Intent?
    if (componentName.className.isBlank()) {
        intent = context.packageManager
            .getLaunchIntentForPackage(componentName.packageName)
    } else {
        intent = Intent()
        intent.component = componentName
    }

    intent?.let {
        it.block()
        try {
            context.startActivity(it)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    return false
}

/**
 * 启动服务
 *
 * @param T       Service类
 * @param context 上下文
 * @param block   代码块，用于设置启动的intent
 */
inline fun <reified T : Service> openService(
    context: Context, block: Intent.() -> Unit = {}
): ComponentName? = context.startService(
    Intent(context, T::class.java).apply { block() })

/**
 * 启动活动
 *
 * @param context 上下文
 * @param packageClassName 包类名
 * @see IntentUtil.parsePackageClassName
 * @param block 代码块，用于设置启动的intent
 * @return 是否启动成功
 */
inline fun openService(
    context: Context, packageClassName: String, block: Intent.() -> Unit = {}
): Boolean {
    Intent().run {
        component = IntentUtil.parsePackageClassName(packageClassName)
        block()
        try {
            context.startService(this)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    return false
}

/**
 * 关闭服务
 *
 * @param T       Service类
 * @param context 上下文
 */
inline fun <reified T : Service> closeService(
    context: Context
) = context.stopService(Intent(context, T::class.java))
