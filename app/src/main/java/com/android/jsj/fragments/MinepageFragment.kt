package com.android.jsj.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.jsj.JSJApplication.Companion.userInfo
import com.android.jsj.R
import com.android.jsj.entity.*
import com.android.jsj.ui.ApplyMerchantActivity
import com.android.jsj.ui.ApplyVIPMerchantActivity
import com.android.jsj.ui.ChangeUserInfoActivity
import com.android.shuizu.myutillibrary.fragment.BaseFragment
import com.android.shuizu.myutillibrary.initActionBar
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.android.shuizu.myutillibrary.utils.getErrorDialog
import com.google.gson.Gson
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_change_userinfo.*
import kotlinx.android.synthetic.main.fragment_minepage.*

/**
 * ChaYin
 * Created by ${蔡雨峰} on 2019/7/23/023.
 */
class MinepageFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_minepage, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initActionBar(activity as AppCompatActivity, "个人中心", false)
        getUserInfo()
    }

    private fun initViews() {
        if (userInfo.type_id == AccountType.USER) {
            applyToMerchant.visibility = View.VISIBLE
            tipText.visibility = View.VISIBLE
            coupon.visibility = View.VISIBLE
            ranking.visibility = View.GONE
            info.visibility = View.GONE
            layoutVip.visibility = View.GONE

            Picasso.with(context).load(userInfo.file_url.getImageUrl())
                .into(userPhoto, object : Callback {
                    override fun onSuccess() {
                    }

                    override fun onError() {
                        userPhoto.setImageResource(R.mipmap.ic_launcher)
                    }
                })
            userName.text = userInfo.title

        } else {
            applyToMerchant.visibility = View.GONE
            tipText.visibility = View.GONE
            coupon.visibility = View.GONE
            ranking.visibility = View.VISIBLE
            info.visibility = View.VISIBLE
            layoutVip.visibility = View.VISIBLE

            Picasso.with(context).load(userInfo.file_url.getImageUrl())
                .into(userPhoto, object : Callback {
                    override fun onSuccess() {
                    }

                    override fun onError() {
                        userPhoto.setImageResource(R.mipmap.ic_launcher)
                    }
                })
            userName.text = userInfo.title
            info.text = userInfo.ppInfo.pp_title
        }

        setting.setOnClickListener {
            startActivity(Intent(context, ChangeUserInfoActivity::class.java))
        }
        applyToMerchant.setOnClickListener {
            startActivity(Intent(context, ApplyMerchantActivity::class.java))
        }
        applyVIPMerchant.setOnClickListener {
            startActivity(Intent(context, ApplyVIPMerchantActivity::class.java))
        }
    }

    private fun getUserInfo() {
        KevinRequest.build(activity as Context).apply {
            setRequestUrl(USER_INFO.getInterface())
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error)
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    val userInfoRes = Gson().fromJson(result, UserInfoRes::class.java)
                    userInfo = userInfoRes.retRes
                    initViews()
                }
            })
            postRequest()
        }
    }

}