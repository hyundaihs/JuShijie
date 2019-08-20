package com.android.jsj.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.android.jsj.R
import com.android.jsj.entity.MerchantInfo
import com.android.shuizu.myutillibrary.fragment.BaseFragment
import com.android.shuizu.myutillibrary.utils.initImageAuto
import kotlinx.android.synthetic.main.fragment_company_info.*

/**
 * JuShijie
 * Created by 蔡雨峰 on 2019/8/14.
 */
class CompanyInfoFragment : BaseFragment() {

    companion object {
        lateinit var merchantInfo: MerchantInfo
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_company_info, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
    }

    private fun initViews() {
        companyAddress.text = merchantInfo.address
        companySubInfo.initImageAuto()
        companySubInfo.loadData(merchantInfo.contents, "text/html; charset=UTF-8", null)
        val tags = merchantInfo.tsfw.split(",")
        for (i in 0 until tags.size) {
            layoutInflater.inflate(R.layout.layout_tese_layout_item, teSeLayout, true)
            (teSeLayout.getChildAt(i) as TextView).text = tags[i]
        }
    }

}