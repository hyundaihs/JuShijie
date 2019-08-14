package com.android.jsj.adapters

import com.android.jsj.R
import com.android.jsj.entity.CompanyInfo
import com.android.shuizu.myutillibrary.adapter.MyBaseAdapter
import com.android.shuizu.myutillibrary.utils.CalendarUtil
import kotlinx.android.synthetic.main.layout_news_list_item.view.*
import java.util.*

/**
 * ChaYin
 * Created by ${蔡雨峰} on 2019/8/14/014.
 */
class NewsAdapter(val data: ArrayList<CompanyInfo>) : MyBaseAdapter(R.layout.layout_news_list_item) {

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val companyInfo = data[position]
        when (companyInfo.type_id) {
            1 -> {
                holder.itemView.type.setBackgroundResource(R.drawable.rect_radius_bg_14)
                holder.itemView.type.text = "新闻"
            }
            2 -> {
                holder.itemView.type.setBackgroundResource(R.drawable.rect_radius_bg_15)
                holder.itemView.type.text = "活动"
            }
            3 -> {
                holder.itemView.type.setBackgroundResource(R.drawable.rect_radius_bg_7)
                holder.itemView.type.text = "推广"
            }
            else -> {
                holder.itemView.type.setBackgroundResource(R.drawable.rect_radius_bg_16)
                holder.itemView.type.text = "饰界"
            }
        }
        holder.itemView.content.text = companyInfo.title
        holder.itemView.hot.text = companyInfo.view_nums.toString()
        holder.itemView.time.text = CalendarUtil(companyInfo.create_time, true).format(CalendarUtil.YYYY_MM_DD)
    }

    override fun getItemCount(): Int = data.size
}