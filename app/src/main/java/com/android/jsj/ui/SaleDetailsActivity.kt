package com.android.jsj.ui

import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import com.android.jsj.R
import com.android.jsj.entity.*
import com.android.jsj.util.dianZan
import com.android.shuizu.myutillibrary.MyBaseActivity
import com.android.shuizu.myutillibrary.initActionBar
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.android.shuizu.myutillibrary.utils.getErrorDialog
import com.cazaea.sweetalert.SweetAlertDialog
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_sale_details.*

/**
 * JuShijie
 * Created by 蔡雨峰 on 2019/8/16.
 */
class SaleDetailsActivity : MyBaseActivity(){

    companion object {
        var aId = 0
    }

    private lateinit var companyInfo: CompanyInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sale_details)
        initActionBar(this, "商品详情")
        getSaleDetails()
    }

    private fun getSaleDetails() {
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
        Picasso.with(this).load(companyInfo.file_url.getImageUrl()).into(image)
        companyTitle.text = companyInfo.title
        tags.text = companyInfo.tags
        price.text = "￥${companyInfo.price}元"
        price2.text = "￥${companyInfo.price2}元"
        price.paint.flags = Paint. STRIKE_THRU_TEXT_FLAG
        pinPai.text = companyInfo.pp_title
        type.text = companyInfo.xinghao
        color.text = companyInfo.yanse
        size.text = companyInfo.chicun
        saleDetails.loadData(companyInfo.contents, "text/html; charset=UTF-8", null)
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
    }
}