package com.android.jsj.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.android.jsj.JSJApplication
import com.android.jsj.R
import com.android.jsj.entity.*
import com.android.jsj.ui.LoginActivity
import com.android.shuizu.myutillibrary.D
import com.android.shuizu.myutillibrary.fragment.BaseFragment
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.android.shuizu.myutillibrary.request.getErrorDialog
import com.android.shuizu.myutillibrary.request.getLoginErrDialog
import com.android.shuizu.myutillibrary.widget.GlideImageLoader
import com.cazaea.sweetalert.SweetAlertDialog
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_homepage.*


/**
 * ChaYin
 * Created by ${蔡雨峰} on 2019/7/23/023.
 */
class HomepageFragment : BaseFragment() {

    private val bannerInfos = ArrayList<BannerInfo>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_homepage, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getBanner()
    }

    private fun getBanner() {
        val map = mapOf(
            Pair("type_id", "2")
        )
        KevinRequest.build(activity as Context).apply {
            setRequestUrl(BANNER.getInterface(Gson().toJson(map)))
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error)
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    val bannerInfoListRes = Gson().fromJson(result, BannerInfoListRes::class.java)
                    bannerInfos.clear()
                    bannerInfos.addAll(bannerInfoListRes.retRes)
                    initBanner()
                }
            })
            setLoginErrCallback(object : KevinRequest.LoginErrCallback {
                override fun onLoginErr(context: Context) {
                    getLoginErrDialog(context, SweetAlertDialog.OnSweetClickListener {
                        startActivity(Intent(context, LoginActivity::class.java))
                        activity?.finish()
                    })
                }

            })
            setDataMap(map)
            setDialog()
            postRequest()
        }
    }

    private fun initBanner() {
        val images = ArrayList<String>()
        for (i in 0 until bannerInfos.size) {
            images.add(bannerInfos[i].file_url.getImageUrl())
        }
        banner.setImages(images).setImageLoader(GlideImageLoader()).start()
        banner.setOnBannerListener {
            val banner = bannerInfos[it]
//            val intent = Intent(activity, WebActivity::class.java)
//            if (banner.http_url == "") {
//                intent.putExtra("type", 0)
//                intent.putExtra("html", banner.contents)
//                intent.putExtra("pageName", "平台信息")
//            } else {
//                intent.putExtra("type", 1)
//                intent.putExtra("html", banner.http_url)
//                intent.putExtra("pageName", "平台信息")
//                intent.putExtra("title", banner.goods_title)
//                intent.putExtra("price1", banner.zl_price1)
//                intent.putExtra("price2", banner.price)
//                intent.putExtra("url", banner.file_url)
//            }
//            startActivity(intent)
        }
    }


}