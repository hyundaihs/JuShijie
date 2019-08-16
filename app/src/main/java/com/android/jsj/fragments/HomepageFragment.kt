package com.android.jsj.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.jsj.JSJApplication.Companion.chooseType
import com.android.jsj.R
import com.android.jsj.entity.*
import com.android.jsj.ui.CompanyShowActivity
import com.android.jsj.ui.LoginActivity
import com.android.jsj.ui.WebActivity
import com.android.shuizu.myutillibrary.adapter.LineDecoration
import com.android.shuizu.myutillibrary.adapter.MyBaseAdapter
import com.android.shuizu.myutillibrary.fragment.BaseFragment
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.android.shuizu.myutillibrary.utils.getErrorDialog
import com.android.shuizu.myutillibrary.widget.GlideImageLoader
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_homepage.*
import kotlinx.android.synthetic.main.layout_merchant_list_item.view.*


/**
 * ChaYin
 * Created by ${蔡雨峰} on 2019/7/23/023.
 */
class HomepageFragment : BaseFragment() {

    private val bannerInfos = ArrayList<BannerInfo>()
    private var onlineNumInfo: OnlineNumInfo? = null
    private val provInfoList = ArrayList<ProvInfo>()
    private var prov = ""
    private var city = ""
    private var area = ""
    private val merchantInfo = ArrayList<MerchantInfo>()
    private val merchantAdapter = MerchantAdapter(merchantInfo)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_homepage, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getBanner()
        getOnlineNum()
        getChooseType()
        getAreas()
        getMerchantInfo()
        initViews()
    }

    private fun initViews() {
        menuBtn1.setOnClickListener { }
        menuBtn2.setOnClickListener { }
        menuBtn3.setOnClickListener { }
        menuBtn4.setOnClickListener { }
        menuBtn5.setOnClickListener { }
        menuBtn6.setOnClickListener { }
        menuBtn7.setOnClickListener { }
        menuBtn8.setOnClickListener { }
        val layoutManager = LinearLayoutManager(context)
        homepageList.layoutManager = layoutManager
        layoutManager.orientation = OrientationHelper.VERTICAL
        homepageList.addItemDecoration(LineDecoration(context, LineDecoration.VERTICAL))
        homepageList.itemAnimator = DefaultItemAnimator()
        homepageList.isNestedScrollingEnabled = false
        homepageList.adapter = merchantAdapter
        merchantAdapter.myOnItemClickListener = object : MyBaseAdapter.MyOnItemClickListener {
            override fun onItemClick(parent: MyBaseAdapter, view: View, position: Int) {
                val intent = Intent(view.context, CompanyShowActivity::class.java)
                intent.putExtra("id", merchantInfo[position].account_id)
                startActivity(intent)
            }
        }
    }

    private fun getBanner() {
        val map = mapOf(
            Pair("type_id", "2")
        )
        KevinRequest.build(activity as Context).apply {
            setRequestUrl(BANNER.getInterface(map))
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
            openLoginErrCallback(LoginActivity::class.java)
            setDataMap(map)
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
            getBannerInfo(banner.id)
        }
        banner.start()
    }

    private fun getBannerInfo(id:Int) {
        val map = mapOf(
            Pair("id", id)
        )
        KevinRequest.build(activity as Context).apply {
            setRequestUrl(BANNERINFO.getInterface(map))
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error)
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    val bannerInfoRes = Gson().fromJson(result, BannerInfoRes::class.java)
                    WebActivity.pageTitle = "详情"
                    WebActivity.pageContent = bannerInfoRes.retRes.contents
                    startActivity(Intent(context, WebActivity::class.java))
                }
            })
            openLoginErrCallback(LoginActivity::class.java)
            setDialog()
            setDataMap(map)
            postRequest()
        }
    }

    private fun getOnlineNum() {
        KevinRequest.build(activity as Context).apply {
            setRequestUrl(ONLINEINFO.getInterface())
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error)
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    val onlineNumInfoRes = Gson().fromJson(result, OnlineNumInfoRes::class.java)
                    onlineNumInfo = onlineNumInfoRes.retRes
                    addrText.text = onlineNumInfo?.city
                    onlineNum.text = onlineNumInfo?.online_num.toString()
                    areaText.text = onlineNumInfo?.area
                }
            })
            postRequest()
        }
    }

    private fun getChooseType() {
        KevinRequest.build(activity as Context).apply {
            setRequestUrl(STYPE.getInterface())
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error)
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    val chooseTypeMapRes = Gson().fromJson(result, ChooseTypeMapRes::class.java)
                    chooseType = chooseTypeMapRes.retRes
                }
            })
            postRequest()
        }
    }

    private fun getAreas() {
        KevinRequest.build(activity as Context).apply {
            setRequestUrl(AREA)
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, "地区信息获取失败，请检查网络后重试！")
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
                    addrText.setOnClickListener {
                        PickerUtil.showAddress(
                            activity as Context
                        ) { options1, options2, options3, v ->
                            //返回的分别是三个级别的选中位置
                            prov = provInfoList[options1].title
                            city = provInfoList[options1].lists[options2].title
                            area = provInfoList[options1].lists[options2].lists[options3].title
                            changeArea()
                        }
                    }
                }
            })
            setDialog()
            request()
        }
    }

    private fun changeArea() {
        val map = mapOf(
            Pair("province", prov),
            Pair("city", city),
            Pair("area", area)
        )
        KevinRequest.build(activity as Context).apply {
            setRequestUrl(CHANGEPCA.getInterface(map))
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error)
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    addrText.text = city
                    areaText.text = area
                }
            })
            setDataMap(map)
            setDialog()
            postRequest()
        }
    }

    private fun getMerchantInfo() {
        val map = mapOf(
            Pair("page_size", "10"),
            Pair("page", "1"),
            Pair("tg", "1")
        )
        KevinRequest.build(activity as Context).apply {
            setRequestUrl(PPLISTS.getInterface(map))
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error)
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    val merchantInfoListRes = Gson().fromJson(result, MerchantInfoListRes::class.java)
                    merchantInfo.clear()
                    merchantInfo.addAll(merchantInfoListRes.retRes)
                    merchantAdapter.notifyDataSetChanged()
                }
            })
            setDataMap(map)
            setDialog()
            postRequest()
        }
    }

    private class MerchantAdapter(val data: ArrayList<MerchantInfo>) :
        MyBaseAdapter(R.layout.layout_merchant_list_item) {

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            val merchantInfo = data[position]
            Picasso.with(holder.itemView.context).load(merchantInfo.pp_file_url.getImageUrl())
                .resize(200, 200)
                .into(holder.itemView.merchantLogo)
            holder.itemView.merchantName.text = merchantInfo.title
            holder.itemView.merchantCompany.text = merchantInfo.pp_title
            val tags = merchantInfo.tags.split(",")
            if (tags.isNotEmpty()) {
                holder.itemView.merchantTag1.text = tags[0]
            }
            if (tags.size >= 2) {
                holder.itemView.merchantTag2.text = tags[1]
            }
        }

        override fun getItemCount(): Int = data.size
    }

}