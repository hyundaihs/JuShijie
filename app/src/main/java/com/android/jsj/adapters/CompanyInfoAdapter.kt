package com.android.jsj.adapters

import com.android.jsj.R
import com.android.jsj.entity.CompanyInfo
import com.android.jsj.entity.MerchantInfo
import com.android.jsj.entity.getImageUrl
import com.android.shuizu.myutillibrary.adapter.MyBaseAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_merchant_list_item.view.*
import java.util.ArrayList

/**
 * ChaYin
 * Created by ${蔡雨峰} on 2019/8/17/017.
 */
class CompanyInfoAdapter(val data: ArrayList<CompanyInfo>) :
    MyBaseAdapter(R.layout.layout_merchant_list_item) {

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val merchantInfo = data[position]
        Picasso.with(holder.itemView.context).load(merchantInfo.file_url.getImageUrl())
            .resize(200, 200)
            .into(holder.itemView.merchantLogo)
        holder.itemView.merchantName.text = merchantInfo.title
        holder.itemView.merchantCompany.text = merchantInfo.pp_title
        val tags = merchantInfo.tags.split(",")
        if (tags.isNotEmpty()) {
            holder.itemView.merchantTag1.text = tags[0]
        }
        if (tags.size >= 2) {
            holder.itemView.merchantTag2.text = tags[1]
        }
    }

    override fun getItemCount(): Int = data.size
}