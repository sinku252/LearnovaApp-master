package com.sambhav.tws.ui.home.videos.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.sambhav.tws.apiModel.VideoData
import com.sambhav.tws.base.BaseBindingAdapter
import com.sambhav.tws.base.BaseCallback
import com.sambhav.tws.base.BaseViewHolder
import com.sambhav.tws.databinding.ItemEventHeaderBinding
import com.sambhav.tws.databinding.ItemVideoListBinding
import com.sambhav.tws.utils.*

class VideosListAdapter(
    private val mContext: Context,
    var mList: ArrayList<VideoData>,
    val mCallback:BaseCallback
) : BaseBindingAdapter() {

    var isStudent = true
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        if(mList[viewType].id ==0){
            return ViewHolderHeader(
                ItemEventHeaderBinding.inflate(
                    getInflater(parent), parent,
                    false
                )
            )
        }else{
            return ViewHolderEvent(
                ItemVideoListBinding.inflate(
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

    inner class ViewHolderEvent(val binding: ItemVideoListBinding) : BaseViewHolder(binding.root) {
        override fun bindData(position: Int) {
            binding.video = mList[position]
            binding.tvName.text =
                String.format("%s - %s",mList[position].teacher_first_name.capitalize(),
                    mList[position].teacher_last_name.capitalize())
            binding.layoutView.visibility = if(isStudent) View.GONE else View.VISIBLE
            val dte = mList[position].created_date
            /*mList[position].created_date = dte.changeDate()
            mList[position].created_time = dte.changeTime()*/

            binding.tvDate.text=dte.changeDate()
            binding.tvTime.text=dte.changeTime()
            binding.executePendingBindings()
            binding.root.setOnClickListener(CustomClickListener {
                mCallback.onItemClick(position, ACTION_VIEW)
            })

            binding.tvView.setOnClickListener(CustomClickListener {
                mCallback.onItemClick(position, ACTION_VIEW)
            })

            binding.tvDel.setOnClickListener(CustomClickListener {
                mCallback.onItemClick(position, ACTION_DELETE)
            })
        }
    }

    inner class ViewHolderHeader(val binding: ItemEventHeaderBinding) :
        BaseViewHolder(binding.root) {
        override fun bindData(position: Int) {
            binding.tvHeader.text = mList[position].title.capitalize()
        }
    }

    fun updateList(List: ArrayList<VideoData>) {
        mList = List
        notifyDataSetChanged()
    }

}