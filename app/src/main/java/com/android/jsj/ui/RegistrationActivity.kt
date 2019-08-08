package com.android.jsj.ui

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.android.jsj.R
import com.android.jsj.entity.REG
import com.android.jsj.entity.SEND_VERF
import com.android.jsj.entity.getInterface
import com.android.shuizu.myutillibrary.MyBaseActivity
import com.android.shuizu.myutillibrary.request.MySimpleRequest
import org.jetbrains.anko.toast
import com.android.shuizu.myutillibrary.utils.LoginErrDialog
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_registration.*

/**
 * ChaYin
 * Created by ${蔡雨峰} on 2018/8/14/014.
 */
class RegistrationActivity : MyBaseActivity() {

    companion object {
        val PAGE_KEY = "isRegiste"
    }

    var isRegister = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        isRegister = intent.getBooleanExtra(PAGE_KEY, true)
        if (isRegister) {
            initRegistration()
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
        contract.setOnClickListener {
            startActivity(Intent(it.context, WebActivity::class.java))
        }
    }

    private fun initRegistration() {
        goLogin.visibility = View.VISIBLE
        layout_1.visibility = View.VISIBLE
        submit.text = "注  册"
    }

    private fun initFindPswd() {
        goLogin.visibility = View.GONE
        layout_1.visibility = View.GONE
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
        } else {
            return true
        }
    }

    private fun getVerifyCode() {
        val map = mapOf(Pair("phone", phone.text.toString()))
        MySimpleRequest(object : MySimpleRequest.RequestCallBack {
            override fun onSuccess(context: Context, result: String) {
            }

            override fun onError(context: Context, error: String) {
                toast(error)
            }

            override fun onLoginErr(context: Context) {
                LoginErrDialog(DialogInterface.OnClickListener { _, _ ->
                    val intent = Intent(context, LoginActivity::class.java)
                    startActivity(intent)
                })
            }

        }).postRequest(this, SEND_VERF.getInterface(Gson().toJson(map)), map)
    }

    private fun submit() {
        val map = mapOf(Pair("phone", phone.text.toString()),
                Pair("msgverf", verifyCode.text.toString()),
                Pair("password", password.text.toString()))
        MySimpleRequest(object : MySimpleRequest.RequestCallBack {
            override fun onSuccess(context: Context, result: String) {
                toast("注册成功")
                finish()
            }

            override fun onError(context: Context, error: String) {
                toast(error)
            }

            override fun onLoginErr(context: Context) {
                LoginErrDialog(DialogInterface.OnClickListener { _, _ ->
                    val intent = Intent(context, LoginActivity::class.java)
                    startActivity(intent)
                })
            }

        }).postRequest(this, REG.getInterface(Gson().toJson(map)), map)
    }
}