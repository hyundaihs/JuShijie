package com.android.jsj.ui

import android.content.Context
import android.os.Bundle
import com.android.jsj.JSJApplication.Companion.systemInfo
import com.android.jsj.R
import com.android.jsj.entity.*
import com.android.shuizu.myutillibrary.MyBaseActivity
import com.android.shuizu.myutillibrary.initActionBar
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.android.shuizu.myutillibrary.request.getErrorDialog
import com.android.shuizu.myutillibrary.utils.CalendarUtil
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_live_details.*

/**
 * ChaYin
 * Created by ${蔡雨峰} on 2019/8/11/011.
 */
class LiveDetailsActivity : MyBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_details)
        initActionBar(this, "直播详情")
        getDetails(intent.getIntExtra("id", 0))
    }

    private fun getDetails(id: Int) {
        val map = mapOf(
            Pair("id", id.toString())
        )
        KevinRequest.build(this).apply {
            setRequestUrl(ZBINFO.getInterface(map))
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error)
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    val liveInfoRes = Gson().fromJson(result, LiveInfoRes::class.java)
                    initViews(liveInfoRes.retRes)
                }
            })
            openLoginErrCallback(LoginActivity::class.java)
            setDataMap(map)
            setDialog()
            postRequest()
        }
    }

    private fun initViews(liveInfo: LiveInfo) {
        Picasso.with(this).load(liveInfo.file_url.getImageUrl()).into(image)
        liveTitle.text = liveInfo.title
        person.text = "主讲：${liveInfo.zjr_title}"
        price.text = liveInfo.price.toString()
        time.text = "时间：${CalendarUtil(liveInfo.start_time, true).format(CalendarUtil.MM_DD_HH_MM)} " +
                "~ ${CalendarUtil(liveInfo.end_time, true).format(CalendarUtil.MM_DD_HH_MM)}"
        contents.loadData(liveInfo.contents, "text/html; charset=UTF-8", null)
        bank.text = "开户银行：${systemInfo.bank_zh}"
        bankAccountNum.text = systemInfo.bank_yhkh
    }
}