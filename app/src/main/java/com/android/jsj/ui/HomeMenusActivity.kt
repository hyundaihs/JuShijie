package com.android.jsj.ui

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.view.View
import android.widget.TextView
import com.android.jsj.JSJApplication
import com.android.jsj.R
import com.android.jsj.adapters.*
import com.android.jsj.entity.*
import com.android.jsj.fragments.NewsFragment
import com.android.shuizu.myutillibrary.D
import com.android.shuizu.myutillibrary.MyBaseActivity
import com.android.shuizu.myutillibrary.adapter.GridDivider
import com.android.shuizu.myutillibrary.adapter.LineDecoration
import com.android.shuizu.myutillibrary.adapter.MyBaseAdapter
import com.android.shuizu.myutillibrary.dp2px
import com.android.shuizu.myutillibrary.initActionBar
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.android.shuizu.myutillibrary.utils.getErrorDialog
import com.android.shuizu.myutillibrary.widget.SwipeRefreshAndLoadLayout
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_home_menus.*
import java.util.*

/**
 * ChaYin
 * Created by ${蔡雨峰} on 2019/8/17/017.
 */
class HomeMenusActivity : MyBaseActivity() {

    companion object {
        var pageKey = 0
        const val LOAD_MENUS1 = 1 //装饰品牌
        const val LOAD_MENUS2 = 2 //建材家具
        const val LOAD_MENUS3 = 3 //视频
        const val LOAD_MENUS5 = 5 //资讯
        const val LOAD_MENUS6 = 6 //案例
        const val LOAD_MENUS7 = 7 //设计师
        const val LOAD_MENUS8 = 8 //特卖
    }

    private val chooseTypes = ArrayList<ChooseType>()
    private val types = ArrayList<Map<String, String>>()
    private lateinit var typesLayout: List<View>
    private lateinit var typesViews: List<TextView>
    private val currIds = IntArray(3) { 0 }
    private val companyInfoList = ArrayList<CompanyInfo>()
    private val merchantInfoList = ArrayList<MerchantInfo>()
    private lateinit var baseAdapter: MyBaseAdapter
    private var mkId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_menus)
        typesLayout = listOf(layout1, layout2, layout3)
        typesViews = listOf<TextView>(chooseType1, chooseType2, chooseType3)
        initChooseType()
        initViews()
    }

    private fun initChooseType() {
        types.clear()
        when (pageKey) {
            LOAD_MENUS1 -> {
                types.add(mapOf(Pair("id", "4"), Pair("title", "区域")))
                types.add(mapOf(Pair("id", "5"), Pair("title", "模式")))
                types.add(mapOf(Pair("id", "13"), Pair("title", "其它")))
            }
            LOAD_MENUS2 -> {
                types.add(mapOf(Pair("id", "8"), Pair("title", "家具类型")))
            }
            LOAD_MENUS3 -> {
                types.add(mapOf(Pair("id", "12"), Pair("title", "筛选")))
                mkId = 3
            }
            LOAD_MENUS5 -> {
                types.add(mapOf(Pair("id", "1"), Pair("title", "信息源")))
                types.add(mapOf(Pair("id", "2"), Pair("title", "类型")))
                mkId = 1
            }
            LOAD_MENUS6 -> {
                types.add(mapOf(Pair("id", "4"), Pair("title", "区域")))
                types.add(mapOf(Pair("id", "3"), Pair("title", "风格")))
                types.add(mapOf(Pair("id", "10"), Pair("title", "等级")))
                mkId = 5
            }
            LOAD_MENUS7 -> {
                types.add(mapOf(Pair("id", "9"), Pair("title", "从业时间")))
                types.add(mapOf(Pair("id", "10"), Pair("title", "职称")))
                types.add(mapOf(Pair("id", "11"), Pair("title", "设计费")))
                mkId = 4
            }
            LOAD_MENUS8 -> {
                types.add(mapOf(Pair("id", "12"), Pair("title", "筛选")))
                types.add(mapOf(Pair("id", "6"), Pair("title", "材料")))
                mkId = 6
            }
        }
        initTypeViews()
    }

    private fun initTypeViews() {
        for (i in 0 until types.size) {
            typesLayout[i].visibility = View.VISIBLE
            typesViews[i].text = types[i]["title"]
            typesViews[i].setOnClickListener {
                chooseTypes.clear()
                chooseTypes.add(ChooseType(0, "不限"))
                chooseTypes.addAll(JSJApplication.chooseType.getValue(types[i]["id"]!!))
                PickerUtil.initChooseType(chooseTypes)
                PickerUtil.showChooseType(it.context, types[i]["title"]) { options1, options2, options3, v ->
                    currIds[i] = chooseTypes[options1].id
                    typesViews[i].text = chooseTypes[options1].pickerViewText
                    menusPageSwipe.isRefreshing = true
                    refresh()
                }
            }
        }
    }

    private fun initViews() {
        val m = dp2px(10f).toInt()
        when (pageKey) {
            LOAD_MENUS7 -> {
                val gridLayoutManager = GridLayoutManager(this, 2)
                menusPageListView.layoutManager = gridLayoutManager
                //水平分割线
                menusPageListView.addItemDecoration(GridDivider(this, dp2px(10f).toInt(), 2))
                menusPageListView.itemAnimator = DefaultItemAnimator()
                menusPageListView.isNestedScrollingEnabled = false
                menusPageListView.setBackgroundResource(android.R.color.transparent)
            }
            else -> {
                val layoutManager = LinearLayoutManager(this)
                menusPageListView.layoutManager = layoutManager
                layoutManager.orientation = OrientationHelper.VERTICAL
                menusPageListView.addItemDecoration(LineDecoration(this, LineDecoration.VERTICAL))
                menusPageListView.itemAnimator = DefaultItemAnimator()
                menusPageListView.isNestedScrollingEnabled = false
                menusPageListView.setBackgroundResource(R.drawable.white_rect_round)
            }
        }
        when (pageKey) {
            LOAD_MENUS1 -> {
                initActionBar(this, "装饰品牌")
                baseAdapter = MerchantAdapter(merchantInfoList)
                menusPageListView.adapter = baseAdapter
                menusPageSwipe.setPadding(0, 0, 0, 0)
            }
            LOAD_MENUS2 -> {
                initActionBar(this, "建材家具")
                baseAdapter = MerchantAdapter(merchantInfoList)
                menusPageListView.adapter = baseAdapter
                menusPageSwipe.setPadding(0, 0, 0, 0)
            }
            LOAD_MENUS3 -> {
                initActionBar(this, "视频")
                baseAdapter = VideoAdapter(companyInfoList)
                menusPageListView.adapter = baseAdapter
                menusPageSwipe.setPadding(m, m, m, m)
            }
            LOAD_MENUS5 -> {
                initActionBar(this, "资讯")
                baseAdapter = NewsAdapter(companyInfoList)
                menusPageListView.adapter = baseAdapter
                menusPageSwipe.setPadding(m, m, m, m)
            }
            LOAD_MENUS6 -> {
                initActionBar(this, "案例")
                baseAdapter = CaseAdapter(companyInfoList)
                menusPageListView.adapter = baseAdapter
                menusPageSwipe.setPadding(m, m, m, m)
            }
            LOAD_MENUS7 -> {
                initActionBar(this, "设计师")
                baseAdapter = DesignerAdapter(companyInfoList)
                menusPageListView.adapter = baseAdapter
                menusPageSwipe.setPadding(m, m, m, m)
            }
            LOAD_MENUS8 -> {
                initActionBar(this, "特卖")
                baseAdapter = SaleAdapter(companyInfoList)
                menusPageListView.adapter = baseAdapter
                menusPageSwipe.setPadding(m, m, m, m)
            }
        }
        menusPageSwipe.setOnRefreshListener(object : SwipeRefreshAndLoadLayout.OnRefreshListener {
            override fun onRefresh() {
                refresh()
            }

            override fun onLoadMore(currPage: Int, totalPages: Int) {
                loadMore(currPage)
            }
        })
        baseAdapter.myOnItemClickListener = object : MyBaseAdapter.MyOnItemClickListener {
            override fun onItemClick(parent: MyBaseAdapter, view: View, position: Int) {
                val companyInfo = companyInfoList[position]

            }
        }
        refresh()
    }


    private fun refresh() {
        when (pageKey) {
            LOAD_MENUS1, LOAD_MENUS2 -> {
                getPPInfoList(menusPageSwipe.currPage, true)
            }
            else -> {
                getCompanyInfoList(menusPageSwipe.currPage, true)
            }
        }

    }

    private fun loadMore(currPage: Int) {
        when (pageKey) {
            LOAD_MENUS1, LOAD_MENUS2 -> {
                getPPInfoList(currPage)
            }
            else -> {
                getCompanyInfoList(currPage)
            }
        }

    }

    private fun getPPInfoList(page: Int, isRefresh: Boolean = false) {
        val map = mapOf(Pair("page_size", "10"), Pair("page", page)).toMutableMap()
        when (pageKey) {
            LOAD_MENUS1 -> {
                map["bdls_id"] = currIds[0]
                map["qbzb_id"] = currIds[1]
                map["zqybj_id"] = currIds[2]
            }
            LOAD_MENUS2 -> {
                map["ppfl_id"] = currIds[0]
            }
        }
        KevinRequest.build(this).apply {
            setRequestUrl(PPLISTS.getInterface(map))
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error)
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    val liveInfoListRes = Gson().fromJson(result, MerchantInfoListRes::class.java)
                    menusPageSwipe.setTotalPages(liveInfoListRes.retCounts, 10)
                    if (isRefresh) {
                        merchantInfoList.clear()
                    }
                    merchantInfoList.addAll(liveInfoListRes.retRes)
                    baseAdapter.notifyDataSetChanged()
                    menusPageSwipe.isRefreshing = false
                }
            })
            setDataMap(map)
            postRequest()
        }
    }

    /**
     * type_id:信息源id（1：新闻，2：活动，3：推广，4：饰界 ）(对应系统分类标识 1)
    zxlx_id:资讯类型id（205：装饰建材，206：家具） (对应系统分类标识 2)
    fengge_id:风格id (对应系统分类标识 3)
    sjsdj_id:设计师等级id (对应系统分类标识 10)
    jy_id:经验id(对应系统分类标识 9)
    jg_id:价格(对应系统分类标识 11)
    sp_id：商品id（商品详情推荐用）
    jcfl_id：建材分类id(对应系统分类标识 6)
    px:排序（1：最新，2:最热 ，3：主推 不传默认最新）
     */
    private fun getCompanyInfoList(page: Int, isRefresh: Boolean = false) {
        val map = mapOf(Pair("page_size", "10"), Pair("page", page), Pair("mk_id", mkId)).toMutableMap()
        when (pageKey) {
            LOAD_MENUS3 -> {
                map["px"] = currIds[0]
            }
            LOAD_MENUS5 -> {
                map["type_id"] = currIds[0]
                map["zxlx_id"] = currIds[1]
            }
            LOAD_MENUS6 -> {
                map["fengge_id"] = currIds[1]
                map["sjsdj_id"] = currIds[2]
            }
            LOAD_MENUS7 -> {
                map["jy_id"] = currIds[0]
                map["sjsdj_id"] = currIds[1]
                map["jg_id"] = currIds[2]
            }
            LOAD_MENUS8 -> {
                map["px"] = currIds[0]
                map["jcfl_id"] = currIds[1]
            }
        }
        KevinRequest.build(this).apply {
            setRequestUrl(NEWSLISTS.getInterface(map))
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error)
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    val liveInfoListRes = Gson().fromJson(result, CompanyInfoListRes::class.java)
                    menusPageSwipe.setTotalPages(liveInfoListRes.retCounts, 10)
                    if (isRefresh) {
                        companyInfoList.clear()
                    }
                    companyInfoList.addAll(liveInfoListRes.retRes)
                    baseAdapter.notifyDataSetChanged()
                    menusPageSwipe.isRefreshing = false
                }
            })
            setDataMap(map)
            postRequest()
        }
    }
}