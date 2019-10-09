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
import kotlinx.android.synthetic.main.fragment_livepage.*

/**
 * JuShijie
 * Created by 蔡雨峰 on 2019/10/9.
 */
class LivePageFragment :BaseFragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_livepage, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
    }

    private fun initViews() {
        val fragments = ArrayList<Fragment>()
        val keys = listOf(
            LiveListFragment.LOCAL,
            LiveListFragment.All
        )
        val titles = listOf("本地直播", "全部直播")

        for (i in 0 until keys.size) {
            val liveListFragment = LiveListFragment()
            val bundle = Bundle()
            bundle.putInt(LiveListFragment.PAGE_KEY, keys[i])
            liveListFragment.arguments = bundle
            fragments.add(liveListFragment)
        }

        viewpagerLivePage.adapter = MyPagerAdapter(childFragmentManager, fragments, titles)
        tabLayoutLivePage.setupWithViewPager(viewpagerLivePage)//此方法就是让tablayout和ViewPager联动
    }

    private class MyPagerAdapter(fm: FragmentManager?, val fragments: List<Fragment>, val titles: List<String>) :
        FragmentPagerAdapter(fm) {

        override fun getCount(): Int {
            return fragments.size
        }

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titles[position]
        }
    }
}