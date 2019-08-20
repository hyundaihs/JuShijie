package com.android.jsj.util

import android.content.Context
import com.android.jsj.entity.GETPLAYURL
import com.android.jsj.entity.NRCZ
import com.android.jsj.entity.TJZBZFPZ
import com.android.jsj.entity.getInterface
import com.android.jsj.ui.LoginActivity
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.android.shuizu.myutillibrary.utils.getErrorDialog
import com.android.shuizu.myutillibrary.utils.getSuccessDialog
import com.cazaea.sweetalert.SweetAlertDialog
import org.jetbrains.anko.toast

/**
 * JuShijie
 * Created by 蔡雨峰 on 2019/8/12.
 */
/**
 * id：内容id/账号id
 * table_name:内容类型【account：公司，news：荣誉，案例，视频，资讯，设计团队，特卖，pllog：评论，accountzbj：直播】
 * type_str:操作类型（zan：点赞，sc：收藏，gz：关注）
 */

var startTime = 0L

fun Context.dianZan(id: Int, table_name: String, type_str: String, c: KevinRequest.SuccessCallback) {
    if (System.currentTimeMillis() - startTime < 2000) {
        toast("点的太快了")
        return
    }
    startTime = System.currentTimeMillis()
    val map = mapOf(
        Pair("id", id),
        Pair("table_name", table_name),
        Pair("type_str", type_str)
    )
    KevinRequest.build(this).apply {
        setRequestUrl(NRCZ.getInterface(map))
        setErrorCallback(object : KevinRequest.ErrorCallback {
            override fun onError(context: Context, error: String) {
                getErrorDialog(context, error)
            }
        })
        setSuccessCallback(c)
        setDataMap(map)
        postRequest()
    }
}

fun Context.getPlayUrl(id: Int, c: KevinRequest.SuccessCallback) {
    val map = mapOf(
        Pair("id", id)
    )
    KevinRequest.build(this).apply {
        setRequestUrl(GETPLAYURL.getInterface(map))
        setErrorCallback(object : KevinRequest.ErrorCallback {
            override fun onError(context: Context, error: String) {
                getErrorDialog(context, error)
            }
        })
        setSuccessCallback(c)
        setDataMap(map)
        postRequest()
    }
}
