package com.android.jsj.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.view.View
import com.android.jsj.JSJApplication
import com.android.jsj.R
import com.android.jsj.adapters.CaseAdapter
import com.android.jsj.entity.*
import com.android.jsj.fragments.CaseFragment
import com.android.jsj.util.dianZan
import com.android.shuizu.myutillibrary.MyBaseActivity
import com.android.shuizu.myutillibrary.adapter.LineDecoration
import com.android.shuizu.myutillibrary.adapter.MyBaseAdapter
import com.android.shuizu.myutillibrary.initActionBar
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.android.shuizu.myutillibrary.utils.getErrorDialog
import com.cazaea.sweetalert.SweetAlertDialog
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_designer_details.*
import kotlinx.android.synthetic.main.layout_list_empty.*
import java.util.*

/**
 * JuShijie
 * Created by 蔡雨峰 on 2019/8/15.
 */
class DesignerDetailsActivity : MyBaseActivity() {
    companion object {
        var aId = 0
    }

    private lateinit var companyInfo: CompanyInfo
    private val companyInfoList = ArrayList<CompanyInfo>()
    private val companyInfoAdapter = CaseAdapter(companyInfoList)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_designer_details)
        initActionBar(this, "案例详情")
        getDesignerDetails()
    }

    private fun getDesignerDetails() {
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
        zan.setCheckedZan(companyInfo.is_zan == 1)
        guanzhu.setCheckedZan(companyInfo.is_gz == 1)
        Picasso.with(this).load(companyInfo.bj_file_url.getImageUrl()).into(image)
        Picasso.with(this).load(companyInfo.file_url.getImageUrl()).into(photo)
        companyTitle.text = companyInfo.title
        tags.text = companyInfo.fengge_title
        price.text = companyInfo.price_str
        subTitle.text = companyInfo.sub_info
        val chooseTypes = JSJApplication.chooseType["10"]
        if (null != chooseTypes) {
            for (i in 0 until chooseTypes.size) {
                if (chooseTypes[i].id == companyInfo.jy)
                    ex.text = chooseTypes[i].title
            }
        }
        price.text = "￥${companyInfo.price}元/天"
        share.setOnClickListener {
            //分享
        }
        zan.setOnClickListener {
            dianZan(aId, "news", "zan", object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    val zanResultRes = Gson().fromJson(result, ZanResultRes::class.java)
                    zan.setCheckedZan(zanResultRes.retRes.type == 1)
                }
            })
        }
        guanzhu.setOnClickListener {
            dianZan(aId, "news", "gz", object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    val zanResultRes = Gson().fromJson(result, ZanResultRes::class.java)
                    guanzhu.setCheckedZan(zanResultRes.retRes.type == 1)
                }
            })
        }

        val layoutManager = LinearLayoutManager(this)
        caseList.layoutManager = layoutManager
        layoutManager.orientation = OrientationHelper.VERTICAL
        caseList.addItemDecoration(LineDecoration(this, LineDecoration.VERTICAL))
        caseList.itemAnimator = DefaultItemAnimator()
        caseList.isNestedScrollingEnabled = false
        caseList.setEmptyView(emptyView)
        caseList.adapter = companyInfoAdapter
        companyInfoAdapter.myOnItemClickListener = object : MyBaseAdapter.MyOnItemClickListener {
            override fun onItemClick(parent: MyBaseAdapter, view: View, position: Int) {
                CaseDetailsActivity.aId = companyInfoList[position].id
                startActivity(Intent(view.context, CaseDetailsActivity::class.java))
            }
        }
        getCompanyInfo()
    }

    private fun getCompanyInfo() {
        val map = mapOf(
            Pair("page_size", "100"),
            Pair("page", 1),
            Pair("id", CaseFragment.aId),
            Pair("mk_id", 5),
            Pair("sjs_id", companyInfo.account_id)
        )
        KevinRequest.build(this).apply {
            setRequestUrl(NEWSLISTS.getInterface(map))
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error)
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    val companyInfoListRes = Gson().fromJson(result, CompanyInfoListRes::class.java)
                    companyInfoList.clear()
                    companyInfoList.addAll(companyInfoListRes.retRes)
                    companyInfoAdapter.notifyDataSetChanged()
                }
            })
            setDataMap(map)
            postRequest()
        }
    }
}