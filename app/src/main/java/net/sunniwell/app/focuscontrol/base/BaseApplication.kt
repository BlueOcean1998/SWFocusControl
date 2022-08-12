package net.sunniwell.app.focuscontrol.base

import android.app.Application
import androidx.multidex.MultiDexApplication

lateinit var baseApplication: Application

/**
 * 基础应用
 *
 * @author YSK
 * @date 2022-03-30
 */
open class BaseApplication : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        baseApplication = this
    }
}