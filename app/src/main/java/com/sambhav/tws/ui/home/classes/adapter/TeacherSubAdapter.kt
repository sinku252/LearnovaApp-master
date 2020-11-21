package com.sambhav.tws.ui.home.classes.adapter

import android.content.Context
import android.view.ViewGroup
import com.sambhav.tws.base.BaseBindingAdapter
import com.sambhav.tws.base.BaseCallback
import com.sambhav.tws.base.BaseViewHolder
import com.sambhav.tws.databinding.ItemTeacherLiveSubBinding
import com.sambhav.tws.ui.home.classes.model.TeacherMenuModel
import com.sambhav.tws.utils.ACTION_ADD
import com.sambhav.tws.utils.ACTION_START
import com.sambhav.tws.utils.ACTION_VIEW
import com.sambhav.tws.utils.CustomClickListener

class TeacherSubAdapter(
    private val mContext: Context,
    var mList: ArrayList<TeacherMenuModel>,
    val mCallback: BaseCallback
) : BaseBindingAdapter() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding =
            ViewHolderEvent(
                ItemTeacherLiveSubBinding.inflate(
                    getInflater(parent), parent,
                    false
                )
            )
        return binding
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class ViewHolderEvent(val binding: ItemTeacherLiveSubBinding) :
        BaseViewHolder(binding.root) {
        override fun bindData(position: Int) {
            binding.event = mList[position]
            binding.executePendingBindings()
            binding.root.setOnClickListener(CustomClickListener {
                when (position) {
                    0 -> mCallback.onItemClick(position, ACTION_ADD)
                    1 -> mCallback.onItemClick(position, ACTION_START)
                    2 -> mCallback.onItemClick(position, ACTION_VIEW)
                }
            })
        }
    }

    fun updateList(List: ArrayList<TeacherMenuModel>) {
        mList = List
        notifyDataSetChanged()
    }

    interface Callback {
        abstract fun onItemClick(position: Int)
    }
}