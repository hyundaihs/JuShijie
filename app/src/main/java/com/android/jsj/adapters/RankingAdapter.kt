package com.android.jsj.adapters

import com.android.jsj.R
import com.android.jsj.entity.MerchantInfo
import com.android.jsj.entity.getImageUrl
import com.android.shuizu.myutillibrary.adapter.MyBaseAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_merchant_ranking_list_item.view.*
import java.util.ArrayList

/**
 * ChaYin
 * Created by ${蔡雨峰} on 2019/8/14/014.
 */
class RankingAdapter(val data: ArrayList<MerchantInfo>) :
    MyBaseAdapter(R.layout.layout_merchant_ranking_list_item) {

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val merchantInfo = data[position]
        val level = if (position < 10) {
            "0${position + 1}"
        } else {
            "${position + 1}"
        }
        holder.itemView.rankingNum.text = level
        Picasso.with(holder.itemView.context).load(merchantInfo.file_url.getImageUrl())
            .into(holder.itemView.photo)
        holder.itemView.name.text = merchantInfo.title
        holder.itemView.company.text = merchantInfo.pp_title
        holder.itemView.fsNumText.text = merchantInfo.gz_nums.toString()
        holder.itemView.gzNumText.text = merchantInfo.gz_nums.toString()
        holder.itemView.hzNumText.text = merchantInfo.zan_nums.toString()
        holder.itemView.ftNumText.text = merchantInfo.ft_nums.toString()
    }

    override fun getItemCount(): Int = data.size
}