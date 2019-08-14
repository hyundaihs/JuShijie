package com.android.jsj.adapters

import com.android.jsj.JSJApplication.Companion.chooseType
import com.android.jsj.R
import com.android.jsj.entity.CompanyInfo
import com.android.jsj.entity.getImageUrl
import com.android.shuizu.myutillibrary.adapter.MyBaseAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_designer_list_item.view.*
import java.util.ArrayList

/**
 * ChaYin
 * Created by ${蔡雨峰} on 2019/8/14/014.
 */
class DesignerAdapter(val data: ArrayList<CompanyInfo>) : MyBaseAdapter(R.layout.layout_designer_list_item) {

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val companyInfo = data[position]
        Picasso.with(holder.itemView.context).load(companyInfo.file_url.getImageUrl()).resize(200, 250)
            .into(holder.itemView.image)
        holder.itemView.workNum.text = companyInfo.al_num.toString()
        holder.itemView.name.text = companyInfo.title
        val chooseTypes = chooseType["10"]
        if (null != chooseTypes) {
            for (i in 0 until chooseTypes.size) {
                if (chooseTypes[i].id == companyInfo.sjsdj_id)
                    holder.itemView.work.text = chooseTypes[i].title
            }
        }
    }

    override fun getItemCount(): Int = data.size
}