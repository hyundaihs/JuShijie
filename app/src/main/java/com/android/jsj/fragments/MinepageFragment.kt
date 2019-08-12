package com.android.jsj.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.jsj.JSJApplication.Companion.userInfo
import com.android.jsj.R
import com.android.jsj.entity.*
import com.android.jsj.ui.ApplyMerchantActivity
import com.android.jsj.ui.ChangeUserInfoActivity
import com.android.shuizu.myutillibrary.fragment.BaseFragment
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_minepage, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        getUserInfo()
    }

    private fun initViews() {
        val keys = ArrayList<Int>()
        val titles = ArrayList<String>()
        if (userInfo.type_id == AccountType.USER) {
            layout_user.visibility = View.VISIBLE
            layout_merchant.visibility = View.GONE

            Picasso.with(context).load(userInfo.file_url.getImageUrl())
                .into(userPhoto, object : Callback {
                    override fun onSuccess() {
                    }

                    override fun onError() {
                        userPhoto.setImageResource(R.mipmap.ic_launcher)
                    }
                })
            userName.text = userInfo.title

            keys.add(ListViewFragment.LOAD_MESSAGE)
            keys.add(ListViewFragment.LOAD_RANKING)
            titles.add("消息")
            titles.add("商家排名")
        } else {
            layout_user.visibility = View.GONE
            layout_merchant.visibility = View.VISIBLE

            Picasso.with(context).load(userInfo.file_url.getImageUrl())
                .into(merchantPhoto, object : Callback {
                    override fun onSuccess() {
                    }

                    override fun onError() {
                        userPhoto.setImageResource(R.mipmap.ic_launcher)
                    }
                })
            merchantName.text = userInfo.title
            merchantCompany.text = userInfo.ppInfo.pp_title

            keys.add(ListViewFragment.LOAD_MESSAGE)
            keys.add(ListViewFragment.LOAD_WORLD)
            keys.add(ListViewFragment.LOAD_RANKING)
            titles.add("消息")
            titles.add("我的饰界")
            titles.add("商家排名")
        }

        setting.setOnClickListener {
            startActivity(Intent(context, ChangeUserInfoActivity::class.java))
        }
        applyToMerchant.setOnClickListener {
            startActivity(Intent(context, ApplyMerchantActivity::class.java))
        }
        val fragments = ArrayList<Fragment>()
        for (i in 0 until keys.size) {
            val ordersFragment = ListViewFragment()
            val bundle = Bundle()
            bundle.putInt(ListViewFragment.PAGE_KEY, keys[i])
            ordersFragment.arguments = bundle
            fragments.add(ordersFragment)
        }

        viewpager.adapter = MyPagerAdapter(childFragmentManager, fragments, titles)
        tabLayout.setupWithViewPager(viewpager)//此方法就是让tablayout和ViewPager联动
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

    private class MyPagerAdapter(fm: FragmentManager?, val fragments: List<Fragment>, val titles: List<String>) :
        FragmentPagerAdapter(fm) {

        override fun getCount(): Int {
            return fragments.size
        }

        override fun getItem(position: Int): android.support.v4.app.Fragment {
            return fragments[position]
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titles[position]
        }
    }
}