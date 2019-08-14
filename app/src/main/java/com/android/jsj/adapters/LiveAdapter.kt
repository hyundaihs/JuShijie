package com.android.jsj.adapters

import android.view.View
import com.android.jsj.R
import com.android.jsj.entity.LiveInfo
import com.android.jsj.entity.getImageUrl
import com.android.shuizu.myutillibrary.adapter.MyBaseAdapter
import com.android.shuizu.myutillibrary.utils.CalendarUtil
import com.android.shuizu.myutillibrary.utils.DifferType
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_live_list_item.view.*
import java.util.ArrayList

/**
 * ChaYin
 * Created by ${蔡雨峰} on 2019/8/14/014.
 */
class LiveAdapter(val data: ArrayList<LiveInfo>) : MyBaseAdapter(R.layout.layout_live_list_item) {

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val liveInfo = data[position]
        Picasso.with(holder.itemView.context).load(liveInfo.file_url.getImageUrl()).into(holder.itemView.livePhoto)
        when (liveInfo.zb_status) {
            0 -> {
                val cale = CalendarUtil(liveInfo.start_time, true)
                val hour = cale.getTimeDifferFromNow(DifferType.TYPE_HOUR)
                val min = cale.getTimeDifferFromNow(DifferType.TYPE_MIN)
                val differMin = min - hour * 60
                holder.itemView.liveStatus.text = "直播还有${hour}小时${differMin}分钟"
                holder.itemView.liveStatus.setBackgroundResource(R.drawable.bg10_rect_corner)
            }
            1 -> {
                holder.itemView.liveStatus.text = "直播中"
                holder.itemView.liveStatus.setBackgroundResource(R.drawable.bg_8_rect_corner)
            }
            else -> {
                holder.itemView.liveStatus.visibility = View.GONE
            }
        }
        holder.itemView.liveCompany.text = liveInfo.account_title
        holder.itemView.liveHot.text = "${liveInfo.view_nums}w"
        holder.itemView.liveTitle.text = liveInfo.title
    }

    override fun getItemCount(): Int = data.size
}