package com.android.jsj.ui

import android.content.Context
import android.os.Bundle
import android.widget.RadioButton
import com.android.jsj.R
import com.android.jsj.entity.*
import com.android.jsj.fragments.CompanyInfoFragment
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
                    getErrorDialog(context, error,
                        SweetAlertDialog.OnSweetClickListener { finish() })
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    merchantInfo = Gson().fromJson(result, MerchantInfoRes::class.java).retRes
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
        companyTitle.text = merchantInfo.title2
        Picasso.with(this).load(merchantInfo.file_url.getImageUrl()).resize(200, 200).into(companyPhoto)
        companyPPName.text = merchantInfo.title
        gz_nums.text = merchantInfo.gz_nums.toString()
        hz_nums.text = merchantInfo.zan_nums.toString()
        ft_nums.text = merchantInfo.ft_nums.toString()
        for (i in 0 until merchantInfo.zdyfl_lists.size) {
            val rbtn = layoutInflater.inflate(R.layout.company_show_menus_item, buttons) as RadioButton
            rbtn.text = merchantInfo.zdyfl_lists[i].title
            buttons.addView(rbtn)
        }
        buttons.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.companyFirst->{
                    loadCompanyInfoFragment()
                }
                1->{

                }
                2->{

                }
                3->{

                }
                4->{

                }
                5->{

                }
                6->{

                }
                else->{

                }
            }
        }

        loadCompanyInfoFragment()
    }

    private fun loadCompanyInfoFragment() {
        val ft = supportFragmentManager.beginTransaction()
        val fragment = CompanyInfoFragment()
        CompanyInfoFragment.merchantInfo = merchantInfo
        ft.add(R.id.layoutFragment, fragment)
        ft.commit()
    }

}