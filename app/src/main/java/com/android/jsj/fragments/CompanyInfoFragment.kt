package com.android.jsj.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.jsj.R
import com.android.jsj.entity.MerchantInfo
import com.android.jsj.ui.WebActivity
import com.android.shuizu.myutillibrary.fragment.BaseFragment
import kotlinx.android.synthetic.main.fragment_company_info.*

/**
 * JuShijie
 * Created by 蔡雨峰 on 2019/8/14.
 */
class CompanyInfoFragment : BaseFragment(){

    companion object {
        var merchantInfo: MerchantInfo? = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_company_info, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
    }

    private fun initViews(){
        companyAddress.text = merchantInfo?.address
        companyTrait.text = merchantInfo?.tsfw
        companySubInfo.loadData(merchantInfo?.contents,"text/html; charset=UTF-8", null)
    }
}