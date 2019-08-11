package com.android.jsj.ui

import android.os.Bundle
import com.android.jsj.R
import com.android.shuizu.myutillibrary.MyBaseActivity
import com.android.shuizu.myutillibrary.initActionBar
import kotlinx.android.synthetic.main.activity_web.*

/**
 * ChaYin
 * Created by ${蔡雨峰} on 2018/11/6/006.
 */
class WebActivity:MyBaseActivity(){
    companion object {
        var pageTitle = ""
        var pageContent = ""
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        initActionBar(this,pageTitle)
        image.loadData(pageContent,"text/html; charset=UTF-8", null)
    }
}