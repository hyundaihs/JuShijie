package com.android.jsj.entity

import com.android.shuizu.myutillibrary.request.RequestResult
import com.contrarywind.interfaces.IPickerViewData
import com.google.gson.Gson
import com.google.gson.JsonParser
import java.lang.reflect.Type

/**
 * ChaYin
 * Created by ${蔡雨峰} on 2018/8/21/021.
 */

class App_Actions {
    companion object {
        const val ACTION_WATER_MONITOR = "action.water_monitor"
    }
}

class App_Keyword {
    companion object {
        const val LOGIN_VERF = "login_verf"
        const val LOGIN_ACCOUNT = "login_account"
        const val IS_SET_ALIAS = "isSetAlias"

    }
}

/*[title] => 系统名称
[link_man] => 联系人
[telphone] => 联系电话
[qq] => QQ
[email] => 55555@qq.com
[address] => 地址
[company_right] => 版权
[beian_info] => 备案号
[gzh_file_url] => 公众号二维码
[zcxy] => 注册协议(html)
[bank_khh] => 建设银行
[bank_zh] => 建设银行光谷支行
[bank_zhhh] => 支行行号
[bank_yhkh] => 银行卡号
[bank_khmc] => 开户名称*/
data class SystemInfo(
    var title: String,
    var link_man: String,
    var telphone: String,
    var qq: String,
    var email: String,
    var address: String,
    var company_right: String,
    var beian_info: String,
    var gzh_file_url: String,
    var zcxy: String,
    var bank_khh: String,
    var bank_zh: String,
    var bank_zhhh: String,
    var bank_yhkh: String,
    var bank_khmc: String
)

data class SystemInfoRes(var retRes: SystemInfo) : RequestResult()

class AccountType {
    companion object {
        /**
         * 普通用户
         */
        const val USER = 1
        /**
         * 商家用户
         */
        const val MERCHANT = 2
        /**
         * VIP商家用户
         */
        const val VIP = 3
    }
}

class ApplyStatus {
    companion object {
        /**
         * 未申请
         */
        const val APPLY_NO = 0
        /**
         * 申请中
         */
        const val APPLY_PRO = 1
        /**
         * 申请通过
         */
        const val APPLY_PASS = 2
        /**
         * 申请拒绝
         */
        const val APPLY_DECLINE = 3
    }
}

/*[id] => 账号id
[account] => 账号
[type_id] => 类型（1：普通用户，2：商家，3：vip商家）
[title] => 昵称/公司名称
[file_url] => 头像
[area] => 江夏区
[city] => 武汉
[province] => 湖北
[login_time] => 登录时间戳
[create_time] => 注册时间戳
[fs_nums] => 粉丝数
[gz_nums] => 关注数
[zan_nums] => 点赞数
[ft_nums] => 发帖数
[sjsq_status] => 商家申请状态（0：未申请，1：申请中，2：已通过，3：拒绝）
[ppInfo] => Array（品牌信息）
(
[id] => 品牌id
[pp_title] => 宜家
[pp_file_url] => 品牌logo
)*/

data class PPInfo(val id: Int, var pp_title: String, var pp_file_url: String)

data class UserInfo(
    val id: Int,
    var account: String,
    var type_id: Int,
    var title: String,
    var file_url: String,
    var area: String,
    var city: String,
    var province: String,
    var login_time: Long,
    var create_time: Long,
    var fs_nums: Int,
    var gz_nums: Int,
    var zan_nums: Int,
    var ft_nums: Int,
    var sjsq_status: Int,
    var ppInfo: PPInfo
)

data class UserInfoRes(var retRes: UserInfo) : RequestResult()

/*[code] => 110000
[title] => 北京
[ykt] => 地区是否开通（0|1）
[lists] => Array*/
data class AreaInfo(val code: String, val title: String, val ykt: Int)

data class CityInfo(val code: String, val title: String, val ykt: Int, var lists: ArrayList<AreaInfo>)

data class ProvInfo(val code: String, val title: String, val ykt: Int, var lists: ArrayList<CityInfo>) :
    IPickerViewData {
    override fun getPickerViewText(): String {
        return title
    }
}

/*[id] => 广告图id
[file_url] => 图片
[contents] => 详情（html）*/
data class BannerInfo(val id: Int, val file_url: String, val contents: String, val account_id: Int)

data class BannerInfoRes(var retRes: BannerInfo) : RequestResult()

data class BannerInfoListRes(var retRes: ArrayList<BannerInfo>) : RequestResult()

/*[login_verf] => 自动登录密码*/
data class LoginInfo(var login_verf: String)

data class LoginInfoRes(var retRes: LoginInfo) : RequestResult()

/*[v_title] => 版本名称
[v_num] => 版本号码
[http_url] => 下载地址*/
data class VersionInfo(var v_title: String, var v_num: Int, var http_url: String)

data class VersionInfoRes(var retRes: VersionInfo) : RequestResult()


data class UploadInfo(var file_url: String)

data class UploadInfoRes(var retRes: UploadInfo) : RequestResult()

data class UploadInfoListRes(var retRes: ArrayList<String>) : RequestResult()

/*
[province] => 当前省
[city] => 当前市
[area] => 当前区
[all_num] => 总在线人数
[online_num] => 当前市在线人数*/

data class OnlineNumInfo(
    val province: String,
    val city: String,
    val area: String,
    val all_num: Int,
    val online_num: Int
)

data class OnlineNumInfoRes(var retRes: OnlineNumInfo) : RequestResult()

/*[id] => 账号id
[title] => 昵称/公司简称
[pp_title] => 品牌名
[file_url] => 头像
[gz_nums] => 关注数
[zan_nums] => 点赞数
[ft_nums] => 发帖数
[pp_id] => 品牌id
[pp_title] => 品牌名称
[pp_file_url] => 品牌logo
[tags] => 标签（世界500强,优秀企业  ,隔开）*/
data class MerchantInfo(
    val id: Int, val title: String, val file_url: String, val gz_nums: Int, val zan_nums: Int,
    val ft_nums: Int, val pp_id: Int, val pp_title: String, val pp_file_url: String, val tags: String
)

data class MerchantInfoListRes(val retRes: ArrayList<MerchantInfo>) : RequestResult()

/*[id] => 通知id
[type_str] => 通知类型（登录成功）
[from_title] => 通知来源（系统通知）
[title] => 通知标题（登录成功！）
[contents] => 通知详情
[is_read] => 是否已读（0|1）
[create_time] => 时间戳*/

data class MessageInfo(
    val id: Int,
    val type_str: String,
    val from_title: String,
    val title: String,
    val contents: String,
    val is_read: Int,
    val create_time: Long
)

data class MessageInfoRes(val retRes: MessageInfo) : RequestResult()

data class MessageInfoListRes(val retRes: ArrayList<MessageInfo>) : RequestResult()

/*[id] => 文件id
[file_url] => 文件地址
[title] => 文件名称
[file_size] => 文件大小
[file_ext] => 文件后缀
[resize_file] => 缩略图地址*/
data class UploadFileInfo(
    val id: Int,
    val file_url: String,
    val title: String,
    val file_size: String,
    val file_ext: String,
    val resize_file: String
)

data class UploadFileInfoRes(var retRes: UploadFileInfo) : RequestResult()
