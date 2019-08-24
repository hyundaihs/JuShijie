package com.android.jsj

import android.app.Application
import com.android.jsj.entity.ChooseType
import com.android.jsj.entity.SystemInfo
import com.android.jsj.entity.UserInfo
import com.tencent.rtmp.TXLiveBase
import kotlin.properties.Delegates

/**
 * ChaYin
 * Created by ${蔡雨峰} on 2018/8/15/015.
 */
class JSJApplication : Application() {

    companion object {
        var instance: JSJApplication by Delegates.notNull()
        var systemInfo = SystemInfo()
        var userInfo = UserInfo()
        var chooseType : Map<String, ArrayList<ChooseType>> = mapOf()
        var isUserinfoChange = false
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        val licenceURL = "http://license.vod2.myqcloud.com/license/v1/e13dad417f55b25480df2570667e663b/TXLiveSDK.licence" // 获取到的 licence url
        val licenceKey = "eee4d6dbe223769235373061b142318a" // 获取到的 licence key
        TXLiveBase.getInstance().setLicence(this, licenceURL, licenceKey)
    }
}
