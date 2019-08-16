package com.android.jsj.adapters

import android.graphics.Paint
import com.android.jsj.R
import com.android.jsj.entity.CompanyInfo
import com.android.jsj.entity.getImageUrl
import com.android.shuizu.myutillibrary.adapter.MyBaseAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_sale_list_item.view.*
import java.util.ArrayList

/**
 * ChaYin
 * Created by ${蔡雨峰} on 2019/8/14/014.
 */
class SaleAdapter(val data: ArrayList<CompanyInfo>) : MyBaseAdapter(R.layout.layout_sale_list_item) {

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val companyInfo = data[position]
        Picasso.with(holder.itemView.context).load(companyInfo.file_url.getImageUrl()).resize(200, 180)
            .into(holder.itemView.logo)
        holder.itemView.name.text = companyInfo.title
        holder.itemView.price.text = "原价：￥${companyInfo.price}"
        holder.itemView.price.paint.flags = Paint. STRIKE_THRU_TEXT_FLAG
        holder.itemView.price2.text = "现价：￥${companyInfo.price2}"
    }

    override fun getItemCount(): Int = data.size
}