package com.android.jsj.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import com.android.jsj.R
import com.android.jsj.entity.*
import com.android.shuizu.myutillibrary.MyBaseActivity
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.android.shuizu.myutillibrary.utils.getErrorDialog
import com.cazaea.sweetalert.SweetAlertDialog
import com.google.gson.Gson
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_registration.*
import org.jetbrains.anko.toast


/**
 * ChaYin
 * Created by ${蔡雨峰} on 2018/8/14/014.
 */
class RegistrationActivity : MyBaseActivity() {

    companion object {
        val PAGE_KEY = "isRegiste"
    }

    var isRegister = false
    private val provInfoList = ArrayList<ProvInfo>()
    private var prov = ""
    private var city = ""
    private var area = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        isRegister = intent.getBooleanExtra(PAGE_KEY, true)
        if (isRegister) {
            initRegistration()
            getAreas()
        } else {
            initFindPswd()
        }
        getVerifyCode.setOnClickListener {
            if (phone.text.isEmpty()) {
                phone.error = "手机号码不能为空"
            } else {
                getVerifyCode.startCount()
                getVerifyCode()
            }
        }
        submit.setOnClickListener {
            if (checkData()) {
                submit()
            }
        }
        goLogin.setOnClickListener {
            finish()
        }

        address.setOnClickListener {
            PickerUtil.showAddress(this
            ) { options1, options2, options3, v ->
                //返回的分别是三个级别的选中位置
                prov = provInfoList[options1].title
                city = provInfoList[options1].lists[options2].title
                area = provInfoList[options1].lists[options2].lists[options3].title
                address.setText("$prov-$city-$area")
            }
        }
    }

    private fun getAreas() {
        KevinRequest.build(this).apply {
            setRequestUrl(AREA)
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context,"地区信息获取失败，请检查网络后重试！", SweetAlertDialog.OnSweetClickListener {
                        finish()
                    })
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    //Json的解析类对象
                    val parser = JsonParser()
                    //将JSON的String 转成一个JsonArray对象
                    val jsonArray = parser.parse(result).asJsonArray
                    for (user in jsonArray) {
                        //使用GSON，直接转成Bean对象
                        val userBean = Gson().fromJson(user, ProvInfo::class.java)
                        provInfoList.add(userBean)
                    }
                    PickerUtil.initAddress(provInfoList)
                }
            })
            setDialog()
            request()
        }
    }

    private fun initRegistration() {
        goLogin.visibility = View.VISIBLE
        address.visibility = View.VISIBLE
        addressLine.visibility = View.VISIBLE
        submit.text = "注  册"
    }

    private fun initFindPswd() {
        goLogin.visibility = View.GONE
        address.visibility = View.GONE
        addressLine.visibility = View.GONE
        submit.text = "提  交"
    }

    private fun checkData(): Boolean {
        if (phone.text.isEmpty()) {
            phone.error = "手机号码不能为空"
            return false
        } else if (password.text.isEmpty()) {
            password.error = "密码不能为空"
            return false
        } else if (verifyCode.text.isEmpty()) {
            verifyCode.error = "验证码不能为空"
            return false
        } else if (address.text.isEmpty() && isRegister) {
            address.error = "注册区域不能为空"
            return false
        } else {
            return true
        }
    }

    private fun getVerifyCode() {
        val map = mapOf(Pair("phone", phone.text.toString()))
        KevinRequest.build(this).apply {
            setRequestUrl(SEND_VERF.getInterface(map))
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context,error)
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    toast("已发送短信")
                }
            })
            setDataMap(map)
            postRequest()
        }
    }

    private fun submit() {
        val map = mapOf(
            Pair("phone", phone.text.toString()),
            Pair("msgverf", verifyCode.text.toString()),
            Pair("password", password.text.toString()),
            Pair("province", prov),
            Pair("city", city),
            Pair("area", area)
        )
        KevinRequest.build(this).apply {
            setRequestUrl(REG.getInterface(map))
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    toast(error)
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    toast("注册成功")
                    finish()
                }
            })
            setDataMap(map)
            setDialog()
            postRequest()
        }
    }
}