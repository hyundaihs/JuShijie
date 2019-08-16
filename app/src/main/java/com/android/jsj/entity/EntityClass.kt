package com.android.jsj.entity

import com.android.shuizu.myutillibrary.request.RequestResult
import com.contrarywind.interfaces.IPickerViewData

/**
 * ChaYin
 * Created by ${蔡雨峰} on 2018/8/21/021.
 */
/*
分类标识
1 : 资讯来源
2 : 资讯类型
3 : 风格
4 : 品牌地区
5 : 装饰模式
6 : 建材材料类型
7 : 荣誉星级
8 : 家具家电类型
9 : 设计师入行时间
10 : 设计师级别
11 : 设计师费用
12 : 商品类型
13 : 装饰品牌房间类型
14 : 品牌类型
15 : 商户等级*/

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
[account_id]=> 账号id
[type_id] => 类型（2：普通商家，3：VIP商家）
[title] => 公司简称
[title2] => 公司全称
[pp_title] => 品牌名
[file_url] => 头像
[tsfw] => 特色服务（,间隔）
[address] => 地址
[contents] => 详情（html）
[gz_nums] => 关注数
[zan_nums] => 点赞数
[ft_nums] => 发帖数
[pp_id] => 品牌id
[pp_title] => 品牌名称
[pp_file_url] => 品牌logo
[ppInfo] => Array（品牌信息）
    (
        [id] => 品牌id
        [pp_title] => 宜家
        [pp_file_url] => 品牌logo
    )
[is_zan] => 是否点过赞
[is_gz] => 是否关注
[tags] => 标签（世界500强,优秀企业  ,隔开）
[zbj_id] => 直播间id 未申请为0
            [zbj_title] => 直播名称
            [zbj_zb_status] => 直播状态（0：未直播，1：正在直播，3：结束）
            [zbj_status] => 直播申请状态（0：未申请，1：申请中，2：已通过，3：未通过）
[zdyfl_lists] => Array（商家自定义分类）
               (
                   [0] => Array
                       (
                           [id] => 分类id
                           [title] => 分类名称
                       )
               )
*/
data class MerchantInfo(
    val id: Int,
    val account_id: Int,
    val type_id: Int,
    val title: String,
    val title2: String,
    val file_url: String,
    val tsfw: String,
    val address: String,
    val contents: String,
    val gz_nums: Int,
    val zan_nums: Int,
    val ft_nums: Int,
    val pp_id: Int,
    val pp_title: String,
    val pp_file_url: String,
    val tags: String,
    val ppInfo: PPInfo,
    val is_zan: Int,
    val is_gz: Int,
    val zbj_id: Int,
    val zbj_title:String,
    val zbj_zb_status:Int,
    val zbj_status:Int,
    val zdyfl_lists: ArrayList<ChooseType>
)

data class MerchantInfoRes(val retRes: MerchantInfo) : RequestResult()

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

/*[account_id] => 账号id
[account_title] => 公司简称
[account_file_url] => 公司头像
[id] => 直播id
[pp_id] => 品牌id
[zjr_title] => 主讲人
[file_url] => 封面图
[video_file_url] => 播放地址
[title] => 直播名称
[contents] => 课程介绍（html）
[ppfl_id] => 品牌类型id（1401:装饰品牌,1402:建材家具 对应分类标识 14）
[zbsc] => 时长（小时）
[start_time] => 开播时间戳
[end_time] => 结束时间戳
[sf_status] => 是否收费（0|1）
[price] => 价格
[zan_nums] => 点赞数
[sc_nums] => 收藏数
[pl_nums] => 评论数
[gz_nums] => 关注数
[view_nums] => 热度、查看人数
[is_zan] => 是否点过赞
[is_gz] => 是否关注
[create_time] => 创建时间戳
[zb_status] => 直播状态（0：未直播，1：正在直播，3：结束）
[gm_status] => 购买状态（1：已购买，0：未购买）*/

data class LiveInfo(
    val account_id: Int,
    val account_title: String,
    val account_file_url: String,
    val id: Int,
    val pp_id: Int,
    val zjr_title: String,
    val file_url: String,
    val video_file_url: String,
    val title: String,
    val contents: String,
    val ppfl_id: Int,
    val zbsc: Float,
    val start_time: Long,
    val end_time: Long,
    val sf_status: Int,
    val price: Float,
    val zan_nums: Int,
    val sc_nums: Int,
    val pl_nums: Int,
    val gz_nums: Int,
    val view_nums: Int,
    val is_zan: Int,
    val is_gz: Int,
    val create_time: Long,
    val zb_status: Int,
    val gm_status: Int
)

data class LiveInfoRes(val retRes: LiveInfo) : RequestResult()

data class LiveInfoListRes(val retRes: ArrayList<LiveInfo>) : RequestResult()

/*[id] => 分类id
[title] => 分类名称*/
data class ChooseType(val id: Int, val title: String) :
    IPickerViewData {
    override fun getPickerViewText(): String {
        return title
    }
}

data class ChooseTypeMapRes(val retRes: Map<String, ArrayList<ChooseType>>) : RequestResult()

/**
 * [type] => 操作结果（1：添加，0：取消）
 */
data class ZanResult(val type: Int)

data class ZanResultRes(val retRes: ZanResult) : RequestResult()

/*
[https_m3u8] => https://play.zb.idianjiao.com/live/jsj0000001.m3u8
[https_flv] => https://play.zb.idianjiao.com/live/jsj0000001.flv
[rtmp] => rtmp://play.zb.idianjiao.com/live/jsj0000001*/

data class PlayUrl(val https_m3u8: String, val https_flv: String, val rtmp: String)

data class PlayUrlRes(val retRes: PlayUrl) : RequestResult()

/*[account_id] => 账号id
[account_title] => 公司名称
[account_file_url] => 公司头像
[dj_id] => 公司等级id
[id] => 内容id
[mk_id] => 模块id（1：资讯，2：品牌荣誉，3：视频：4：设计团队，5：案例，6：特卖）
[create_time] => 发布时间戳
[is_zan] => 是否点过赞（0|1）
[is_sc] => 是否收藏过（0|1）
[is_gz] => 是否关注过（0|1）
[zan_nums] => 点赞数量
[view_nums] => 访问数量
[gz_nums] => 关注数


[img_lists] => Array(图片列表)
(
[0] => Array
(
[id] => 文件id
[file_url] => 原图
[resize_file_url] => 缩略图
[file_size] => 文件大小
)

)
 品牌荣誉字段
                    [file_url] => 图片
                    [title] => 标题

                    案例字段
                    [file_url] => 图片
                    [title] => 标题
                    [sjs_id] => 设计师id
                    [sjs_title] => 设计师标题
                    [sjsdj_id] => 设计师等级id

                    视频字段
                    [file_url] => 图片
                    [video_file_url] => 视频地址
                    [title] => 标题
                    [sub_info] => 简介

                    资讯字段
                    [type_id] => 信息源id（1：新闻，2：活动，3：推广，4：饰界 ）(对应系统分类标识 1)
                    [file_url] => 图片
                    [title] => 标题
                    [zan_nums] => 点赞数量
                    [view_nums] => 访问数量

                    设计团队字段
                    [file_url] => 图片
                    [title] => 标题
                    [sjsdj_id] => 设计师等级id
                    [jy] => 经验id
                    [al_num] => 作品案例数

                    特卖字段
                    [file_url] => 图片
                    [title] => 标题
                    [price] => 现价
                    [price2] => 原价

                    商家自定义内容
                    [title] => 标题
                    [file_url] => 图片
                    [sub_info] => 简介
*/

data class FileInfo(val id: Int, val file_url: String, val resize_file_url: String, val file_size: String)

data class CompanyInfo(
    var account_id: Int,
    var account_title: String,
    var account_file_url: String,
    val title: String,
    val file_url: String,

    val sjs_id: Int,
    val sjs_title: String,
    val bj_file_url: String,
    val sjsdj_id: Int,
    val fengge_ids: String,
    val fengge_title: String,

    val video_file_url: String,
    val sub_info: String,
    val pp_title: String,
    val xinghao: String,
    val yanse: String,
    val chicun: String,

    val type_id: Int,

    val jy: Int,
    val al_num: Int,

    val price: String,
    val price2: String,

    var dj_id: Int,
    var id: Int,
    var mk_id: Int,
    var create_time: Long,
    var is_zan: Int,
    var is_sc: Int,
    var is_gz: Int,
    var zan_nums: Int,
    var view_nums: Int,
    var gz_nums: Int,
    val contents: String,
    val tags: String,
    val price_str: String,
    val address: String,
    val pl_nums: Int,
    var img_lists: ArrayList<FileInfo>
)

data class CompanyInfoRes(val retRes: CompanyInfo) : RequestResult()

data class CompanyInfoListRes(val retRes: ArrayList<CompanyInfo>) : RequestResult()

/*
[id] => 评论id
[title] => 评论详情
[create_time] => 时间戳
[account_id] => 发布人账号id
[account_title] => 发布人昵称
[account_file_url] => 发布人头像
[hf_account_title] => 回复对象昵称
[hf_account_file_url] => 回复对象头像
[zan_nums] => 点赞数
[pl_nums] => 回复数
[my] => 是否是自己发布的（0|1）
[hf_lists] => Array（回复列表）
(
[0] => Array
(
[id] => 评论id
[title] => 评论详情
[create_time] => 时间戳
[account_id] => 发布人账号id
[account_title] => 发布人昵称
[account_file_url] => 发布人头像
[hf_account_title] => 回复对象昵称
[hf_account_file_url] => 回复对象头像
[zan_nums] => 点赞数
[pl_nums] => 回复数
[my] => 是否是自己发布的（0|1）
)

)
*/
open class Reviews(
    val id: Int = 0,
    val title: String = "",
    val create_time: Long = 0,
    val account_id: Int = 0,
    val account_title: String = "",
    val account_file_url: String = "",
    val hf_account_title: String = "",
    val hf_account_file_url: String = "",
    val zan_nums: Int = 0,
    val pl_nums: Int = 0,
    val my: Int = 0
)

data class ReviewsInfo(val hf_lists: ArrayList<Reviews>) : Reviews()

data class ReviewsInfoListRes(var retRes: ArrayList<ReviewsInfo>) : RequestResult()