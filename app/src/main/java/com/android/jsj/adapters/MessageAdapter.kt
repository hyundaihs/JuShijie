package com.android.jsj.adapters

import com.android.jsj.R
import com.android.jsj.entity.MessageInfo
import com.android.shuizu.myutillibrary.adapter.MyBaseAdapter
import com.android.shuizu.myutillibrary.utils.CalendarUtil
import kotlinx.android.synthetic.main.layout_message_list_item.view.*
import java.util.ArrayList

/**
 * ChaYin
 * Created by ${蔡雨峰} on 2019/8/14/014.
 */
class MessageAdapter(val data: ArrayList<MessageInfo>) :
    MyBaseAdapter(R.layout.layout_message_list_item) {

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val messageInfo = data[position]
        holder.itemView.isRead.isChecked = messageInfo.is_read == 0
        holder.itemView.msgFrom.text = messageInfo.from_title
        holder.itemView.msgType.text = messageInfo.type_str
        val c = CalendarUtil(messageInfo.create_time, true)
        holder.itemView.createTime.text = c.format(CalendarUtil.MM_DD_HH_MM)
    }

    override fun getItemCount(): Int = data.size
}