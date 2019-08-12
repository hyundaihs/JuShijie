package com.android.jsj.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.android.jsj.JSJApplication
import com.android.jsj.R
import com.android.jsj.entity.TJSJSQ
import com.android.jsj.entity.UPLOADFILE
import com.android.jsj.entity.UploadFileInfoRes
import com.android.jsj.entity.getInterface
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
import kotlinx.android.synthetic.main.activity_apply_merchant.*
import kotlinx.android.synthetic.main.layout_upload_image_list_item.view.*
import org.jetbrains.anko.toast
import java.io.File

/**
 * ChaYin
 * Created by ${蔡雨峰} on 2019/8/10/010.
 */
class ApplyMerchantActivity : MyBaseActivity() {

    private val imageData = ArrayList<String>()
    private val imageAdapter = ImageAdapter(imageData)

    companion object {
        val MAX_IMAGE = 10
        val CHOOSE_PPSQ = 101
        val CHOOSE_YYZZ = 102
        val CHOOSE_ELSE = 103
    }

    private var currChoose = 0
    private var ppsqFileUrl = ""
    private var yyzzFileUrl = ""
    private val elseFileUrl = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apply_merchant)
        initActionBar(this, "申请认证商家")
        initViews()
    }

    private fun initViews() {
        ppsqImage.setOnClickListener {
            currChoose = CHOOSE_PPSQ
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
        ppsqDelete.setOnClickListener {
            ppsqImage.setImageResource(R.mipmap.btn_add_photo)
            ppsqFileUrl = ""
            it.visibility = View.GONE
        }
        yyzzImage.setOnClickListener {
            currChoose = CHOOSE_YYZZ
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
        yyzzDelete.setOnClickListener {
            yyzzImage.setImageResource(R.mipmap.btn_add_photo)
            yyzzFileUrl = ""
            it.visibility = View.GONE
        }
        val gridLayoutManager1 = GridLayoutManager(this, 5)
        mmzpRecyclerView.layoutManager = gridLayoutManager1
        //水平分割线
        mmzpRecyclerView.addItemDecoration(GridDivider(this, dp2px(10f).toInt(), 5))
        mmzpRecyclerView.itemAnimator = DefaultItemAnimator()
        mmzpRecyclerView.adapter = imageAdapter
        mmzpRecyclerView.isNestedScrollingEnabled = false
        imageAdapter.myOnItemClickListener = object : MyBaseAdapter.MyOnItemClickListener {
            override fun onItemClick(parent: MyBaseAdapter, view: View, position: Int) {
                if (position == imageData.size && position < MAX_IMAGE) {
                    currChoose = CHOOSE_ELSE
                    PictureSelector.create(this@ApplyMerchantActivity)
                        .openGallery(PictureMimeType.ofImage())
//                        .enableCrop(true)
                        .compress(true)
                        .withAspectRatio(1, 1)
                        .minimumCompressSize(100)
                        .freeStyleCropEnabled(true)
                        .maxSelectNum(MAX_IMAGE - imageData.size)
                        .minSelectNum(1)
                        .forResult(CHOOSE_REQUEST)
                } else {
                    //ShowImageDialog(File(images[position]))
                }
            }
        }

        apply.setOnClickListener {
            if (checkData()) {
                apply()
            }
        }

    }

    private fun apply() {
        val map = mapOf(
            Pair("type_id", "${JSJApplication.userInfo.type_id + 1}"),
            Pair("title", userInfoSubName.text.toString()),
            Pair("title2", merchantCompany.text.toString()),
            Pair("pp_title", merchantName.text.toString()),
            Pair("sqs_file_url", ppsqFileUrl),
            Pair("yyzz_file_url", yyzzFileUrl),
            Pair("img_lists", elseFileUrl)
        )
        KevinRequest.build(this).apply {
            setRequestUrl(TJSJSQ.getInterface(map))
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    toast(error)
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    getSuccessDialog(context, "申请成功，请耐心等待审核！", listener = SweetAlertDialog.OnSweetClickListener {
                        finish()
                    })
                }
            })
            setDataMap(map)
            setDialog()
            postRequest()
        }
    }

    private fun checkData(): Boolean {
        when {
            merchantName.text.isEmpty() -> {
                merchantName.error = "代理品牌不能为空"
                return false
            }
            merchantCompany.text.isEmpty() -> {
                merchantCompany.error = "注册企业不能为空"
                return false
            }
            userInfoSubName.text.isEmpty() -> {
                userInfoSubName.error = "企业简称不能为空"
                return false
            }
            ppsqFileUrl == "" -> {
                getErrorDialog(this, "请上传品牌授权书")
                return false
            }
            yyzzFileUrl == "" -> {
                getErrorDialog(this, "请上传营业执照")
                return false
            }
            elseFileUrl.size < 6 -> {
                getErrorDialog(this, "请上传门面图片，至少六张")
                return false
            }
            else -> return true
        }
    }

    private class ImageAdapter(val data: ArrayList<String>) : MyBaseAdapter(R.layout.layout_upload_image_list_item) {

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            if (position >= data.size && position < MAX_IMAGE) {
                holder.itemView.uploadImage.setImageResource(R.mipmap.btn_add_photo)
                holder.itemView.uploadDelete.visibility = View.GONE
            } else {
                Picasso.with(holder.itemView.context).load(File(data[position])).resize(300, 300)
                    .into(holder.itemView.uploadImage)
                holder.itemView.uploadDelete.visibility = View.VISIBLE
            }
            holder.itemView.uploadDelete.setOnClickListener {
                data.removeAt(position)
                notifyDataSetChanged()
            }
        }

        override fun getItemCount(): Int = if (data.size < MAX_IMAGE) data.size + 1 else data.size
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
                    when (currChoose) {
                        CHOOSE_PPSQ -> {
                            ppsqFileUrl = uploadFileInfoRes.retRes.file_url
                            Picasso.with(context).load(File(file)).resize(300, 300).into(ppsqImage)
                            ppsqDelete.visibility = View.VISIBLE
                        }
                        CHOOSE_YYZZ -> {
                            yyzzFileUrl = uploadFileInfoRes.retRes.file_url
                            Picasso.with(context).load(File(file)).resize(300, 300).into(yyzzImage)
                            yyzzDelete.visibility = View.VISIBLE
                        }
                        CHOOSE_ELSE -> {
                            elseFileUrl.add(uploadFileInfoRes.retRes.file_url)
                            imageData.add(file)
                            imageAdapter.notifyDataSetChanged()
                        }
                    }
                }
            })
            setDialog()
            uploadFile(list)
        }
    }

}