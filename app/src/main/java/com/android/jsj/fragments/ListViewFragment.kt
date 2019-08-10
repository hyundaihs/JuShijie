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
import com.android.jsj.R
import com.android.jsj.entity.*
import com.android.jsj.ui.WebActivity
import com.android.shuizu.myutillibrary.adapter.LineDecoration
import com.android.shuizu.myutillibrary.adapter.MyBaseAdapter
import com.android.shuizu.myutillibrary.fragment.BaseFragment
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.android.shuizu.myutillibrary.request.getErrorDialog
import com.android.shuizu.myutillibrary.utils.CalendarUtil
import com.android.shuizu.myutillibrary.utils.CalendarUtil.MM_DD_HH_MM
import com.android.shuizu.myutillibrary.widget.SwipeRefreshAndLoadLayout
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_listview.*
import kotlinx.android.synthetic.main.fragment_minepage.*
import kotlinx.android.synthetic.main.layout_merchant_ranking_list_item.view.*
import kotlinx.android.synthetic.main.layout_message_list_item.view.*
import java.util.*

/**
 * ChaYin
 * Created by ${蔡雨峰} on 2019/7/30/030.
 */
class ListViewFragment : BaseFragment() {

    companion object {
        const val PAGE_KEY = "page_key"
        const val LOAD_MESSAGE = 1 //我的消息
        const val LOAD_WORLD = 2 //我的饰界
        const val LOAD_RANKING = 3 //商家排名
        const val LOAD_ALIVE = 4 //直播
        const val LOAD_MERCHANT = 5 //商家
        const val LOAD_CASE = 6 //案例
        const val LOAD_DESIGNER = 7 //设计师
    }

    private var messageInfo = ArrayList<MessageInfo>()
    private var messageAdapter = MessageAdapter(messageInfo)

    private var merchantInfo = ArrayList<MerchantInfo>()
    private var merchantAdapter = MerchantAdapter(merchantInfo)
    private var pageKey = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_listview, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        pageKey = arguments!!.getInt(PAGE_KEY)
        initListView()
    }

    private fun initListView() {
        val layoutManager = LinearLayoutManager(context)
        listView.layoutManager = layoutManager
        layoutManager.orientation = OrientationHelper.VERTICAL
        listView.addItemDecoration(LineDecoration(context, LineDecoration.VERTICAL))
        listView.itemAnimator = DefaultItemAnimator()
        listView.isNestedScrollingEnabled = false
        listViewSwipe.setOnRefreshListener(object : SwipeRefreshAndLoadLayout.OnRefreshListener {
            override fun onRefresh() {
                refresh()
            }

            override fun onLoadMore(currPage: Int, totalPages: Int) {
                loadMore(currPage)
            }
        })
        when (pageKey) {
            LOAD_MESSAGE -> {
                listView.adapter = messageAdapter
                messageAdapter.myOnItemClickListener = object : MyBaseAdapter.MyOnItemClickListener {
                    override fun onItemClick(parent: MyBaseAdapter, view: View, position: Int) {
                        getMessageInfoDetails(messageInfo[position].id)
                    }
                }
            }
            LOAD_RANKING -> {
                listView.adapter = merchantAdapter
                merchantAdapter.myOnItemClickListener = object : MyBaseAdapter.MyOnItemClickListener {
                    override fun onItemClick(parent: MyBaseAdapter, view: View, position: Int) {
//                        getMessageInfoDetails(messageInfo[position].id)
                    }
                }
            }
        }
        refresh()
    }

    private fun refresh() {
        when (pageKey) {
            LOAD_MESSAGE -> {
                getMessageInfo(listViewSwipe.currPage, true)
            }
            LOAD_RANKING->{
                getMerchantInfo(listViewSwipe.currPage, true)
            }
        }

    }

    private fun loadMore(currPage: Int) {
        when (pageKey) {
            LOAD_MESSAGE -> {
                getMessageInfo(currPage)
            }
            LOAD_RANKING->{
                getMerchantInfo(currPage)
            }
        }
    }

    private fun getMerchantInfo(page: Int, isRefresh: Boolean = false) {
        val map = mapOf(
            Pair("page_size", "15"),
            Pair("page", page.toString())
        )
        KevinRequest.build(activity as Context).apply {
            setRequestUrl(SJLB.getInterface(map))
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error)
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    val merchantInfoListRes = Gson().fromJson(result, MerchantInfoListRes::class.java)
                    if (isRefresh) {
                        merchantInfo.clear()
                    }
                    merchantInfo.addAll(merchantInfoListRes.retRes)
                    merchantAdapter.notifyDataSetChanged()
                    listViewSwipe.isRefreshing = false
                }
            })
            setDataMap(map)
            postRequest()
        }
    }

    private fun getMessageInfo(page: Int, isRefresh: Boolean = false) {
        val map = mapOf(
            Pair("page_size", "15"),
            Pair("page", page.toString())
        )
        KevinRequest.build(activity as Context).apply {
            setRequestUrl(MSGLISTS.getInterface(map))
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error)
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    val messageInfoListRes = Gson().fromJson(result, MessageInfoListRes::class.java)
                    if (isRefresh) {
                        messageInfo.clear()
                    }
                    messageInfo.addAll(messageInfoListRes.retRes)
                    messageAdapter.notifyDataSetChanged()
                    listViewSwipe.isRefreshing = false
                }
            })
            setDataMap(map)
            postRequest()
        }
    }

    private fun getMessageInfoDetails(id: Int) {
        val map = mapOf(
            Pair("id", id.toString())
        )
        KevinRequest.build(activity as Context).apply {
            setRequestUrl(MSGINFO.getInterface(map))
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error)
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    val messageInfoRes = Gson().fromJson(result, MessageInfoRes::class.java)
                    WebActivity.pageTitle = messageInfoRes.retRes.from_title
                    WebActivity.pageContent = messageInfoRes.retRes.contents
                    startActivity(Intent(context, WebActivity::class.java))
                }
            })
            setDataMap(map)
            postRequest()
        }
    }

    private class MessageAdapter(val data: ArrayList<MessageInfo>) :
        MyBaseAdapter(R.layout.layout_message_list_item) {

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            val messageInfo = data[position]
            holder.itemView.isRead.isChecked = messageInfo.is_read == 0
            holder.itemView.msgFrom.text = messageInfo.from_title
            holder.itemView.msgType.text = messageInfo.type_str
            val c = CalendarUtil(messageInfo.create_time, true)
            holder.itemView.createTime.text = c.format(MM_DD_HH_MM)
        }

        override fun getItemCount(): Int = data.size
    }

    private class MerchantAdapter(val data: ArrayList<MerchantInfo>) :
        MyBaseAdapter(R.layout.layout_merchant_ranking_list_item) {

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            val merchantInfo = data[position]
            val level = if(position < 10){
                "0${position + 1}"
            }else{
                "${position + 1}"
            }
            holder.itemView.rankingNum.text = level
            Picasso.with(holder.itemView.context).load(merchantInfo.file_url.getImageUrl())
                .into(holder.itemView.photo)
            holder.itemView.name.text = merchantInfo.title
            holder.itemView.company.text = merchantInfo.pp_title
            holder.itemView.fsNumText.text = merchantInfo.gz_nums.toString()
            holder.itemView.gzNumText.text = merchantInfo.gz_nums.toString()
            holder.itemView.hzNumText.text = merchantInfo.zan_nums.toString()
            holder.itemView.ftNumText.text = merchantInfo.ft_nums.toString()
        }

        override fun getItemCount(): Int = data.size
    }


}