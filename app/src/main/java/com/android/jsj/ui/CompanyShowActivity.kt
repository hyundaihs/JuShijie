package com.android.jsj.ui

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.RadioButton
import com.android.jsj.R
import com.android.jsj.entity.*
import com.android.jsj.fragments.*
import com.android.jsj.util.dianZan
import com.android.jsj.util.getPlayUrl
import com.android.shuizu.myutillibrary.MyBaseActivity
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.android.shuizu.myutillibrary.utils.getErrorDialog
import com.cazaea.sweetalert.SweetAlertDialog
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_company_show.*
import android.widget.LinearLayout
import com.luck.picture.lib.tools.ScreenUtils
import kotlinx.android.synthetic.main.dialog_layout_yuyue.view.*
import android.widget.Toast
import com.android.shuizu.myutillibrary.utils.CalendarUtil
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.bigkoo.pickerview.builder.TimePickerBuilder
import kotlinx.android.synthetic.main.dialog_layout_private_message.view.*
import kotlinx.android.synthetic.main.dialog_layout_yuyue.*
import org.jetbrains.anko.toast
import java.util.*
import android.view.KeyEvent.KEYCODE_BACK




/**
 * JuShijie
 * Created by 蔡雨峰 on 2019/8/12.
 */
class CompanyShowActivity : MyBaseActivity() {

    private var companyId = 0
    private lateinit var merchantInfo: MerchantInfo
    private val MENUS: MutableList<String> = listOf("公司简介", "品牌荣誉", "案例", "视频", "资讯", "设计团队", "特卖").toMutableList()
    private lateinit var fragments: Array<Fragment?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_show)
        companyId = intent.getIntExtra("id", 0)
        getCompanyInfo()
    }

    private fun getCompanyInfo() {
        val map = mapOf(
            Pair("id", companyId)
        )
        KevinRequest.build(this).apply {
            setRequestUrl(SJXQ.getInterface(map))
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error,
                        SweetAlertDialog.OnSweetClickListener { finish() })
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    merchantInfo = Gson().fromJson(result, MerchantInfoRes::class.java).retRes
                    initViews()
                }
            })
            openLoginErrCallback(LoginActivity::class.java)
            setDataMap(map)
            setDialog()
            postRequest()
        }
    }

    private fun initViews() {
        companyGz.text = if (merchantInfo.is_gz == 1) "已关注" else "关注"
        companyGz.setBackgroundResource(
            if (merchantInfo.is_gz == 1) R.drawable.rect_bg17_round_bg18_bold
            else R.drawable.rect_bg6_round_white_bold
        )
        companyTitle.text = merchantInfo.title2
        Picasso.with(this).load(merchantInfo.file_url.getImageUrl()).resize(200, 200).into(companyPhoto)
        companyPPName.text = merchantInfo.title
        gz_nums.text = merchantInfo.gz_nums.toString()
        hz_nums.text = merchantInfo.zan_nums.toString()
        ft_nums.text = merchantInfo.ft_nums.toString()
        fragments = Array<Fragment?>(7 + merchantInfo.zdyfl_lists.size) { null }
        for (i in 0 until merchantInfo.zdyfl_lists.size) {
            MENUS.add(merchantInfo.zdyfl_lists[i].title)
        }
        for (i in 0 until MENUS.size) {
            layoutInflater.inflate(R.layout.layout_company_show_menus_item, buttons, true)
            (buttons.getChildAt(i) as RadioButton).text = MENUS[i]
            if (i == 0) {
                (buttons.getChildAt(i) as RadioButton).isChecked = true
            }
        }
        CompanyInfoFragment.merchantInfo = merchantInfo
        buttons.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                1 -> {
                    loadFragment(0, 0, 0)
                }
                2 -> {
                    loadFragment(checkedId - 1, merchantInfo.id, 2)
                }
                3 -> {
                    loadFragment(checkedId - 1, merchantInfo.id, 5)
                }
                4 -> {
                    loadFragment(checkedId - 1, merchantInfo.id, 3)
                }
                5 -> {
                    loadFragment(checkedId - 1, merchantInfo.id, 1)
                }
                6 -> {
                    loadFragment(checkedId - 1, merchantInfo.id, 4)
                }
                7 -> {
                    loadFragment(checkedId - 1, merchantInfo.id, 6)
                }
                in 8..fragments.size -> {
                    loadFragment(
                        checkedId - 1,

                        merchantInfo.id,
                        merchantInfo.zdyfl_lists[checkedId - 8].id
                    )
                }
            }
        }
        loadFragment(0, 0, 0)
        yuYue.setOnClickListener {
            showYuYue()
        }
        companyMsg.setOnClickListener {
            showPrivateMessage()
        }
        companyShare.setOnClickListener {

        }
        companyGz.setOnClickListener {
            dianZan(CaseDetailsActivity.aId, "account", "gz", object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    val zanResultRes = Gson().fromJson(result, ZanResultRes::class.java)
                    companyGz.text = if (zanResultRes.retRes.type == 1) "已关注" else "关注"
                    companyGz.setBackgroundResource(
                        if (zanResultRes.retRes.type == 1) R.drawable.rect_bg17_round_bg18_bold
                        else R.drawable.rect_bg6_round_white_bold
                    )
                }
            })
        }
        companyWatchShow.visibility = if (merchantInfo.zbj_zb_status == 1) View.VISIBLE else View.GONE
        companyWatchShow.setOnClickListener {
            it.context.getPlayUrl(merchantInfo.zbj_id, object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    val playUrl = Gson().fromJson(result, PlayUrlRes::class.java).retRes
                    val intent = Intent(context, LivePageActivity::class.java)
                    intent.putExtra("url", playUrl.https_flv)
                    startActivity(intent)
                }
            })
        }
    }

    private var isYuYueShow = false

    private fun showYuYue() {
        isYuYueShow = true
        val pvTime = TimePickerBuilder(this,
            OnTimeSelectListener { date, v ->
                time.text = CalendarUtil(date.time).format(CalendarUtil.YYYY_MM_DD)
            }).build()
        layoutYuYue.visibility = View.VISIBLE
        time.setOnClickListener {
            pvTime.show()
        }
        ok.setOnClickListener {
            if (pvTime.isShowing) {
                pvTime.dismiss()
            }
            layoutYuYue.visibility = View.GONE
            isYuYueShow = false
            if (time.text.isEmpty() || time.text.toString() == "年-月-日") {
                getErrorDialog(this@CompanyShowActivity, "预约时间为空")
            } else {
                yuYue(time.text.toString())
            }
        }
        cancel.setOnClickListener {
            if (pvTime.isShowing) {
                pvTime.dismiss()
            }
            layoutYuYue.visibility = View.GONE
            isYuYueShow = false
        }
    }

    override fun onBackPressed() {
        if(isYuYueShow){
            layoutYuYue.visibility = View.GONE
            isYuYueShow = false
        }else{
            super.onBackPressed()
        }
    }

    private fun yuYue(time: String) {
        val map = mapOf(
            Pair("account_id", merchantInfo.id),
//            Pair("sjs_id", merchantInfo.id),
            Pair("yy_time", time)
        )
        KevinRequest.build(this).apply {
            setRequestUrl(TJYY.getInterface(map))
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error)
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    toast("预约成功")
                }
            })
            setDataMap(map)
            setDialog()
            postRequest()
        }
    }

    private fun showPrivateMessage() {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_layout_private_message, null, false)
        val builder = AlertDialog.Builder(this).setView(view)
        builder.setPositiveButton("确定", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                if (view.message.text.trim().isEmpty()) {
                    getErrorDialog(this@CompanyShowActivity, "私信内容为空")
                } else {
                    sendMessage(view.message.text.toString())
                }
            }
        })
        builder.setNegativeButton("取消", null)
        builder.create().show()
    }

    private fun sendMessage(text: String) {
        val map = mapOf(
            Pair("account_id", merchantInfo.id),
            Pair("contents", text)
        )
        KevinRequest.build(this).apply {
            setRequestUrl(FSSX.getInterface(map))
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error)
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    toast("私信发送成功")
                }
            })
            setDataMap(map)
            setDialog()
            postRequest()
        }
    }

    private fun loadFragment(position: Int, id: Int, mk_id: Int) {
        val ft = supportFragmentManager.beginTransaction()
        val fragment = when (position) {
            0 -> {
                CompanyInfoFragment()
            }
            1 -> {
                HonorFragment.aId = id
                HonorFragment.mkId = mk_id
                HonorFragment()
            }
            2 -> {
                CaseFragment.aId = id
                CaseFragment.mkId = mk_id
                CaseFragment()
            }
            3 -> {
                VideoFragment.aId = id
                VideoFragment.mkId = mk_id
                VideoFragment()
            }
            4 -> {
                NewsFragment.aId = id
                NewsFragment.mkId = mk_id
                NewsFragment()
            }
            5 -> {
                DesignerFragment.aId = id
                DesignerFragment.mkId = mk_id
                DesignerFragment()
            }
            6 -> {
                SaleFragment.aId = id
                SaleFragment.mkId = mk_id
                SaleFragment()
            }
            else -> {
                CustomFragment.aId = id
                CustomFragment.mkId = 7
                CustomFragment.zdyId = mk_id
                CustomFragment()
            }
        }
        ft.replace(R.id.layoutFragment, fragment)
        ft.commit()
    }
}