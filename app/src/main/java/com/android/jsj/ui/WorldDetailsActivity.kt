package com.android.jsj.ui

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.view.View
import com.android.jsj.R
import com.android.jsj.adapters.ReplyAdapter
import com.android.jsj.adapters.ReviewsAdapter
import com.android.jsj.entity.*
import com.android.shuizu.myutillibrary.MyBaseActivity
import com.android.shuizu.myutillibrary.adapter.GridDivider
import com.android.shuizu.myutillibrary.adapter.LineDecoration
import com.android.shuizu.myutillibrary.adapter.MyBaseAdapter
import com.android.shuizu.myutillibrary.dp2px
import com.android.shuizu.myutillibrary.initActionBar
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.android.shuizu.myutillibrary.utils.CalendarUtil
import com.android.shuizu.myutillibrary.utils.getErrorDialog
import com.cazaea.sweetalert.SweetAlertDialog
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_world_details.*
import kotlinx.android.synthetic.main.layout_list_empty.*
import kotlinx.android.synthetic.main.layout_upload_image_list_item.view.*
import org.jetbrains.anko.toast
import java.util.ArrayList

/**
 * JuShijie
 * Created by 蔡雨峰 on 2019/8/16.
 */
class WorldDetailsActivity : MyBaseActivity(){
    companion object {
        var aId = 0
    }
    private lateinit var companyInfo: CompanyInfo
    private val imageData = ArrayList<FileInfo>()
    private val imageAdapter = ImageAdapter(imageData)
    private var reviewsInfo = ArrayList<ReviewsInfo>()
    private var adapter = ReviewsAdapter(reviewsInfo)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_world_details)
        initActionBar(this, "饰界详情")
        getWorldDetails()
    }

    private fun getWorldDetails() {
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
        Picasso.with(this).load(companyInfo.account_file_url.getImageUrl()).resize(100, 100)
            .into(photo)
        accountTitle.text = companyInfo.account_title
        content.text = companyInfo.sub_info
        time.text = CalendarUtil(companyInfo.create_time, true).format(CalendarUtil.MM_DD_HH_MM)
        zan.text = companyInfo.zan_nums.toString()
        gz.text = companyInfo.gz_nums.toString()
        pl.text = companyInfo.pl_nums.toString()

        val gridLayoutManager1 = GridLayoutManager(this, 5)
        images.layoutManager = gridLayoutManager1
        //水平分割线
        images.addItemDecoration(GridDivider(this, this.dp2px(10f).toInt(), 5))
        images.itemAnimator = DefaultItemAnimator()
        images.adapter = imageAdapter
        images.isNestedScrollingEnabled = false
        imageAdapter.myOnItemClickListener = object : MyBaseAdapter.MyOnItemClickListener {
            override fun onItemClick(parent: MyBaseAdapter, view: View, position: Int) {

            }
        }

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = OrientationHelper.VERTICAL
        pingLun.layoutManager = layoutManager
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

    private class ImageAdapter(val data: ArrayList<FileInfo>) : MyBaseAdapter(R.layout.layout_upload_image_list_item) {

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            val file = data[position]
            Picasso.with(holder.itemView.context).load(file.resize_file_url).into(holder.itemView.uploadImage)
            holder.itemView.uploadDelete.visibility = View.GONE
        }

        override fun getItemCount(): Int = data.size
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

    private fun send() {
        if (pingLunInfo.text.isEmpty()) {
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