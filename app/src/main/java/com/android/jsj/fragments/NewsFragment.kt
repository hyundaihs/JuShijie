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
import com.android.jsj.adapters.NewsAdapter
import com.android.jsj.entity.CompanyInfo
import com.android.jsj.entity.CompanyInfoListRes
import com.android.jsj.entity.NEWSLISTS
import com.android.jsj.entity.getInterface
import com.android.jsj.ui.NewsDetailsActivity
import com.android.shuizu.myutillibrary.adapter.LineDecoration
import com.android.shuizu.myutillibrary.adapter.MyBaseAdapter
import com.android.shuizu.myutillibrary.fragment.BaseFragment
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.android.shuizu.myutillibrary.utils.getErrorDialog
import com.android.shuizu.myutillibrary.widget.SwipeRefreshAndLoadLayout
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_listview_menus.*
import kotlinx.android.synthetic.main.layout_list_empty.*
import java.util.*

/**
 * ChaYin
 * Created by ${蔡雨峰} on 2019/8/14/014.
 */
class NewsFragment : BaseFragment() {

    companion object {
        var aId = 0
        var mkId = 0
        var zdyId = 0
    }

    private val companyInfoList = ArrayList<CompanyInfo>()
    private val companyInfoAdapter = NewsAdapter(companyInfoList)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_listview_menus, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initListView()
    }

    private fun initListView() {
        val layoutManager = LinearLayoutManager(context)
        listView.layoutManager = layoutManager
        layoutManager.orientation = OrientationHelper.VERTICAL
        listView.addItemDecoration(LineDecoration(context, LineDecoration.VERTICAL))
        listView.itemAnimator = DefaultItemAnimator()
        listView.isNestedScrollingEnabled = false
        listView.setEmptyView(emptyView)
        listViewSwipe.setOnRefreshListener(object : SwipeRefreshAndLoadLayout.OnRefreshListener {
            override fun onRefresh() {
                refresh()
            }

            override fun onLoadMore(currPage: Int, totalPages: Int) {
                loadMore(currPage)
            }
        })
        listView.setBackgroundResource(R.drawable.white_rect_round)
        listView.adapter = companyInfoAdapter
        companyInfoAdapter.myOnItemClickListener = object : MyBaseAdapter.MyOnItemClickListener {
            override fun onItemClick(parent: MyBaseAdapter, view: View, position: Int) {
                NewsDetailsActivity.aId = companyInfoList[position].id
                startActivity(Intent(view.context, NewsDetailsActivity::class.java))
            }
        }
        refresh()
    }

    private fun refresh() {
        getCompanyInfo(listViewSwipe.currPage, true)
    }

    private fun loadMore(currPage: Int) {
        getCompanyInfo(currPage)
    }

    private fun getCompanyInfo(page: Int, isRefresh: Boolean = false) {
        val map = mapOf(
            Pair("page_size", "15"),
            Pair("page", page),
            Pair("id", aId),
            Pair("mk_id", mkId)
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
                        companyInfoList.clear()
                    }
                    companyInfoList.addAll(companyInfoListRes.retRes)
                    companyInfoAdapter.notifyDataSetChanged()
                    listViewSwipe.isRefreshing = false
                }
            })
            setDataMap(map)
            postRequest()
        }
    }
}