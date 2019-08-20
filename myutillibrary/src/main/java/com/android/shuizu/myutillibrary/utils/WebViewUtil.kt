package com.android.shuizu.myutillibrary.utils

import android.os.Build
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.webkit.WebSettings
import android.os.Build.VERSION_CODES
import android.os.Build.VERSION_CODES.LOLLIPOP
import android.os.Build.VERSION
import android.webkit.CookieManager


/**
 * JuShijie
 * Created by 蔡雨峰 on 2019/8/20.
 */
fun WebView.initImageAuto(){
    val settings = getSettings()
    // 设置WebView支持JavaScript
    settings.setJavaScriptEnabled(true)
    //支持自动适配
    settings.setUseWideViewPort(true)
    settings.setLoadWithOverviewMode(true)
//    settings.setSupportZoom(true)  //支持放大缩小
//    settings.setBuiltInZoomControls(true) //显示缩放按钮
//    settings.setBlockNetworkImage(true)// 把图片加载放在最后来加载渲染
//    settings.setAllowFileAccess(true) // 允许访问文件
//    settings.setSaveFormData(true)
//    settings.setGeolocationEnabled(true)
//    settings.setDomStorageEnabled(true)
//    settings.setJavaScriptCanOpenWindowsAutomatically(true)/// 支持通过JS打开新窗口
//    settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS)
//    settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN)
////不加这个图片显示不出来
//    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
//        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW)
//    }
//    settings.setBlockNetworkImage(false)


    webViewClient = object : WebViewClient(){
        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            val javascript = "javascript:function ResizeImages() {" +
                    "var myimg,oldwidth;" +
                    "var maxwidth = document.body.clientWidth;" +
                    "for(i=0;i <document.images.length;i++){" +
                    "myimg = document.images[i];" +
                    "if(myimg.width > maxwidth){" +
                    "oldwidth = myimg.width;" +
                    "myimg.width = maxwidth;" +
                    "}" +
                    "}" +
                    "}"
            val width = View.MeasureSpec.makeMeasureSpec(
                0,
                View.MeasureSpec.UNSPECIFIED
            )
            val height = View.MeasureSpec.makeMeasureSpec(
                0,
                View.MeasureSpec.UNSPECIFIED
            )
            measure(width, height)
            measuredWidth // 获取宽度
//                view.getMeasuredHeight() // 获取高度
//                val width = String.valueOf(ScreenUtils.widthPixels(mContext))
            loadUrl(javascript)
            loadUrl("javascript:ResizeImages();")
        }
    }
}