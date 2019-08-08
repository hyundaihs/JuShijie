package com.android.jsj

import android.app.Application
import com.android.jsj.entity.SystemInfo
import kotlin.properties.Delegates

/**
 * ChaYin
 * Created by ${蔡雨峰} on 2018/8/15/015.
 */
class JSJApplication : Application() {

    companion object {
        var instance: JSJApplication by Delegates.notNull()
        var systemInfo: SystemInfo by Delegates.notNull()
        var isUserinfoChange = false
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}
