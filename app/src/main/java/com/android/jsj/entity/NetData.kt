package com.android.jsj.entity

import android.text.TextUtils
import android.util.Log
import com.android.shuizu.myutillibrary.utils.CalendarUtil
import com.google.gson.Gson
import java.security.MessageDigest

/**
 * ChaYin
 * Created by ${蔡雨峰} on 2018/8/21/021.
 */
const val TEST_DEVICE_ID = "TR-005D16660016402020E42DE4"

const val ROOT_URL = "http://zxzb.hyk001.com"
const val INTERFACE_INDEX = "/api.php/Index/"
const val FROM = "/from/android"
const val KEY_STR = "/keystr/"
const val AREA = "$ROOT_URL/static/pca.json"

fun String.getImageUrl(): String {
    return if (this.contains("http")) this else ROOT_URL + "/" + this
}

fun String.getInterface(map: Map<String, Any>? = null): String {
    var jsonStr = ""
    if(map != null){
        jsonStr = Gson().toJson(map)
    }
    val keyStr = getKeyStr(jsonStr, this)
    Log.d("md",keyStr)
    return ROOT_URL + INTERFACE_INDEX + this + FROM + KEY_STR + keyStr
}

fun getKeyStr(jsonStr: String, inter: String): String {
    return md5(jsonStr + "nimdaae" + inter + CalendarUtil(System.currentTimeMillis()).format(CalendarUtil.YYYYMMDD))
}

private fun md5(string: String): String {
    if (TextUtils.isEmpty(string)) {
        return ""
    }
    val md5: MessageDigest = MessageDigest.getInstance("MD5")
    val bytes = md5.digest(string.toByteArray())
    var result = ""
    val c = 0xff
    for (i in bytes.indices) {
        var temp = Integer.toHexString(c and bytes[i].toInt())
        if (temp.length == 1) {
            temp = "0$temp"
        }
        result += temp
    }
    return result
}

const val UPLOADFILE = "uploadfile"//文件上传
/*phone：手机号码*/
const val SEND_VERF = "sendverf" //发送短信验证码

/*account：手机号码
verf:短信验证码
password：密码*/
const val REG = "reg" //注册

/*account：手机号码
password：密码*/
const val LOGIN = "login" //登录

/*login_verf：自动登录密码*/
const val VERF_LOGIN = "verflogin" //自动登录密码登录

/*account：手机号
verf：短信验证码
password：密码*/
const val RESET_PASS = "resetpass" //修改密码

const val USER_INFO = "userinfo" //用户信息

/*title：昵称
file_url：头像文件路径（为空时不修改，通过文件上传接口得到）
ts_status:推送接收状态（0：关闭，1：开启）*/
const val SET_INFO = "setinfo" //修改用户信息

const val SYS_INFO = "sysinfo" //获取系统介绍信息

const val BANNER = "banner" //获取Banner图片

const val BANNERINFO = "bannerinfo" //获取banner详情

const val ONLINEINFO = "onlineinfo" //在线人数

const val CHANGEPCA = "changepca" //修改地址

const val SJLB = "sjlb" //商家列表
const val PPLISTS = "pplists" //品牌列表

const val MSGLISTS = "msglists" //系统通知列表
const val MSGINFO = "msginfo" //系统通知详情
const val SETINFO = "setinfo" //设置用户信息
const val TJSJSQ = "tjsjsq" //提交商家申请
const val ZBLB = "zblb" //直播列表
const val ZBINFO = "zbinfo" //直播详情
const val STYPE = "stype" //分类列表
const val TJZBZFPZ = "tjzbzfpz" //提交直播支付凭证
const val NRCZ = "nrcz" //点赞收藏关注

