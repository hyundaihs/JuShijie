package com.android.jsj.ui

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.widget.RadioButton
import com.android.jsj.R
import com.android.jsj.entity.*
import com.android.jsj.fragments.*
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
    private val MENUS: MutableList<String> = listOf("公司简介", "品牌荣誉", "案例", "视频", "资讯", "设计团队", "特卖").toMutableList()
    private lateinit var fragments: Array<Fragment?>

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
        fragments = Array<Fragment?>(7 + merchantInfo.zdyfl_lists.size) { null }
        for (i in 0 until merchantInfo.zdyfl_lists.size) {
            MENUS.add(merchantInfo.zdyfl_lists[i].title)
        }
        for (i in 0 until MENUS.size) {
            layoutInflater.inflate(R.layout.layout_company_show_menus_item, buttons, true)
            (buttons.getChildAt(i) as RadioButton).text = MENUS[i]
            if (i == 0) {
                (buttons.getChildAt(i) as RadioButton).isChecked = true
            }
        }
        CompanyInfoFragment.merchantInfo = merchantInfo
        buttons.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                1 -> {
                    loadFragment(0, 0, 0)
                }
                2 -> {
                    loadFragment(checkedId - 1, merchantInfo.id, 2)
                }
                3 -> {
                    loadFragment(checkedId - 1, merchantInfo.id, 5)
                }
                4 -> {
                    loadFragment(checkedId - 1, merchantInfo.id, 3)
                }
                5 -> {
                    loadFragment(checkedId - 1, merchantInfo.id, 1)
                }
                6 -> {
                    loadFragment(checkedId - 1, merchantInfo.id, 4)
                }
                7 -> {
                    loadFragment(checkedId - 1, merchantInfo.id, 6)
                }
                in 8..fragments.size -> {
                    loadFragment(
                        checkedId - 1,

                        merchantInfo.id,
                        merchantInfo.zdyfl_lists[checkedId - 8].id
                    )
                }
            }
        }
        loadFragment(0, 0, 0)
    }

    private fun loadFragment(position: Int, id: Int, mk_id: Int) {
        val ft = supportFragmentManager.beginTransaction()
        val fragment = when (position) {
            0 -> {
                CompanyInfoFragment()
            }
            1 -> {
                HonorFragment.aId = id
                HonorFragment.mkId = mk_id
                HonorFragment()
            }
            2 -> {
                CaseFragment.aId = id
                CaseFragment.mkId = mk_id
                CaseFragment()
            }
            3 -> {
                VideoFragment.aId = id
                VideoFragment.mkId = mk_id
                VideoFragment()
            }
            4 -> {
                NewsFragment.aId = id
                NewsFragment.mkId = mk_id
                NewsFragment()
            }
            5 -> {
                DesignerFragment.aId = id
                DesignerFragment.mkId = mk_id
                DesignerFragment()
            }
            6 -> {
                SaleFragment.aId = id
                SaleFragment.mkId = mk_id
                SaleFragment()
            }
            else -> {
                CustomFragment.aId = id
                CustomFragment.mkId = 7
                CustomFragment.zdyId = mk_id
                CustomFragment()
            }
        }
        ft.replace(R.id.layoutFragment, fragment)
        ft.commit()
    }
}