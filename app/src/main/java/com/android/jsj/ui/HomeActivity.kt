package com.android.jsj.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import com.android.jsj.R
import com.android.jsj.fragments.ConcernpageFragment
import com.android.jsj.fragments.HomepageFragment
import com.android.jsj.fragments.LivepageFragment
import com.android.jsj.fragments.MinepageFragment
import com.android.shuizu.myutillibrary.MyBaseActivity
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.ashokvarma.bottomnavigation.BottomNavigationItem
import kotlinx.android.synthetic.main.activity_home.*


/**
 * ChaYin
 * Created by ${蔡雨峰} on 2018/8/15/015.
 */
class HomeActivity : MyBaseActivity(), BottomNavigationBar.OnTabSelectedListener {
    override fun onTabReselected(position: Int) {
    }

    override fun onTabUnselected(position: Int) {
    }

    override fun onTabSelected(position: Int) {
        loadFragment(position)
    }

    private val fragments = Array<Fragment?>(5) { null }
    private var last = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        init()
    }

    private fun init() {
        navigation.setTabSelectedListener(this)
        navigation.setFirstSelectedPosition(0)
        val item =
            navigation.addItem(
                BottomNavigationItem(
                    R.mipmap.nav1c,
                    getString(R.string.home_page)
                ).setInactiveIconResource(R.mipmap.nav1)
            )
                .addItem(
                    BottomNavigationItem(
                        R.mipmap.nav2c,
                        getString(R.string.live_page)
                    ).setInactiveIconResource(R.mipmap.nav2)
                )
                .addItem(
                    BottomNavigationItem(R.mipmap.nav3c, getString(R.string.concern_page)).setInactiveIconResource(
                        R.mipmap.nav3
                    )
                )
                .addItem(
                    BottomNavigationItem(
                        R.mipmap.nav4c,
                        getString(R.string.mine_page)
                    ).setInactiveIconResource(R.mipmap.nav4)
                )
                .initialise()//所有的设置需在调用该方法前完成
        loadFragment(0)
    }

    fun loadPage(index: Int) {
        navigation.selectTab(index)
    }

    private fun loadFragment(position: Int) {
        val ft = supportFragmentManager.beginTransaction()
        if (fragments[position] == null) {
            fragments[position] = when (position) {
                0 -> HomepageFragment()
                1 -> LivepageFragment()
                2 -> ConcernpageFragment()
                3 -> MinepageFragment()
                else -> MinepageFragment()
            }
            ft.add(R.id.content, fragments[position])
        }
        if (last != -1) {
            ft.hide(fragments[last])
        }
        ft.show(fragments[position])
        last = position
        ft.commit()
    }

}