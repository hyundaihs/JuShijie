package com.android.jsj.adapters

import com.android.jsj.R
import com.android.jsj.entity.Reviews
import com.android.shuizu.myutillibrary.adapter.MyBaseAdapter
import kotlinx.android.synthetic.main.layout_reply_list_item.view.*
import java.util.*

/**
 * JuShijie
 * Created by 蔡雨峰 on 2019/8/15.
 */
class ReplyAdapter(val data: ArrayList<Reviews>) : MyBaseAdapter(R.layout.layout_reply_list_item) {

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val reviews = data[position]
        holder.itemView.replyer.text = reviews.hf_account_title
        holder.itemView.sender.text = reviews.account_title
        holder.itemView.content.text = reviews.title
    }

    override fun getItemCount(): Int = data.size
}