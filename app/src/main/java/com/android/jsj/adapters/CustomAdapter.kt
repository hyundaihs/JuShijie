package com.android.jsj.adapters

import com.android.jsj.R
import com.android.jsj.entity.CompanyInfo
import com.android.jsj.entity.getImageUrl
import com.android.shuizu.myutillibrary.adapter.MyBaseAdapter
import com.android.shuizu.myutillibrary.utils.CalendarUtil
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_custom_list_item.view.*
import java.util.ArrayList

/**
 * ChaYin
 * Created by ${蔡雨峰} on 2019/8/14/014.
 */
class CustomAdapter(val data: ArrayList<CompanyInfo>) : MyBaseAdapter(R.layout.layout_custom_list_item) {

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val companyInfo = data[position]
        Picasso.with(holder.itemView.context).load(companyInfo.file_url.getImageUrl()).resize(200, 180)
            .into(holder.itemView.logo)
        holder.itemView.name.text = companyInfo.title
        holder.itemView.content.text = companyInfo.sub_info
        holder.itemView.zan.text = companyInfo.zan_nums.toString()
        holder.itemView.hot.text = companyInfo.view_nums.toString()
        holder.itemView.time.text = CalendarUtil(companyInfo.create_time, true).format(CalendarUtil.YYYY_MM_DD)
    }

    override fun getItemCount(): Int = data.size
}