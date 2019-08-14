package com.android.jsj.adapters

import com.android.jsj.R
import com.android.jsj.entity.CompanyInfo
import com.android.jsj.entity.getImageUrl
import com.android.shuizu.myutillibrary.adapter.MyBaseAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_honor_list_item.view.*
import java.util.*

/**
 * ChaYin
 * Created by ${蔡雨峰} on 2019/8/14/014.
 */
class HonorAdapter(val data: ArrayList<CompanyInfo>) : MyBaseAdapter(R.layout.layout_honor_list_item) {

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val companyInfo = data[position]
        Picasso.with(holder.itemView.context).load(companyInfo.file_url.getImageUrl()).into(holder.itemView.image)
        holder.itemView.title.text = companyInfo.title
    }

    override fun getItemCount(): Int = data.size
}