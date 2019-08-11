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
        var systemInfo: SystemInfo by Delegates.notNull()
        var userInfo:UserInfo by Delegates.notNull()
        var chooseType:Map<String, ArrayList<ChooseType>> by Delegates.notNull()
        var isUserinfoChange = false
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        val licenceURL = "http://license.vod2.myqcloud.com/license/v1/01de8f682ffae40cd8cf0a43ff6a4659/TXLiveSDK.licence" // 获取到的 licence url
        val licenceKey = "a5a008b95c078460c037f483f783dc0d" // 获取到的 licence key
        TXLiveBase.getInstance().setLicence(this, licenceURL, licenceKey)
    }
}
