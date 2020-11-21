package com.sambhav.tws.ui.chapter.adapter

import android.content.Context
import android.view.ViewGroup
import com.sambhav.tws.base.BaseBindingAdapter
import com.sambhav.tws.base.BaseCallback
import com.sambhav.tws.base.BaseViewHolder
import com.sambhav.tws.databinding.ItemChapterBinding
import com.sambhav.tws.databinding.ItemEventHeaderBinding
import com.sambhav.tws.ui.chapter.model.ChapterData
import com.sambhav.tws.utils.ACTION_START
import com.sambhav.tws.utils.CustomClickListener

class ChapterAdapter(
    private val mContext: Context,
    var mList: ArrayList<ChapterData>,
    val mCallback:BaseCallback?= null
) : BaseBindingAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        if (mList[viewType].chapter_id =="") {
            return ViewHolderHeader(
                ItemEventHeaderBinding.inflate(
                    getInflater(parent), parent,
                    false
                )
            )
        } else {
            return ViewHolder(
                ItemChapterBinding.inflate(
                    getInflater(parent), parent,
                    false
                )
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
    override fun getItemCount(): Int {
        return mList.size
    }

    inner class ViewHolder(val binding: ItemChapterBinding) : BaseViewHolder(binding.root) {
        override fun bindData(position: Int) {
            binding.event = mList[position]
            binding.tvSub.text = mList[position].chapter_title.capitalize()
            binding.executePendingBindings()
            binding.rootLayout.setOnClickListener(CustomClickListener {
                mCallback?.onItemClick(position, ACTION_START)
            })
        }
    }

    inner class ViewHolderHeader(val binding: ItemEventHeaderBinding) :
        BaseViewHolder(binding.root) {
        override fun bindData(position: Int) {
            binding.tvHeader.text = mList[position].chapter_title
            binding.executePendingBindings()

        }
    }


    fun updateList(List: ArrayList<ChapterData>) {
        mList = List
        notifyDataSetChanged()
    }
}