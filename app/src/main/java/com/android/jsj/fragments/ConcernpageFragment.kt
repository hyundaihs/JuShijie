package com.android.jsj.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.jsj.R
import com.android.shuizu.myutillibrary.fragment.BaseFragment
import com.android.shuizu.myutillibrary.initActionBar
import kotlinx.android.synthetic.main.fragment_concernpage.*

/**
 * ChaYin
 * Created by ${蔡雨峰} on 2019/7/23/023.
 */
class ConcernpageFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_concernpage, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
    }

    private fun initViews() {
        val fragments = ArrayList<Fragment>()
        val titles = listOf("我的饰界", "商家", "资讯", "案例")
        fragments.add(MyWorldFragment())
        fragments.add(MerchantFragment())
        fragments.add(NewsFragment())
        fragments.add(CaseFragment())
        viewpagerConcern.adapter = MyPagerAdapter(childFragmentManager, fragments, titles)
        tabLayoutConcern.setupWithViewPager(viewpagerConcern)//此方法就是让tablayout和ViewPager联动
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