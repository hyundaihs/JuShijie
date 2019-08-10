package com.android.jsj.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.android.jsj.JSJApplication
import com.android.jsj.JSJApplication.Companion.userInfo
import com.android.jsj.R
import com.android.jsj.entity.*
import com.android.shuizu.myutillibrary.MyBaseActivity
import com.android.shuizu.myutillibrary.initActionBar
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.google.gson.Gson
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_change_userinfo.*
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.layout_merchant_ranking_list_item.*
import org.jetbrains.anko.toast
import java.io.File

/**
 * ChaYin
 * Created by ${蔡雨峰} on 2019/8/10/010.
 */
class ChangeUserInfoActivity : MyBaseActivity() {

    private var fileUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_userinfo)
        initActionBar(this, "修改信息")
        initViews()
    }

    private fun initViews() {
        Picasso.with(this).load(JSJApplication.userInfo.file_url.getImageUrl())
            .into(userInfoPhoto, object : Callback {
                override fun onSuccess() {
                }

                override fun onError() {
                    userInfoPhoto.setImageResource(R.mipmap.ic_launcher)
                }
            })
        userInfoPhoto.setOnClickListener {
            PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .enableCrop(true)
                .compress(true)
                .withAspectRatio(1, 1)
                .minimumCompressSize(100)
                .freeStyleCropEnabled(true)
                .maxSelectNum(1)
//                    .minSelectNum(0)
                .forResult(PictureConfig.CHOOSE_REQUEST)
        }
        userInfoName.setText(JSJApplication.userInfo.title)
        userInfoSubmit.setOnClickListener {
            if (checkData()) {
                if (fileUrl == "") {
                    submit(JSJApplication.userInfo.file_url.getImageUrl())
                } else {
                    submit(fileUrl)
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
                    fileUrl = uploadFileInfoRes.retRes.file_url
                    userInfoPhoto.setImageURI(Uri.fromFile(File(file)))
                }
            })
            setDialog()
            uploadFile(list)
        }
    }

    private fun submit(url: String) {
        val map = mapOf(
            Pair("title", userInfoName.text.toString()),
            Pair("file_url", url)
        )
        KevinRequest.build(this).apply {
            setRequestUrl(SETINFO.getInterface(map))
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    toast(error)
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    toast("修改成功")
                    finish()
                }
            })
            setDataMap(map)
            postRequest()
        }
    }

    private fun checkData(): Boolean {
        return if (fileUrl == "" && userInfoName.text.toString() == userInfo.title) {
            toast("没有做任何修改")
            false
        } else if (userInfoName.text.isEmpty()) {
            userInfoName.error = "昵称不能为空"
            false
        } else {
            true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            val selectList = PictureSelector.obtainMultipleResult(data)
            when (requestCode) {
                PictureConfig.CHOOSE_REQUEST -> {
                    for (i in 0 until selectList.size) {
                        val file = selectList[i].compressPath
                        uploadPhoto(file)
                    }
                }
            }
        }
    }

}