package com.sambhav.tws.ui.grade.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.sambhav.tws.base.BaseViewHolder
import com.sambhav.tws.databinding.ItemHomeSubBinding
import com.sambhav.tws.databinding.ItemSpinnerGradeBinding
import com.sambhav.tws.databinding.ItemSpinnerSubBinding
import com.sambhav.tws.ui.grade.model.GradeData

class GradeListAdapter(
    private val mContext: Context,
    var mList: ArrayList<GradeData>
) : BaseAdapter() {
    override fun getItem(p0: Int): Any {
        return mList[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return mList.size
    }

    override fun getView(postion: Int, view: View?, viewGroup: ViewGroup?): View {
        val bind: ItemSpinnerGradeBinding
        if (view == null) {
            bind = ItemSpinnerGradeBinding.inflate(
                LayoutInflater.from(viewGroup?.context),
                viewGroup, false
            )
            bind.root.tag = bind
        } else {
            bind = view.tag as ItemSpinnerGradeBinding
        }
        val viewHolder = ViewHolder(bind)
        viewHolder.bindData(postion)
        return bind.root
    }

    inner class ViewHolder(val binding: ItemSpinnerGradeBinding) : BaseViewHolder(binding.root) {
        override fun bindData(position: Int) {
            binding.model = mList[position]
            binding.executePendingBindings()
            binding.tvSub.text = mList[position].grade_title.capitalize()
        }

    }

}