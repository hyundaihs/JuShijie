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
import com.android.jsj.adapters.*
import com.android.jsj.entity.*
import com.android.jsj.ui.CompanyShowActivity
import com.android.jsj.ui.WebActivity
import com.android.shuizu.myutillibrary.adapter.LineDecoration
import com.android.shuizu.myutillibrary.adapter.MyBaseAdapter
import com.android.shuizu.myutillibrary.fragment.BaseFragment
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.android.shuizu.myutillibrary.utils.getErrorDialog
import com.android.shuizu.myutillibrary.widget.SwipeRefreshAndLoadLayout
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_listview.*
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

    private var pageKey = 0
    private var aId = 0
    private var mkId = 0
    private var messageInfo = ArrayList<MessageInfo>()
    private var messageAdapter = MessageAdapter(messageInfo)

    private var rankingInfo = ArrayList<MerchantInfo>()
    private var rankingAdapter = RankingAdapter(rankingInfo)

    private var merchantInfo = ArrayList<MerchantInfo>()
    private var merchantAdapter = MerchantAdapter(merchantInfo)

    private val liveInfoList = ArrayList<LiveInfo>()
    private val liveAdapter = LiveAdapter(liveInfoList)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_listview, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        pageKey = arguments!!.getInt(PAGE_KEY)
        aId = arguments!!.getInt("id")
        mkId = arguments!!.getInt("mk_id", 0)
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
                listView.setBackgroundResource(R.drawable.white_rect_round)
                listView.adapter = messageAdapter
                messageAdapter.myOnItemClickListener = object : MyBaseAdapter.MyOnItemClickListener {
                    override fun onItemClick(parent: MyBaseAdapter, view: View, position: Int) {
                        getMessageInfoDetails(messageInfo[position].id)
                    }
                }
            }
            LOAD_RANKING -> {
                listView.setBackgroundResource(R.drawable.white_rect_round)
                listView.adapter = rankingAdapter
                rankingAdapter.myOnItemClickListener = object : MyBaseAdapter.MyOnItemClickListener {
                    override fun onItemClick(parent: MyBaseAdapter, view: View, position: Int) {

                    }
                }
            }
            LOAD_ALIVE -> {
                listView.setBackgroundResource(android.R.color.transparent)
                listView.adapter = liveAdapter
                liveAdapter.myOnItemClickListener = object : MyBaseAdapter.MyOnItemClickListener {
                    override fun onItemClick(parent: MyBaseAdapter, view: View, position: Int) {

                    }
                }
            }
            LOAD_MERCHANT -> {
                listView.setBackgroundResource(R.drawable.white_rect_round)
                listView.adapter = merchantAdapter
                merchantAdapter.myOnItemClickListener = object : MyBaseAdapter.MyOnItemClickListener {
                    override fun onItemClick(parent: MyBaseAdapter, view: View, position: Int) {
                        val intent = Intent(view.context, CompanyShowActivity::class.java)
                        intent.putExtra("id", merchantInfo[position].id)
                        startActivity(intent)
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
            LOAD_RANKING -> {
                getRankingInfo(listViewSwipe.currPage, true)
            }
            LOAD_ALIVE -> {
                getLiveInfo(listViewSwipe.currPage, true)
            }
            LOAD_MERCHANT -> {
                getMerchantInfo(listViewSwipe.currPage, true)
            }
        }

    }

    private fun loadMore(currPage: Int) {
        when (pageKey) {
            LOAD_MESSAGE -> {
                getMessageInfo(currPage)
            }
            LOAD_RANKING -> {
                getRankingInfo(currPage)
            }
            LOAD_ALIVE -> {
                getLiveInfo(currPage)
            }
            LOAD_MERCHANT -> {
                getMerchantInfo(currPage)
            }
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
                    listViewSwipe.setTotalPages(messageInfoListRes.retCounts, 15)
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

    private fun getRankingInfo(page: Int, isRefresh: Boolean = false) {
        val map = mapOf(
            Pair("page_size", "15"),
            Pair("page", page.toString()),
            Pair("gz", 0)
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
                    listViewSwipe.setTotalPages(merchantInfoListRes.retCounts, 15)
                    if (isRefresh) {
                        rankingInfo.clear()
                    }
                    rankingInfo.addAll(merchantInfoListRes.retRes)
                    rankingAdapter.notifyDataSetChanged()
                    listViewSwipe.isRefreshing = false
                }
            })
            setDataMap(map)
            postRequest()
        }
    }

    private fun getMerchantInfo(page: Int, isRefresh: Boolean = false) {
        val map = mapOf(
            Pair("page_size", "15"),
            Pair("page", page.toString()),
            Pair("gz", 1)
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
                    listViewSwipe.setTotalPages(merchantInfoListRes.retCounts, 15)
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

    private fun getLiveInfo(page: Int, isRefresh: Boolean = false) {
        val map = mapOf(
            Pair("page_size", "15"),
            Pair("page", page.toString()),
            Pair("gz", "1")
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
                    listViewSwipe.setTotalPages(liveInfoListRes.retCounts, 15)
                    if (isRefresh) {
                        liveInfoList.clear()
                    }
                    liveInfoList.addAll(liveInfoListRes.retRes)
                    liveAdapter.notifyDataSetChanged()
                    listViewSwipe.isRefreshing = false
                }
            })
            setDataMap(map)
            postRequest()
        }
    }
}