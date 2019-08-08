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
import com.android.jsj.R
import com.android.jsj.entity.LoginInfoRes
import com.android.jsj.entity.USER_INFO
import com.android.jsj.entity.VERF_LOGIN
import com.android.jsj.entity.getInterface
import com.android.jsj.ui.HomeActivity
import com.android.shuizu.myutillibrary.fragment.BaseFragment
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.android.shuizu.myutillibrary.request.getErrorDialog
import com.android.shuizu.myutillibrary.request.getLoadingDialog
import com.google.gson.Gson
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
        initViews()
        getUserInfo()
    }

    private fun initViews() {
        setting.setOnClickListener {
            if (layout_user.visibility == View.GONE) {
                layout_user.visibility = View.VISIBLE
                layout_merchant.visibility = View.GONE
            } else {
                layout_user.visibility = View.GONE
                layout_merchant.visibility = View.VISIBLE
            }
        }
        val fragments = ArrayList<Fragment>()
        val keys = listOf(
            ListViewFragment.LOAD_MESSAGE,
            ListViewFragment.LOAD_WORLD,
            ListViewFragment.LOAD_RANKING
        )
        val titles = listOf("消息", "我的饰界", "商家排名")

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
        val map = mapOf(Pair("", ""))
        KevinRequest.build(activity as Context).apply {
            setRequestUrl(USER_INFO.getInterface(Gson().toJson(map)))
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error)
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    val loginInfoRes = Gson().fromJson(result, LoginInfoRes::class.java)
                    val loginInfo = loginInfoRes.retRes
                }
            })
            setDialog()
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