package com.android.jsj.ui

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.view.View
import android.widget.ImageView
import com.android.jsj.R
import com.android.jsj.adapters.ReviewsAdapter
import com.android.jsj.entity.*
import com.android.shuizu.myutillibrary.MyBaseActivity
import com.android.shuizu.myutillibrary.adapter.LineDecoration
import com.android.shuizu.myutillibrary.initActionBar
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.android.shuizu.myutillibrary.utils.getErrorDialog
import com.bumptech.glide.Glide
import com.cazaea.sweetalert.SweetAlertDialog
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer
import kotlinx.android.synthetic.main.activity_video_details.*
import kotlinx.android.synthetic.main.layout_list_empty.*
import org.jetbrains.anko.toast

/**
 * JuShijie
 * Created by 蔡雨峰 on 2019/8/15.
 */
class VideoDetailsActivity : MyBaseActivity() {

    companion object {
        var aId = 0
    }

    private lateinit var companyInfo: CompanyInfo
    private var reviewsInfo = ArrayList<ReviewsInfo>()
    private var adapter = ReviewsAdapter(reviewsInfo)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_details)
        initActionBar(this, "视频详情")
        getVideoDetails()
    }

    private fun getVideoDetails() {
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
                    reviewsInfo.addAll(Gson().fromJson(result, ReviewsInfoListRes::class.java).retRes)
                    adapter.notifyDataSetChanged()
                }
            })
            setDataMap(map)
            postRequest()
        }
    }

    private var isfirststart = true

    private fun initViews() {
        val setUp = videoView.setUp(companyInfo.video_file_url.getImageUrl(), JCVideoPlayer.SCREEN_WINDOW_TINY, companyInfo.title)
        if (setUp) {
            videoView.thumbImageView.scaleType = ImageView.ScaleType.FIT_XY
            Glide.with(this).load(companyInfo.file_url.getImageUrl()).into(videoView.thumbImageView)
        }
//        videoView.setVideoPath(companyInfo.video_file_url.getImageUrl())
//        videoView.requestFocus()
//        videoView.setOnPreparedListener {
//            if (isfirststart) {
//                isfirststart = false
//                Picasso.with(this).load(companyInfo.file_url.getImageUrl()).into(preview)
//                start.visibility = View.VISIBLE
//                start.setOnClickListener {
//                    videoView.start()
//                    preview.visibility = View.GONE
//                    start.visibility = View.GONE
//                }
//            }
//        }
//        videoView.start()

        companyTitle.text = companyInfo.title
        content.text = companyInfo.sub_info
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
    }

    private fun send(){
        if(pingLunInfo.text.isEmpty()){
            toast("请输入评论信息")
            return
        }
        val map = mapOf(
            Pair("id", aId),
            Pair("title", pingLunInfo.text.toString())
        )
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
