package com.android.shuizu.myutillibrary.request

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Environment
import com.android.shuizu.myutillibrary.D
import com.android.shuizu.myutillibrary.utils.MyProgressDialog
import com.cazaea.sweetalert.SweetAlertDialog
import com.google.gson.Gson
import okhttp3.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.concurrent.TimeUnit

/**
 * ChaYin
 * Created by ${蔡雨峰} on 2019/7/31/031.
 */
class KevinRequest private constructor(val context: Context) {

    companion object {
        var sessionId: String = ""

        private val MEDIA_TYPE_JSON =
            MediaType.parse("application/x-www-form-urlencoded charset=utf-8")//mdiatype 这个需要和服务端保持一致

        const val LOGINERR = "loginerr"//需要重新登录错误信息

        fun build(c: Context): KevinRequest {
            return KevinRequest(c)
        }

        private val mOkHttpClient: OkHttpClient by lazy {
            val httpBuilder = OkHttpClient.Builder()
            httpBuilder
                //设置超时
                .connectTimeout(100, TimeUnit.SECONDS)
                .writeTimeout(150, TimeUnit.SECONDS)
                .build()
        }
    }

    private var successCallback: SuccessCallback? = null
    private var errorCallback: ErrorCallback? = null
    private var loginErrCallback: LoginErrCallback? = null
    private var progressCallback: ProgressCallback? = null
    private var downloadSuccessCallback: DownloadSuccessCallback? = null
    private var requestUrl: String = ""
    private var dialog: Dialog? = null
    private var map = mapOf(Pair("", ""))

    fun setRequestUrl(url: String): KevinRequest {
        requestUrl = url
        return this
    }

    fun setDialog(): KevinRequest {
        dialog = context.getLoadingDialog()
        return this
    }

    fun setSuccessCallback(c: SuccessCallback): KevinRequest {
        successCallback = c
        return this
    }

    fun setErrorCallback(c: ErrorCallback): KevinRequest {
        errorCallback = c
        return this
    }

    fun setLoginErrCallback(c: LoginErrCallback): KevinRequest {
        loginErrCallback = c
        return this
    }

    fun setProgressCallback(c: ProgressCallback): KevinRequest {
        progressCallback = c
        return this
    }

    fun setDownloadSuccessCallback(c: DownloadSuccessCallback): KevinRequest {
        downloadSuccessCallback = c
        return this
    }

    fun setDataMap(m: Map<String, String>): KevinRequest {
        map = m
        return this
    }

    fun request() {
        dialog?.show()
        context.doAsync {
            val request = Request.Builder().url(requestUrl).addHeader("cookie", sessionId).build()
            val call = mOkHttpClient.newCall(request)
            //请求加入调度
            val response = call.execute()
            if (response.isSuccessful) {
                val string = response.body().string()
                getSession(response)
                val res = Gson().fromJson(string, RequestResult::class.java)
                if (res.retInt == 1) {
                    uiThread {
                        dialog?.dismiss()
                        successCallback?.onSuccess(context, string)
                    }
                } else {
                    if (res.retErr == LOGINERR) {
                        uiThread {
                            dialog?.dismiss()
                            loginErrCallback?.onLoginErr(context)
                        }
                    } else {
                        uiThread {
                            dialog?.dismiss()
                            errorCallback?.onError(context, res.retErr)
                        }
                    }
                }
            } else {
                uiThread {
                    dialog?.dismiss()
                    errorCallback?.onError(context, response.message())
                }
            }
        }
    }

    fun postRequest() {
        dialog?.show()
        context.doAsync {
            val requestBody = RequestBody.create(MEDIA_TYPE_JSON, Gson().toJson(map))
            val request =
                Request.Builder().url(requestUrl).post(requestBody).addHeader("cookie", sessionId)
                    .build()
            try {
                val response = mOkHttpClient.newCall(request).execute()
                if (response.isSuccessful) {
                    val string = response.body().string()
                    getSession(response)
                    val res: RequestResult = Gson().fromJson(string, RequestResult::class.java)
                    D("requestResult = $string")
                    if (res.retInt == 1) {
                        uiThread {
                            dialog?.dismiss()
                            successCallback?.onSuccess(context, string)
                        }
                    } else {
                        if (res.retErr == LOGINERR) {
                            uiThread {
                                dialog?.dismiss()
                                loginErrCallback?.onLoginErr(context)
                            }
                        } else {
                            uiThread {
                                dialog?.dismiss()
                                errorCallback?.onError(context, res.retErr)
                            }
                        }
                    }
                } else {
                    uiThread {
                        dialog?.dismiss()
                        errorCallback?.onError(context, response.message())
                    }
                }
            } catch (e: Exception) {
                uiThread {
                    dialog?.dismiss()
                    errorCallback?.onError(context, e.toString())
                }
            }
        }
    }

    fun uploadFile(files: ArrayList<String>) {
        dialog?.show()
        doAsync {
            val name = "uploadedfile[]"
            val requestBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
            for (i in 0 until files.size) {
                val file = File(files[i])
                val fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file)
                requestBodyBuilder.addFormDataPart(name, files[i].substring(files[i].lastIndexOf("/")), fileBody)
            }
            val requestBody = requestBodyBuilder.build()
            val request = Request.Builder()
                .url(requestUrl)
                .post(requestBody).addHeader("cookie", sessionId)
                .build()
            try {
                val response = mOkHttpClient.newCall(request).execute()
                if (response.isSuccessful) {
                    val string = response.body().string()
                    getSession(response)
                    val res: RequestResult = Gson().fromJson(string, RequestResult::class.java)
                    if (res.retInt == 1) {
                        uiThread {
                            dialog?.dismiss()
                            successCallback?.onSuccess(context, string)
                        }
                    } else {
                        if (res.retErr == LOGINERR) {
                            uiThread {
                                dialog?.dismiss()
                                loginErrCallback?.onLoginErr(context)
                            }
                        } else {
                            uiThread {
                                dialog?.dismiss()
                                errorCallback?.onError(context, res.retErr)
                            }
                        }
                    }
                } else {
                    uiThread {
                        dialog?.dismiss()
                        errorCallback?.onError(context, "无响应:" + response.message())
                    }
                }
            } catch (e: Exception) {
                uiThread {
                    dialog?.dismiss()
                    errorCallback?.onError(context, "异常:" + e.toString())
                }
            }
        }
    }

    fun downLoadFile(destFileDir: String) {
        doAsync {
            val request = Request.Builder().url(requestUrl).addHeader("cookie", sessionId).build()
            mOkHttpClient.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call?, e: IOException?) {
                    uiThread {
                        errorCallback?.onError(context, e.toString())
                    }
                }

                override fun onResponse(call: Call?, response: Response) {
                    var ips: InputStream? = null
                    val buf = ByteArray(2048)
                    var len = 0
                    var fos: FileOutputStream? = null
//                FileUtil.isFileExist(destFileDir)
                    // 储存下载文件的目录
//                val savePath = isExistDir(destFileDir)
                    try {
                        ips = response.body().byteStream()
                        val total = response.body().contentLength()
                        val file = File(destFileDir)
                        fos = FileOutputStream(file)
                        if (ips == null) {
                            uiThread {
                                errorCallback?.onError(context, "数据获取失败")
                            }
                            return
                        }
                        var sum = 0
                        while (true) {
                            len = ips.read(buf)
                            if (len == -1) {
                                break
                            }
                            fos.write(buf, 0, len)
                            sum += len
                            val progress = (sum * 1.0f / total * 100).toLong()
                            // 下载中
                            uiThread {
                                progressCallback?.onProgress(total, progress)
                            }
                        }
                        fos.flush()
                        uiThread {
                            downloadSuccessCallback?.onDownloadSucces(file)
                        }
                    } catch (e: Exception) {
                        uiThread {
                            errorCallback?.onError(context, e.toString())
                        }
                    } finally {
                        try {
                            if (ips != null)
                                ips.close()
                        } catch (e: IOException) {
                        }
                        try {
                            if (fos != null)
                                fos.close()
                        } catch (e: IOException) {
                        }
                    }
                }

            })
        }
    }

    private fun getSession(response: Response) {
        val headers = response.headers()
        val cookies = headers.values("Set-Cookie")
        if (cookies.isEmpty()) {
            return
        }
        val session = cookies[0]
        sessionId = session.substring(0, session.indexOf(";"))
    }


    interface ProgressCallback {
        fun onProgress(total: Long, progress: Long)
    }

    interface DownloadSuccessCallback {
        fun onDownloadSucces(file: File)
    }

    interface SuccessCallback {
        fun onSuccess(context: Context, result: String)
    }

    interface ErrorCallback {
        fun onError(context: Context, error: String)
    }

    interface LoginErrCallback {
        fun onLoginErr(context: Context)
    }

}

fun Context.getLoadingDialog(): SweetAlertDialog {
    return SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE).apply {
        titleText = "Loading"
        setCancelable(false)
    }
}

fun Any.getErrorDialog(context: Context, error: String): SweetAlertDialog {
    return SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE).apply {
        titleText = error
        confirmText = "确定"
        show()
    }
}

fun Any.getLoginErrDialog(context: Context, listener: SweetAlertDialog.OnSweetClickListener? = null): SweetAlertDialog {
    return SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE).apply {
        titleText = "需要重新验证登陆!"
        confirmText = "确定"
        setConfirmClickListener(listener)
        show()
    }
}