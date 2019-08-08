package com.android.jsj.entity

import com.android.shuizu.myutillibrary.request.RequestResult

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

class AccountType{
    companion object {
        /**
         * 普通用户
         */
        const val USER= 1
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

class ApplyStatus{
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

data class PPInfo(val id: Int,var pp_title:String,var pp_file_url:String)

data class Userinfo(val id: Int,var account: String,var type_id:Int,var file_url: String,var area:String,var city:String,var province:String,var login_time:Long,var create_time: Long,
                    var fs_nums : Int,var gz_nums:Int,var zan_nums:Int,var ft_nums:Int,var sjsq_status:Int,var ppInfo: PPInfo)

data class UserinfoRes(var retRes: Userinfo) : RequestResult()

/*[login_verf] => 自动登录密码*/
data class LoginInfo(var login_verf: String)

data class LoginInfoRes(var retRes: LoginInfo) : RequestResult()

/*[v_title] => 版本名称
[v_num] => 版本号码
[http_url] => 下载地址*/
data class VersionInfo(var v_title: String, var v_num: Int, var http_url: String)

data class VersionInfoRes(var retRes: VersionInfo) : RequestResult()

/*[id] => 鱼缸id
[title] => 名称
[create_time] => 创建时间（时间戳）
[counts] => 设备数量
*/
data class YGInfo(var id: Int, var title: String, var create_time: Long, var counts: Int)

data class YGInfoRes(var retRes: YGInfo) : RequestResult()

data class YGInfoListRes(var retRes: ArrayList<YGInfo>) : RequestResult()

var DEVICE_IS_ONLINE = arrayOf("离线", "在线")

/*[id] => 设备id
[acccardtype_id] => 鱼缸id（0为常用设备）
[card_type] => 设备类型（“TR”:水质监测，“HT”:加热棒，“WP”：水泵，“PF”：断电报警器）
[title] => 设备名称
[create_time] => 绑定时间（时间戳）
[is_online] => 设备是否在线（0：离线，1：在线）*/
data class MyDevice(
    var id: Int, var acccardtype_id: Int, var card_type: String,
    var title: String, var create_time: Long, var is_online: Int
)

data class MyDeviceListRes(var retRes: ArrayList<MyDevice>) : RequestResult()

var WATER_LEVEL = arrayOf("无", "极优", "优良", "良", "一般", "差")

/*[is_online] => 设备是否在线（0：离线，1：在线）
[wd] => 温度
[ph] => PH
[tds] => TDS
[wd_zl] => 温度质量（0：正常，1：偏低，2：偏高）
[ph_zl] => PH质量（0：正常，1：偏低，2：偏高）
[tds_zl] => TDS质量（0：正常，1：偏低，2：偏高）
[zl] => 水质级别（0:无，1：极优，2：优良，3:良，4：一般，5：差）*/

data class WaterMonitorInfo(
    var is_online: Int,
    var wd: Float,
    var ph: Float,
    var tds: Int,
    var wd_zl: Int,
    var ph_zl: Int,
    var tds_zl: Int,
    var zl: Int
)

data class WaterMonitorInfoRes(var retRes: WaterMonitorInfo) : RequestResult()

/*[x] => x轴（时间戳）
[y] => y轴（数值）*/
data class WaterHistoryData(var x: Long, var y: Float)

data class WaterHistoryDataRes(var retRes: ArrayList<WaterHistoryData>) : RequestResult()

/*acccardtype_id：分组id（0为常用设备）
ids：设备id数组（[1,2,3]）*/
data class PostDeviceIds(var acccardtype_id: Int, var ids: ArrayList<Int>)

/*[phjz_status] => 状态（0：未校准，1：开始校准，2：第一次校准成功，3：第二次开始校准，4：校准成功，5：校准失败）
[phjz_time] => 校准时间（时间戳）*/
data class PHJZ_Info(var phjz_status: Int, var title: String, var phjz_time: Long)

data class PHJZ_InfoRes(var retRes: PHJZ_Info) : RequestResult()

/*[sz_status] => 设置状态（0:未设置，1：正在设置，2：设置成功，3：设置失败）
[sz_time] => 设置时间
[v_1] => 极度低温值
[v_2] => 低温值
[v_3] => 高温值
[v_4] => 极度高温值*/
data class BaoJin_Info(
    var sz_status: Int,
    var sz_time: Long,
    var v_1: Float,
    var v_2: Float,
    var v_3: Float,
    var v_4: Float
)

data class BaoJin_InfoRes(var retRes: BaoJin_Info) : RequestResult()

/*[id] => 日志id
[true_varue] => 报警值
[create_time] => 时间（时间戳）
[title] => 标题（鱼缸水温达到极限26.2摄氏度）*/
data class WarnLog(var id: Int, var true_varue: Float, var create_time: Long, var title: String)

data class WarnLogListRes(var retRes: ArrayList<WarnLog>) : RequestResult()

/*[id] => 用户id
[account] => 账号
[title] => 昵称
[file_url] => 头像
[ts_status] => 推送接收状态（0：关闭，1：开启）*/
data class UserInfo(var id: Int, var account: String, var title: String, var file_url: String, var ts_status: Int)

data class UserInfoRes(var retRes: UserInfo) : RequestResult()

data class UploadInfo(var file_url: String)

data class UploadInfoRes(var retRes: UploadInfo) : RequestResult()

data class UploadInfoListRes(var retRes: ArrayList<String>) : RequestResult()

/*[id] => ID
[title] => 标题
[file_url] => 图片
[sub_title] => 简介
[biaoqian] => 标签（医龄：6年）
[zhuanye] => 专业（鱼类专家
[app_contents] => 详情（html代码）*/
data class YYZJInfo(
    var id: Int, var title: String, var file_url: String, var sub_title: String
    , var biaoqian: String, var zhuanye: String, var app_contents: String, var tags: String
)

data class YYZJInfoRes(var retRes: YYZJInfo) : RequestResult()

data class YYZJInfoListRes(var retRes: ArrayList<YYZJInfo>) : RequestResult()

/*[id] => ID
[title] => 标题
[sub_title] => 简介
[contents] => 详情
[create_time] => 时间（时间戳）
 [img_file_urls] => 图片数组['public/1.jpg','public/2.jpg']*/
data class FishLog(
    var id: Int, var title: String, var sub_title: String, var contents: String, var create_time: Long
    , var file_url: String, var img_file_urls: ArrayList<String>
)

data class FishLogRes(var retRes: FishLog) : RequestResult()

data class FishLogListRes(var retRes: ArrayList<FishLog>) : RequestResult()

/*[id] => ID
[title] => 标题
[file_url] => 图片
[sub_title] => 简介
[app_contents] => 详情（html代码）
后面两个是消息中心消息的字段
create_time => 创建时间
is_read => 是否已读(0：未读，1：已读)*/
data class FishKnowledge(
    var id: Int, var title: String, var file_url: String, var sub_title: String, var app_contents: String
    , var create_time: Long, var is_read: Int
)

data class FishKnowledgeRes(var retRes: FishKnowledge) : RequestResult()

data class FishKnowledgeListRes(var retRes: ArrayList<FishKnowledge>) : RequestResult()

/*[id] => ID
[contents] => 内容
[tx_time] => 提醒时间戳
[create_time] => 创建时间戳*/
data class MemoSetInfo(var id: Int, var title: String, var contents: String, var tx_time: Long, var create_time: Long)

data class MemoSetInfoRes(var retRes: MemoSetInfo) : RequestResult()

data class MemoSetInfoListRes(var retRes: ArrayList<MemoSetInfo>) : RequestResult()

/*[card_type] => TR
[title] => 设备类型（水质监测）
[nums] => 数量*/
data class Statistics(var card_type: String, var title: String, var nums: Int)

/*[ygsl] => 鱼缸数量
[sbsl] => 设备数量
[sbfb] => Array（设备分类详情）
(
[0] => Array
(
Statistics
)
)

[czsl] => 操作数量
[bjsl] => 报警数量*/
data class BigStatistics(
    var ygsl: Int,
    var sbsl: Int,
    var ycsb: Int,
    var sbfb: ArrayList<Statistics>,
    var czsl: Int,
    var bjsl: Int
)

data class BigStatisticsRes(var retRes: BigStatistics) : RequestResult()

/*[id] => ID
[title] => 标题
[file_url] => 图片
[sub_title] => 简介
[app_contents] => 详情（html代码）*/
data class Instructions(
    var id: Int,
    var title: String,
    var file_url: String,
    var sub_title: String,
    var app_contents: String
)

data class InstructionsRes(var retRes: Instructions) : RequestResult()

data class InstructionsListRes(var retRes: ArrayList<Instructions>) : RequestResult()

/*[id] => 预约id
numbers => 预约编号
[yyzj_id] => 专家id
[yyzj_title] => 专家名称
[yyzj_file_url] => 专家头像
[link_man] => 联系人
[link_phone] => 联系电话
[contents] => 内容
[img_file_urls] => Array（图片列表）
(
)

[video_file_urls] => Array（视频列表）
(
)

[status] => 预约状态（1：未处理，2：已处理，3：作废）
[create_time] => 预约时间（时间戳）*/
data class Reservations(
    var id: Int,
    var numbers: String,
    var yyzj_id: Int,
    var yyzj_title: String,
    var yyzj_file_url: String,
    var link_man: String,
    var link_phone: String
    ,
    var contents: String,
    var img_file_urls: ArrayList<String>,
    var video_file_urls: ArrayList<String>
    ,
    var status: Int,
    var create_time: Long
)

data class ReservationsRes(var retRes: Reservations) : RequestResult()

data class ReservationsListRes(var retRes: ArrayList<Reservations>) : RequestResult()


