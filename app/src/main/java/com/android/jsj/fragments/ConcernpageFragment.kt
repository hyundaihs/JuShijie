package com.android.jsj.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.jsj.R
import com.android.shuizu.myutillibrary.fragment.BaseFragment
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
        val keys = listOf(
            ListViewFragment.LOAD_ALIVE,
            ListViewFragment.LOAD_MERCHANT,
            ListViewFragment.LOAD_CASE,
            ListViewFragment.LOAD_DESIGNER
        )
        val titles = listOf("直播", "商家", "案例", "设计师")

        for (i in 0 until keys.size) {
            val ordersFragment = ListViewFragment()
            val bundle = Bundle()
            bundle.putInt(ListViewFragment.PAGE_KEY, keys[i])
            ordersFragment.arguments = bundle
            fragments.add(ordersFragment)
        }

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