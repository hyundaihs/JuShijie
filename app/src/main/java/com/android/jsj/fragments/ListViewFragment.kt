package com.android.jsj.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.jsj.R
import com.android.jsj.adapters.*
import com.android.jsj.entity.*
import com.android.jsj.ui.*
import com.android.shuizu.myutillibrary.adapter.GridDivider
import com.android.shuizu.myutillibrary.adapter.LineDecoration
import com.android.shuizu.myutillibrary.adapter.MyBaseAdapter
import com.android.shuizu.myutillibrary.dp2px
import com.android.shuizu.myutillibrary.fragment.BaseFragment
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.android.shuizu.myutillibrary.utils.getErrorDialog
import com.android.shuizu.myutillibrary.widget.SwipeRefreshAndLoadLayout
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_listview.*
import kotlinx.android.synthetic.main.layout_list_empty.*
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

    private var worldInfo = ArrayList<CompanyInfo>()
    private var worldAdapter = WorldAdapter(worldInfo)

    private var rankingInfo = ArrayList<MerchantInfo>()
    private var rankingAdapter = RankingAdapter(rankingInfo)

    private var merchantInfo = ArrayList<MerchantInfo>()
    private var merchantAdapter = MerchantAdapter(merchantInfo)

    private val liveInfoList = ArrayList<LiveInfo>()
    private val liveAdapter = LiveAdapter(liveInfoList)

    private val caseInfoList = ArrayList<CompanyInfo>()
    private val caseAdapter = CaseAdapter(caseInfoList)

    private val designerInfoList = ArrayList<CompanyInfo>()
    private val designerAdapter = DesignerAdapter(designerInfoList)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_listview, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        pageKey = arguments!!.getInt(PAGE_KEY,LOAD_MESSAGE)
        aId = arguments!!.getInt("id",0)
        mkId = arguments!!.getInt("mk_id", 0)
        initListView()
    }

    private fun initListView() {
        when (pageKey) {
            LOAD_ALIVE, LOAD_DESIGNER -> {
                val gridLayoutManager = GridLayoutManager(context, 2)
                listView.layoutManager = gridLayoutManager
                //水平分割线
                listView.addItemDecoration(GridDivider(context, context?.dp2px(10f)!!.toInt(), 2))
                listView.itemAnimator = DefaultItemAnimator()
                listView.isNestedScrollingEnabled = false
                listView.setEmptyView(emptyView)
                listView.setBackgroundResource(android.R.color.transparent)
            }
            else -> {
                val layoutManager = LinearLayoutManager(context)
                listView.layoutManager = layoutManager
                layoutManager.orientation = OrientationHelper.VERTICAL
                listView.addItemDecoration(LineDecoration(context, LineDecoration.VERTICAL))
                listView.itemAnimator = DefaultItemAnimator()
                listView.isNestedScrollingEnabled = false
                listView.setEmptyView(emptyView)
                listView.setBackgroundResource(R.drawable.white_rect_round)
            }
        }
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
            LOAD_WORLD -> {
                listView.adapter = worldAdapter
                worldAdapter.myOnItemClickListener = object : MyBaseAdapter.MyOnItemClickListener {
                    override fun onItemClick(parent: MyBaseAdapter, view: View, position: Int) {
                        WorldDetailsActivity.aId = worldInfo[position].id
                        startActivity(Intent(view.context, WorldDetailsActivity::class.java))
                    }
                }
            }
            LOAD_RANKING -> {
                listView.adapter = rankingAdapter
                rankingAdapter.myOnItemClickListener = object : MyBaseAdapter.MyOnItemClickListener {
                    override fun onItemClick(parent: MyBaseAdapter, view: View, position: Int) {
                        val intent = Intent(view.context, CompanyShowActivity::class.java)
                        intent.putExtra("id", rankingInfo[position].id)
                        startActivity(intent)
                    }
                }
            }
            LOAD_ALIVE -> {
                listView.adapter = liveAdapter
                liveAdapter.myOnItemClickListener = object : MyBaseAdapter.MyOnItemClickListener {
                    override fun onItemClick(parent: MyBaseAdapter, view: View, position: Int) {

                    }
                }
            }
            LOAD_MERCHANT -> {
                listView.adapter = merchantAdapter
                merchantAdapter.myOnItemClickListener = object : MyBaseAdapter.MyOnItemClickListener {
                    override fun onItemClick(parent: MyBaseAdapter, view: View, position: Int) {
                        val intent = Intent(view.context, CompanyShowActivity::class.java)
                        intent.putExtra("id", merchantInfo[position].id)
                        startActivity(intent)
                    }
                }
            }
            LOAD_CASE -> {
                listView.adapter = caseAdapter
                caseAdapter.myOnItemClickListener = object : MyBaseAdapter.MyOnItemClickListener {
                    override fun onItemClick(parent: MyBaseAdapter, view: View, position: Int) {
                        CaseDetailsActivity.aId = caseInfoList[position].id
                        startActivity(Intent(view.context, CaseDetailsActivity::class.java))
                    }
                }
            }
            LOAD_DESIGNER -> {
                listView.adapter = designerAdapter
                designerAdapter.myOnItemClickListener = object : MyBaseAdapter.MyOnItemClickListener {
                    override fun onItemClick(parent: MyBaseAdapter, view: View, position: Int) {
                        DesignerDetailsActivity.aId = designerInfoList[position].id
                        startActivity(Intent(view.context, DesignerDetailsActivity::class.java))
                    }
                }
            }
            else ->{
                listView.adapter = messageAdapter
                messageAdapter.myOnItemClickListener = object : MyBaseAdapter.MyOnItemClickListener {
                    override fun onItemClick(parent: MyBaseAdapter, view: View, position: Int) {
                        getMessageInfoDetails(messageInfo[position].id)
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
            LOAD_WORLD -> {
                getWorldInfo(listViewSwipe.currPage, true)
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
            LOAD_CASE -> {
                getCaseInfo(listViewSwipe.currPage, true)
            }
            LOAD_DESIGNER -> {
                getDesignerInfo(listViewSwipe.currPage, true)
            }
            else ->{
                getMessageInfo(listViewSwipe.currPage, true)
            }
        }

    }

    private fun loadMore(currPage: Int) {
        when (pageKey) {
            LOAD_MESSAGE -> {
                getMessageInfo(currPage)
            }
            LOAD_WORLD -> {
                getWorldInfo(currPage)
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
            LOAD_CASE -> {
                getCaseInfo(currPage)
            }
            LOAD_DESIGNER -> {
                getDesignerInfo(currPage)
            }
            else ->{
                getMessageInfo(currPage)
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

    private fun getWorldInfo(page: Int, isRefresh: Boolean = false) {
        val map = mapOf(
            Pair("page_size", "15"),
            Pair("page", page.toString()),
            Pair("gzsj", 1)
        )
        KevinRequest.build(activity as Context).apply {
            setRequestUrl(NEWSLISTS.getInterface(map))
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error)
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    val companyInfoListRes = Gson().fromJson(result, CompanyInfoListRes::class.java)
                    listViewSwipe.setTotalPages(companyInfoListRes.retCounts, 15)
                    if (isRefresh) {
                        worldInfo.clear()
                    }
                    worldInfo.addAll(companyInfoListRes.retRes)
                    worldAdapter.notifyDataSetChanged()
                    listViewSwipe.isRefreshing = false
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

    private fun getCaseInfo(page: Int, isRefresh: Boolean = false) {
        val map = mapOf(
            Pair("page_size", "15"),
            Pair("page", page.toString()),
            Pair("mk_id", 5),
            Pair("gz", 1)
        )
        KevinRequest.build(activity as Context).apply {
            setRequestUrl(NEWSLISTS.getInterface(map))
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error)
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    val companyInfoListRes = Gson().fromJson(result, CompanyInfoListRes::class.java)
                    listViewSwipe.setTotalPages(companyInfoListRes.retCounts, 15)
                    if (isRefresh) {
                        caseInfoList.clear()
                    }
                    caseInfoList.addAll(companyInfoListRes.retRes)
                    caseAdapter.notifyDataSetChanged()
                    listViewSwipe.isRefreshing = false
                }
            })
            setDataMap(map)
            postRequest()
        }
    }

    private fun getDesignerInfo(page: Int, isRefresh: Boolean = false) {
        val map = mapOf(
            Pair("page_size", "15"),
            Pair("page", page.toString()),
            Pair("mk_id", 4),
            Pair("gz", 1)
        )
        KevinRequest.build(activity as Context).apply {
            setRequestUrl(NEWSLISTS.getInterface(map))
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error)
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    val companyInfoListRes = Gson().fromJson(result, CompanyInfoListRes::class.java)
                    listViewSwipe.setTotalPages(companyInfoListRes.retCounts, 15)
                    if (isRefresh) {
                        designerInfoList.clear()
                    }
                    designerInfoList.addAll(companyInfoListRes.retRes)
                    designerAdapter.notifyDataSetChanged()
                    listViewSwipe.isRefreshing = false
                }
            })
            setDataMap(map)
            postRequest()
        }
    }
}