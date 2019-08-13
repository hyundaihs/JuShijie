package com.android.jsj.ui

import android.content.Context
import android.os.Bundle
import com.android.jsj.R
import com.android.jsj.entity.MerchantInfo
import com.android.jsj.entity.SJXQ
import com.android.jsj.entity.getImageUrl
import com.android.jsj.entity.getInterface
import com.android.shuizu.myutillibrary.MyBaseActivity
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.android.shuizu.myutillibrary.utils.getErrorDialog
import com.cazaea.sweetalert.SweetAlertDialog
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_company_show.*

/**
 * JuShijie
 * Created by 蔡雨峰 on 2019/8/12.
 */
class CompanyShowActivity : MyBaseActivity() {

    private var companyId = 0
    private lateinit var merchantInfo: MerchantInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_show)
        companyId = intent.getIntExtra("id", 0)
        getCompanyInfo()
    }

    private fun getCompanyInfo() {
        val map = mapOf(
            Pair("id", companyId)
        )
        KevinRequest.build(this).apply {
            setRequestUrl(SJXQ.getInterface(map))
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error, object : SweetAlertDialog.OnSweetClickListener {
                        override fun onClick(sweetAlertDialog: SweetAlertDialog?) {
                            finish()
                        }
                    })
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    merchantInfo = Gson().fromJson(result, MerchantInfo::class.java)
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
        companyTitle.text = merchantInfo.title
        Picasso.with(this).load(merchantInfo.file_url.getImageUrl()).resize(300, 300).into(companyPhoto)
        companyPPName.text = merchantInfo.pp_title
        gz_nums.text = merchantInfo.gz_nums.toString()
        hz_nums.text = merchantInfo.zan_nums.toString()
        ft_nums.text = merchantInfo.ft_nums.toString()
    }

}