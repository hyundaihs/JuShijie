package com.android.jsj.ui

import com.android.jsj.entity.*
import kotlinx.android.synthetic.main.activity_apply_vip_merchant.*

/**
 * ChaYin
 * Created by ${蔡雨峰} on 2019/8/20/020.
 */

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.android.jsj.JSJApplication
import com.android.jsj.JSJApplication.Companion.systemInfo
import com.android.jsj.R
import com.android.shuizu.myutillibrary.MyBaseActivity
import com.android.shuizu.myutillibrary.adapter.GridDivider
import com.android.shuizu.myutillibrary.adapter.MyBaseAdapter
import com.android.shuizu.myutillibrary.dp2px
import com.android.shuizu.myutillibrary.initActionBar
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.android.shuizu.myutillibrary.utils.getErrorDialog
import com.android.shuizu.myutillibrary.utils.getSuccessDialog
import com.cazaea.sweetalert.SweetAlertDialog
import com.google.gson.Gson
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureConfig.CHOOSE_REQUEST
import com.luck.picture.lib.config.PictureMimeType
import com.squareup.picasso.Picasso
import org.jetbrains.anko.toast
import java.io.File

/**
 * ChaYin
 * Created by ${蔡雨峰} on 2019/8/10/010.
 */
class ApplyVIPMerchantActivity : MyBaseActivity() {


    private var uploadFileUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apply_vip_merchant)
        initActionBar(this, "申请认证VIP商家")
        initViews()
    }

    private fun initViews() {
        bank.text = "开户银行：${systemInfo.bank_zh}"
        bankAccountNum.text = systemInfo.bank_yhkh
        payImage.setOnClickListener {
            PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
//                .enableCrop(true)
                .compress(true)
                .withAspectRatio(1, 1)
                .minimumCompressSize(100)
                .freeStyleCropEnabled(true)
                .maxSelectNum(1)
//                    .minSelectNum(0)
                .forResult(PictureConfig.CHOOSE_REQUEST)
        }
        payImageDelete.setOnClickListener {
            payImage.setImageResource(R.mipmap.btn_add_photo)
            uploadFileUrl = ""
            it.visibility = View.GONE
        }
        submit.setOnClickListener {
            if (check()) {
                submit()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            val selectList = PictureSelector.obtainMultipleResult(data)
            when (requestCode) {
                PictureConfig.CHOOSE_REQUEST -> {
                    // 图片、视频、音频选择结果回调
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    for (i in 0 until selectList.size) {
                        val file = selectList[i].compressPath
                        uploadPhoto(file)
                    }
                }
            }
        }
    }

    private fun uploadPhoto(file: String) {
        val list = ArrayList<String>()
        list.add(file)
        KevinRequest.build(this).apply {
            setRequestUrl(UPLOADFILE.getInterface())
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    toast(error)
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    val uploadFileInfoRes = Gson().fromJson(result, UploadFileInfoRes::class.java)
                    uploadFileUrl = uploadFileInfoRes.retRes.file_url
                    Picasso.with(context).load(File(file)).resize(300, 300).into(payImage)
                    payImageDelete.visibility = View.VISIBLE
                }
            })
            setDialog()
            uploadFile(list)
        }
    }

    private fun check(): Boolean {
        return !uploadFileUrl.isEmpty()
    }

    private fun submit() {
        val map = mapOf(
            Pair("type_id", 3),
            Pair("zfpz_file_url", uploadFileUrl)
        )
        KevinRequest.build(this).apply {
            setRequestUrl(TJSJSQ.getInterface(map))
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error)
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    getSuccessDialog(context, "提交成功，请耐心等待审核！", listener = SweetAlertDialog.OnSweetClickListener {
                        finish()
                    })
                }
            })
            openLoginErrCallback(LoginActivity::class.java)
            setDataMap(map)
            setDialog()
            postRequest()
        }
    }
}