package com.android.jsj.ui

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.android.jsj.R
import com.android.jsj.JSJApplication
import com.android.jsj.entity.*
import com.android.shuizu.myutillibrary.D
import com.android.shuizu.myutillibrary.MyBaseActivity
import com.android.shuizu.myutillibrary.request.*
import com.android.shuizu.myutillibrary.utils.PreferenceUtil
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_login.*
import com.cazaea.sweetalert.SweetAlertDialog


/**
 * ChaYin
 * Created by ${蔡雨峰} on 2018/8/14/014.
 */
class LoginActivity : MyBaseActivity() {

    var login_verf: String by PreferenceUtil(JSJApplication.instance, App_Keyword.LOGIN_VERF, "")
    var login_account: String by PreferenceUtil(JSJApplication.instance, App_Keyword.LOGIN_ACCOUNT, "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        registration.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            intent.putExtra(RegistrationActivity.PAGE_KEY, true)
            startActivity(intent)
        }

        forgetPassword.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            intent.putExtra(RegistrationActivity.PAGE_KEY, false)
            startActivity(intent)
        }
        loginBtn.setOnClickListener {
            //            if (checkData()) {
//                login()
//            }
            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
            finish()
        }
        getSystemInfo()
    }

    private fun autoLogin() {
        if (!login_verf.isEmpty()) {
            val map = mapOf(Pair("login_verf", login_verf))
            KevinRequest.build(this).apply {
                setRequestUrl(VERF_LOGIN.getInterface(Gson().toJson(map)))
                setErrorCallback(object : KevinRequest.ErrorCallback {
                    override fun onError(context: Context, error: String) {

                    }
                })
                setSuccessCallback(object : KevinRequest.SuccessCallback {
                    override fun onSuccess(context: Context, result: String) {
                        val loginInfoRes = Gson().fromJson(result, LoginInfoRes::class.java)
                        val loginInfo = loginInfoRes.retRes
                        login_verf = loginInfo.login_verf
                        startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                        finish()
                    }
                })
                setDataMap(map)
                setDialog()
                postRequest()
            }
        }
    }

    private fun checkData(): Boolean {
        return when {
            account.text.trim().isEmpty() -> {
                account.error = "手机号码不能为空"
                false
            }
            password.text.trim().isEmpty() -> {
                password.error = "密码不能为空"
                false
            }
            else -> true
        }
    }


    private fun login() {
        val map = mapOf(
            Pair("account", account.text.toString()),
            Pair("password", password.text.toString())
        )
        KevinRequest.build(this).apply {
            setRequestUrl(LOGIN.getInterface(Gson().toJson(map)))
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {

                }

            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {

                }

            })
            setDataMap(map)
            setDialog()
            postRequest()
        }
    }

    private fun getSystemInfo() {
        val map = mapOf(
            Pair("", "")
        )
        KevinRequest.build(this).apply {
            setRequestUrl(SYS_INFO.getInterface(Gson().toJson(map)))
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error)
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    val systemInfoRes = Gson().fromJson(result, SystemInfoRes::class.java)
                    JSJApplication.systemInfo = systemInfoRes.retRes
                    D(JSJApplication.systemInfo.toString())
                    autoLogin()
                }
            })
            setLoginErrCallback(object : KevinRequest.LoginErrCallback {
                override fun onLoginErr(context: Context) {
                    getLoginErrDialog(context, SweetAlertDialog.OnSweetClickListener {
                        startActivity(Intent(context, LoginActivity::class.java))
                        finish()
                    })
                }

            })
            setDialog()
            postRequest()
        }
    }
}