package com.android.jsj.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.jsj.JSJApplication.Companion.chooseType
import com.android.jsj.R
import com.android.jsj.entity.*
import com.android.jsj.ui.LiveDetailsActivity
import com.android.jsj.ui.LivePageActivity
import com.android.jsj.ui.WebActivity
import com.android.shuizu.myutillibrary.adapter.GridDivider
import com.android.shuizu.myutillibrary.adapter.MyBaseAdapter
import com.android.shuizu.myutillibrary.dp2px
import com.android.shuizu.myutillibrary.fragment.BaseFragment
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.android.shuizu.myutillibrary.request.getErrorDialog
import com.android.shuizu.myutillibrary.request.getMessageDialog
import com.android.shuizu.myutillibrary.request.getSuccessDialog
import com.android.shuizu.myutillibrary.widget.SwipeRefreshAndLoadLayout
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_livepage.*
import kotlinx.android.synthetic.main.layout_live_list_item.view.*
import org.jetbrains.anko.toast

/**
 * ChaYin
 * Created by ${蔡雨峰} on 2019/7/23/023.
 */
class LivepageFragment : BaseFragment() {

    private val liveInfoList = ArrayList<LiveInfo>()
    private val liveAdapter = LiveAdapter(liveInfoList)
    private val chooseTypes = ArrayList<ChooseType>()
    private var currType = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_livepage, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initViews()
    }

    private fun initViews() {
        chooseTypes.clear()
        chooseTypes.add(ChooseType(0, "类型不限"))
        chooseTypes.addAll(chooseType.getValue("14"))
        PickerUtil.initChooseType(chooseTypes)

        val gridLayoutManager = GridLayoutManager(context, 2)
        livePageListView.layoutManager = gridLayoutManager
        //水平分割线
        livePageListView.addItemDecoration(GridDivider(context, context?.dp2px(10f)!!.toInt(), 2))
        livePageListView.itemAnimator = DefaultItemAnimator()
        livePageListView.adapter = liveAdapter
        livePageListView.isNestedScrollingEnabled = false
        livePageSwipe.setOnRefreshListener(object : SwipeRefreshAndLoadLayout.OnRefreshListener {
            override fun onRefresh() {
                refresh()
            }

            override fun onLoadMore(currPage: Int, totalPages: Int) {
                loadMore(currPage)
            }
        })
        liveAdapter.myOnItemClickListener = object : MyBaseAdapter.MyOnItemClickListener {
            override fun onItemClick(parent: MyBaseAdapter, view: View, position: Int) {
                val liveInfo = liveInfoList[position]
//                if(liveInfo.gm_status == 1){
//                    if(liveInfo.zb_status == 1){//已付费并且正在直播
//                        val intent = Intent(context, LivePageActivity::class.java)
//                        intent.putExtra("url", liveInfoList[position].video_file_url)
//                        startActivity(intent)
//                    }else{
//                        getMessageDialog(view.context,"主播还没开播呢！")
//                    }
//                }else{
//                    if(liveInfo.zb_status == 1){//未付费已经开播
//                        getMessageDialog(view.context,"您没有购买，无法观看！")
//                    }else{
//                        val intent = Intent(context, LiveDetailsActivity::class.java)
//                        intent.putExtra("id", liveInfoList[position].id)
//                        startActivity(intent)
//                    }
//                }
                val intent = Intent(context, LiveDetailsActivity::class.java)
                intent.putExtra("id", liveInfoList[position].id)
                startActivity(intent)
            }
        }
        refresh()
        chooseAddress.setOnClickListener {
            PickerUtil.showAddress(context) { options1, options2, options3, v ->
                chooseAddress.text = chooseTypes[options1].pickerViewText
            }
        }
        chooseMerchant.setOnClickListener {
            PickerUtil.showChooseType(context, "商家类型") { options1, options2, options3, v ->
                currType = chooseTypes[options1].id
                chooseMerchant.text = chooseTypes[options1].pickerViewText
                livePageSwipe.isRefreshing = true
                refresh()
            }
        }

    }


    private fun refresh() {
        getLiveList(livePageSwipe.currPage, true)
    }

    private fun loadMore(currPage: Int) {
        getLiveList(currPage)
    }

    private fun getLiveList(page: Int, isRefresh: Boolean = false) {
        val map = mapOf(
            Pair("page_size", "20"),
            Pair("page", page),
            Pair("ppfl_id", currType),
            Pair("gz", "0")
        )
        KevinRequest.build(activity as Context).apply {
            setRequestUrl(ZBLB.getInterface(map))
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error)
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    val liveInfoListRes = Gson().fromJson(result, LiveInfoListRes::class.java)
                    if (isRefresh) {
                        liveInfoList.clear()
                    }
                    liveInfoList.addAll(liveInfoListRes.retRes)
                    liveAdapter.notifyDataSetChanged()
                    livePageSwipe.isRefreshing = false
                }
            })
            setDataMap(map)
            postRequest()
        }
    }

    private class LiveAdapter(val data: ArrayList<LiveInfo>) : MyBaseAdapter(R.layout.layout_live_list_item) {

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            val liveInfo = data[position]
            Picasso.with(holder.itemView.context).load(liveInfo.file_url.getImageUrl()).into(holder.itemView.livePhoto)
            when (liveInfo.zb_status) {
                0 -> {

                }
                1 -> {
                    holder.itemView.liveStatus.text = "直播中"
                    holder.itemView.liveStatus.setBackgroundResource(R.drawable.bg_8_rect_corner)
                }
                2 -> {

                }
            }
            holder.itemView.liveCompany.text = liveInfo.account_title
            holder.itemView.liveHot.text = "${liveInfo.view_nums}w"
            holder.itemView.liveTitle.text = liveInfo.title
        }

        override fun getItemCount(): Int = data.size
    }


}