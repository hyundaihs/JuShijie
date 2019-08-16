package com.android.jsj.adapters

import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.android.jsj.R
import com.android.jsj.entity.CompanyInfo
import com.android.jsj.entity.FileInfo
import com.android.jsj.entity.getImageUrl
import com.android.shuizu.myutillibrary.adapter.GridDivider
import com.android.shuizu.myutillibrary.adapter.MyBaseAdapter
import com.android.shuizu.myutillibrary.dp2px
import com.android.shuizu.myutillibrary.utils.CalendarUtil
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_upload_image_list_item.view.*
import kotlinx.android.synthetic.main.layout_world_list_item.view.*
import java.util.*

/**
 * JuShijie
 * Created by 蔡雨峰 on 2019/8/16.
 */
class WorldAdapter(val data: ArrayList<CompanyInfo>) : MyBaseAdapter(R.layout.layout_world_list_item) {

    private val imageData = ArrayList<FileInfo>()
    private val imageAdapter = ImageAdapter(imageData)

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val companyInfo = data[position]
        Picasso.with(holder.itemView.context).load(companyInfo.account_file_url.getImageUrl()).resize(100, 100)
            .into(holder.itemView.photo)
        holder.itemView.accountTitle.text = companyInfo.account_title
        holder.itemView.content.text = companyInfo.sub_info
        holder.itemView.time.text = CalendarUtil(companyInfo.create_time, true).format(CalendarUtil.MM_DD_HH_MM)
        holder.itemView.content.text = companyInfo.title
        holder.itemView.zan.text = companyInfo.zan_nums.toString()
        holder.itemView.gz.text = companyInfo.gz_nums.toString()
        holder.itemView.pl.text = companyInfo.pl_nums.toString()

        val gridLayoutManager1 = GridLayoutManager(holder.itemView.context, 5)
        holder.itemView.images.layoutManager = gridLayoutManager1
        //水平分割线
        holder.itemView.images.addItemDecoration(GridDivider(holder.itemView.context, holder.itemView.context.dp2px(10f).toInt(), 5))
        holder.itemView.images.itemAnimator = DefaultItemAnimator()
        holder.itemView.images.adapter = imageAdapter
        holder.itemView.images.isNestedScrollingEnabled = false

    }

    override fun getItemCount(): Int = data.size

    private class ImageAdapter(val data: ArrayList<FileInfo>) : MyBaseAdapter(R.layout.layout_upload_image_list_item) {

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            val file = data[position]
            Picasso.with(holder.itemView.context).load(file.resize_file_url).into(holder.itemView.uploadImage)
            holder.itemView.uploadDelete.visibility = View.GONE
        }

        override fun getItemCount(): Int = data.size
    }
}