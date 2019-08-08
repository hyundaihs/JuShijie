package com.android.jsj.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.jsj.R
import com.android.shuizu.myutillibrary.fragment.BaseFragment

/**
 * ChaYin
 * Created by ${蔡雨峰} on 2019/7/30/030.
 */
class ListViewFragment : BaseFragment(){

    companion object {
        const val PAGE_KEY = "page_key"
        const val LOAD_MESSAGE = 1 //我的消息
        const val LOAD_WORLD = 2 //我的饰界
        const val LOAD_RANKING = 3 //商家排名
        const val LOAD_ALIVE = 4 //直播
        const val LOAD_MERCHANT = 5 //商家
        const val LOAD_CASE = 6 //案例
        const val LOAD_DESIGNER = 7 //设计师
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_listview, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}