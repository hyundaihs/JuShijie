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
import com.android.jsj.adapters.MerchantAdapter
import com.android.jsj.entity.MerchantInfo
import com.android.jsj.entity.MerchantInfoListRes
import com.android.jsj.entity.SJLB
import com.android.jsj.entity.getInterface
import com.android.jsj.ui.CompanyShowActivity
import com.android.shuizu.myutillibrary.adapter.LineDecoration
import com.android.shuizu.myutillibrary.adapter.MyBaseAdapter
import com.android.shuizu.myutillibrary.fragment.BaseFragment
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.android.shuizu.myutillibrary.utils.getErrorDialog
import com.android.shuizu.myutillibrary.widget.SwipeRefreshAndLoadLayout
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_listview.*
import kotlinx.android.synthetic.main.layout_list_empty.*
import java.util.*

/**
 * JuShijie
 * Created by 蔡雨峰 on 2019/10/9.
 */
class MerchantFragment : BaseFragment() {

    private var merchantInfo = ArrayList<MerchantInfo>()
    private var merchantAdapter = MerchantAdapter(merchantInfo)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_listview, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
    }

    private fun initViews() {
        val layoutManager = LinearLayoutManager(context)
        listView.layoutManager = layoutManager
        layoutManager.orientation = OrientationHelper.VERTICAL
        listView.addItemDecoration(LineDecoration(context, LineDecoration.VERTICAL))
        listView.itemAnimator = DefaultItemAnimator()
        listView.isNestedScrollingEnabled = false
        listView.setEmptyView(emptyView)
        listView.setBackgroundResource(R.drawable.white_rect_round)
        listView.adapter = merchantAdapter
        merchantAdapter.myOnItemClickListener = object : MyBaseAdapter.MyOnItemClickListener {
            override fun onItemClick(parent: MyBaseAdapter, view: View, position: Int) {
                val intent = Intent(view.context, CompanyShowActivity::class.java)
                intent.putExtra("id", merchantInfo[position].id)
                startActivity(intent)
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
        refresh()
    }

    private fun refresh() {
        getMerchantInfo(listViewSwipe.currPage, true)

    }

    private fun loadMore(currPage: Int) {
        getMerchantInfo(currPage)
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
}