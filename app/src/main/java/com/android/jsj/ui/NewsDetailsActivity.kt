package com.android.jsj.ui

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.view.View
import android.widget.TextView
import com.android.jsj.R
import com.android.jsj.adapters.OnZanPinClickListener
import com.android.jsj.adapters.ReviewsAdapter
import com.android.jsj.entity.*
import com.android.jsj.util.dianZan
import com.android.shuizu.myutillibrary.MyBaseActivity
import com.android.shuizu.myutillibrary.adapter.LineDecoration
import com.android.shuizu.myutillibrary.initActionBar
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.android.shuizu.myutillibrary.utils.getErrorDialog
import com.android.shuizu.myutillibrary.utils.initImageAuto
import com.cazaea.sweetalert.SweetAlertDialog
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_news_details.*
import kotlinx.android.synthetic.main.layout_list_empty.*
import org.jetbrains.anko.toast

/**
 * JuShijie
 * Created by 蔡雨峰 on 2019/8/15.
 */
class NewsDetailsActivity : MyBaseActivity(), OnZanPinClickListener {

    override fun onZanClickListener(view: View, position: Int) {
        val re = reviewsInfo[position]
        dianZan(re.id, "pllog", "zan", object : KevinRequest.SuccessCallback {
            override fun onSuccess(context: Context, result: String) {
                val zanResultRes = Gson().fromJson(result, ZanResultRes::class.java)
                val text = view as TextView
                val o = text.text.toString().toInt()
                if (zanResultRes.retRes.type == 1) {
                    view.text = (o + 1).toString()
                } else {
                    view.text = (o - 1).toString()
                }
            }
        })
    }

    override fun onPinClickListener(view: View, position: Int) {
        val re = reviewsInfo[position]
        pinLunId = re.id
        pingLunInfo.hint = "回复${re.title}:"
        isReply = true
    }

    companion object {
        var aId = 0
    }

    private lateinit var companyInfo: CompanyInfo
    private var reviewsInfo = ArrayList<ReviewsInfo>()
    private var adapter = ReviewsAdapter(reviewsInfo,this)

    private var pinLunId = 0
    private var isReply = false //回复还是评论

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_details)
        initActionBar(this, "")
        getNewsDetails()
    }

    private fun getNewsDetails() {
        val map = mapOf(
            Pair("id", aId)
        )
        KevinRequest.build(this).apply {
            setRequestUrl(NEWSINFO.getInterface(map))
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error,
                        SweetAlertDialog.OnSweetClickListener { finish() })
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    companyInfo = Gson().fromJson(result, CompanyInfoRes::class.java).retRes
                    initViews()
                }
            })
            openLoginErrCallback(LoginActivity::class.java)
            setDataMap(map)
            setDialog()
            postRequest()
        }
    }

    private fun initViews() {
        initActionBar(this, companyInfo.title)
        companyTitle.text = companyInfo.title
        zan.text = companyInfo.zan_nums.toString()
        gz.text = companyInfo.gz_nums.toString()
        pl.text = companyInfo.pl_nums.toString()

        content.initImageAuto()
        content.loadData(companyInfo.contents,"text/html; charset=UTF-8", null)
        val layoutManager = LinearLayoutManager(this)
        pingLun.layoutManager = layoutManager
        layoutManager.orientation = OrientationHelper.VERTICAL
        pingLun.addItemDecoration(LineDecoration(this, LineDecoration.VERTICAL))
        pingLun.itemAnimator = DefaultItemAnimator()
        pingLun.isNestedScrollingEnabled = false
        pingLun.setEmptyView(emptyView)
        pingLun.adapter = adapter
        getPingLun()
        send.setOnClickListener {
            send()
        }
        pl.setOnClickListener {
            isReply = false
            pingLunInfo.hint = "请输入评论信息"
        }

        zan.setOnClickListener {
            dianZan(WorldDetailsActivity.aId, "news", "zan", object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    val zanResultRes = Gson().fromJson(result, ZanResultRes::class.java)
                    val o = zan.text.toString().toInt()
                    if (zanResultRes.retRes.type == 1) {
                        zan.text = (o + 1).toString()
                    } else {
                        zan.text = (o - 1).toString()
                    }
                }
            })
        }
    }

    private fun getPingLun() {
        val map = mapOf(
            Pair("id", aId)
        )
        KevinRequest.build(this).apply {
            setRequestUrl(NRPLLISTS.getInterface(map))
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error,
                        SweetAlertDialog.OnSweetClickListener { finish() })
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    reviewsInfo.clear()
                    reviewsInfo.addAll(Gson().fromJson(result, ReviewsInfoListRes::class.java).retRes)
                    adapter.notifyDataSetChanged()
                }
            })
            setDataMap(map)
            postRequest()
        }
    }

    private fun send() {
        if (pingLunInfo.text.isEmpty()) {
            toast("请输入评论信息")
            return
        }

        val map = mapOf(
            Pair("id", aId),
            Pair("title", pingLunInfo.text.toString())
        ).toMutableMap()
        if (isReply) {
            map.put("pl_id", pinLunId)
        }
        KevinRequest.build(this).apply {
            setRequestUrl(NRPLADD.getInterface(map))
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error,
                        SweetAlertDialog.OnSweetClickListener { finish() })
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    toast("发送成功")
                    pingLunInfo.setText("")
                    getPingLun()
                }
            })
            setDataMap(map)
            postRequest()
        }
    }
}