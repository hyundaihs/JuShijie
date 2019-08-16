package com.android.jsj.adapters

import com.android.jsj.R
import com.android.jsj.entity.CompanyInfo
import com.android.jsj.entity.getImageUrl
import com.android.shuizu.myutillibrary.adapter.MyBaseAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_video_list_item.view.*
import java.util.ArrayList

/**
 * ChaYin
 * Created by ${蔡雨峰} on 2019/8/14/014.
 */
class VideoAdapter(val data: ArrayList<CompanyInfo>) : MyBaseAdapter(R.layout.layout_video_list_item) {

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val companyInfo = data[position]
        Picasso.with(holder.itemView.context).load(companyInfo.file_url.getImageUrl()).resize(500, 400)
            .into(holder.itemView.image)
        holder.itemView.accountTitle.text = companyInfo.title
        holder.itemView.content.text = companyInfo.sub_info
    }

    override fun getItemCount(): Int = data.size
}