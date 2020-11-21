package com.sambhav.tws.ui.grade.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.sambhav.tws.base.BaseViewHolder
import com.sambhav.tws.databinding.ItemSpinnerChapterBinding
import com.sambhav.tws.ui.chapter.model.ChapterData

class ChapterListAdapter(
    private val mContext: Context,
    var mList: ArrayList<ChapterData>
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
        val bind: ItemSpinnerChapterBinding
        if (view == null) {
            bind = ItemSpinnerChapterBinding.inflate(
                LayoutInflater.from(viewGroup?.context),
                viewGroup, false
            )
            bind.root.tag = bind
        } else {
            bind = view.tag as ItemSpinnerChapterBinding
        }
        val viewHolder = ViewHolder(bind)
        viewHolder.bindData(postion)
        return bind.root
    }

    inner class ViewHolder(val binding: ItemSpinnerChapterBinding) : BaseViewHolder(binding.root) {
        override fun bindData(position: Int) {
            binding.model = mList[position]
            binding.executePendingBindings()
            binding.tvSub.text = mList[position].chapter_title.capitalize()
        }

    }

}