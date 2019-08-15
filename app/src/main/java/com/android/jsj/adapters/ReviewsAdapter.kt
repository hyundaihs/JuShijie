package com.android.jsj.adapters

import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import com.android.jsj.R
import com.android.jsj.entity.ReviewsInfo
import com.android.jsj.entity.getImageUrl
import com.android.shuizu.myutillibrary.adapter.LineDecoration
import com.android.shuizu.myutillibrary.adapter.MyBaseAdapter
import com.android.shuizu.myutillibrary.utils.CalendarUtil
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_reviews_list_item.view.*
import java.util.*

/**
 * JuShijie
 * Created by 蔡雨峰 on 2019/8/15.
 */
class ReviewsAdapter(val data: ArrayList<ReviewsInfo>) : MyBaseAdapter(R.layout.layout_reviews_list_item) {

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val reviewsInfo = data[position]
        Picasso.with(holder.itemView.context).load(reviewsInfo.account_file_url.getImageUrl()).resize(100, 100)
            .into(holder.itemView.photo)
        holder.itemView.sender.text = reviewsInfo.account_title
        holder.itemView.sendContent.text = reviewsInfo.title
        holder.itemView.publishTime.text = CalendarUtil(reviewsInfo.create_time, true).format(CalendarUtil.YYYY_MM_DD)
        holder.itemView.zan.text = reviewsInfo.zan_nums.toString()
        holder.itemView.pingLun.text = reviewsInfo.pl_nums.toString()

        val layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.itemView.replyList.layoutManager = layoutManager
        layoutManager.orientation = OrientationHelper.VERTICAL
        holder.itemView.replyList.addItemDecoration(LineDecoration(holder.itemView.context, LineDecoration.VERTICAL))
        holder.itemView.replyList.itemAnimator = DefaultItemAnimator()
        holder.itemView.replyList.isNestedScrollingEnabled = false
        holder.itemView.replyList.adapter = ReplyAdapter(reviewsInfo.hf_lists)
    }

    override fun getItemCount(): Int = data.size
}