package com.android.jsj.ui

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.app.Fragment
import com.android.jsj.R
import com.android.jsj.fragments.*
import com.android.shuizu.myutillibrary.MyBaseActivity
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.ashokvarma.bottomnavigation.BottomNavigationItem
import kotlinx.android.synthetic.main.activity_home.*
import org.jetbrains.anko.toast


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


    private var isExit = false  // 标识是否退出

    private val mHandler = object : Handler() {

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            isExit = false
        }
    }

    fun loadPage(index: Int) {
        navigation.selectTab(index)
    }

    override fun onBackPressed() {
        if (!isExit) {
            isExit = true
            toast("再按一次后退键退出程序")
            mHandler.sendEmptyMessageDelayed(0, 2000)  // 利用handler延迟发送更改状态信息
        } else {
            super.onBackPressed()
        }
    }

    private fun loadFragment(position: Int) {
        val ft = supportFragmentManager.beginTransaction()
        if (fragments[position] == null) {
            fragments[position] = when (position) {
                0 -> HomepageFragment()
                1 -> LivePageFragment()
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