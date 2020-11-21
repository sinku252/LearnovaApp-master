package com.sambhav.tws.ui.profile.activities.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.sambhav.tws.base.BaseCallback
import com.sambhav.tws.base.BaseViewHolder
import com.sambhav.tws.databinding.ItemChapterBinding
import com.sambhav.tws.databinding.ItemSpinnerGenderBinding
import com.sambhav.tws.databinding.ItemSpinnerSubBinding

class GenderAdapter(
    private val mContext: Context,
    var mList: Array<String>,
    val mCallback:BaseCallback?= null
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
        val bind: ItemSpinnerGenderBinding
        if (view == null) {
            bind = ItemSpinnerGenderBinding.inflate(
                LayoutInflater.from(viewGroup?.context),
                viewGroup, false
            )
            bind.root.tag = bind
        } else {
            bind = view.tag as ItemSpinnerGenderBinding
        }
        val viewHolder = ViewHolder(bind)
        viewHolder.bindData(postion)
        return bind.root
    }

    inner class ViewHolder(val binding: ItemSpinnerGenderBinding) : BaseViewHolder(binding.root) {
        override fun bindData(position: Int) {
            binding.value = mList[position]
            binding.executePendingBindings()
        }
    }
}