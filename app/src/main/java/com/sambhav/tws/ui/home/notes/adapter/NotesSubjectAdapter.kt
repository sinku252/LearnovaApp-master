package com.sambhav.tws.ui.home.notes.adapter

import android.content.Context
import android.view.ViewGroup
import com.sambhav.tws.base.BaseBindingAdapter
import com.sambhav.tws.base.BaseCallback
import com.sambhav.tws.base.BaseViewHolder
import com.sambhav.tws.databinding.ItemNotesSubBinding
import com.sambhav.tws.ui.home.notes.model.NotesSubModel
import com.sambhav.tws.utils.ACTION_VIEW
import com.sambhav.tws.utils.CustomClickListener

class NotesSubjectAdapter(
    private val mContext: Context,
    var mList: ArrayList<NotesSubModel>,
    val mCallback:BaseCallback
) : BaseBindingAdapter() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding =
            ViewHolderEvent(
                ItemNotesSubBinding.inflate(
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

    inner class ViewHolderEvent(val binding: ItemNotesSubBinding) : BaseViewHolder(binding.root) {
        override fun bindData(position: Int) {
            binding.event = mList[position]
            binding.executePendingBindings()
            binding.root.setOnClickListener(CustomClickListener {
                mCallback.onItemClick(position, ACTION_VIEW)
            })
        }
    }

    fun updateList(List: ArrayList<NotesSubModel>) {
        mList = List
        notifyDataSetChanged()
    }
}