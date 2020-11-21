package com.sambhav.tws.ui.subject.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.sambhav.tws.base.BaseViewHolder
import com.sambhav.tws.databinding.ItemSpinnerSubBinding
import com.sambhav.tws.ui.subject.model.SubjectData
import kotlin.collections.ArrayList

class SubListAdapter(
    private val mContext: Context,
    var mList: ArrayList<SubjectData>
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
        val bind: ItemSpinnerSubBinding
        if (view == null) {
            bind = ItemSpinnerSubBinding.inflate(
                LayoutInflater.from(viewGroup?.context),
                viewGroup, false
            )
            bind.root.tag = bind
        } else {
            bind = view.tag as ItemSpinnerSubBinding
        }
        val viewHolder = ViewHolder(bind)
        viewHolder.bindData(postion)
        return bind.root
    }

    inner class ViewHolder(val binding: ItemSpinnerSubBinding) : BaseViewHolder(binding.root) {
        override fun bindData(position: Int) {
            binding.model = mList[position]
            binding.executePendingBindings()
        }
    }

}