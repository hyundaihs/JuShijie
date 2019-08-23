package com.android.jsj

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import com.android.jsj.entity.*
import com.android.jsj.ui.LoginActivity
import com.android.jsj.ui.WebActivity
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.android.shuizu.myutillibrary.utils.getErrorDialog
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*

class MainActivity : AppCompatActivity() {

    companion object {
        var time = 6L
        var countTimer: Timer? = null
        var countTask: TimerTask? = null
    }

    private var id = 0

    class CountTask(var text: TextView) : TimerTask() {
        override fun run() {
            doAsync {
                uiThread {
                    if (time == 0L) {
                        countTimer?.cancel()
                        countTimer = null
                        countTask?.cancel()
                        countTask = null
                        time = 6L
                        text.context.startActivity(Intent(text.context, LoginActivity::class.java))
                        (text.context as Activity).finish()
                    } else {
                        text.text = "${time--}s 跳过"
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        counter.visibility = View.GONE
        counter.setOnClickListener {
            counter.isClickable = false
            countTimer?.cancel()
            countTimer = null
            countTask?.cancel()
            countTask = null
            time = 6L
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        getPics()
        getChooseType()
    }

    private fun getChooseType() {
        KevinRequest.build(this).apply {
            setRequestUrl(STYPE.getInterface())
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error)
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    val chooseTypeMapRes = Gson().fromJson(result, ChooseTypeMapRes::class.java)
                    JSJApplication.chooseType = chooseTypeMapRes.retRes
                    startCount()
                }
            })
            postRequest()
        }
    }


    private fun startCount() {
        counter.visibility = View.VISIBLE
        if (countTimer == null) {
            countTimer = Timer()
        }
        if (countTask == null) {
            countTask = CountTask(counter)
        }
        countTimer?.schedule(countTask, 0, 1000)
    }

    private fun getPics() {
        val map = mapOf(
            Pair("type_id", "1")
        )
        KevinRequest.build(this).apply {
            setRequestUrl(BANNER.getInterface(map))
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error)
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    val bannerInfoListRes = Gson().fromJson(result, BannerInfoListRes::class.java)
                    id = bannerInfoListRes.retRes[0].id
                    Picasso.with(context).load(bannerInfoListRes.retRes[0].file_url.getImageUrl()).into(loadingImage)
                    loadingImage.setOnClickListener {
                        getBannerInfo()
                    }
                }
            })
            setDataMap(map)
            postRequest()
        }
    }

    private fun getBannerInfo() {
        val map = mapOf(
            Pair("id", id.toString())
        )
        KevinRequest.build(this).apply {
            setRequestUrl(BANNERINFO.getInterface(map))
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error)
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    countTimer?.cancel()
                    countTimer = null
                    countTask?.cancel()
                    countTask = null
                    time = 6L
                    val bannerInfoRes = Gson().fromJson(result, BannerInfoRes::class.java)
                    WebActivity.pageTitle = "详情"
                    WebActivity.pageContent = bannerInfoRes.retRes.contents
                    startActivity(Intent(context, LoginActivity::class.java))
                    startActivity(Intent(context, WebActivity::class.java))
                    finish()
                }
            })
            setDataMap(map)
            postRequest()
        }
    }
}
