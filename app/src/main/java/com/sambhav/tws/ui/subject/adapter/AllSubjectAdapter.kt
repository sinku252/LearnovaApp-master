package com.sambhav.tws.ui.subject.adapter

import android.content.Context
import android.view.ViewGroup
import com.sambhav.tws.ui.subject.model.SubjectData
import com.sambhav.tws.base.BaseBindingAdapter
import com.sambhav.tws.base.BaseCallback
import com.sambhav.tws.base.BaseViewHolder
import com.sambhav.tws.databinding.ItemAllSubBinding
import com.sambhav.tws.utils.ACTION_VIEW
import com.sambhav.tws.utils.CustomClickListener

class AllSubjectAdapter(
    private val mContext: Context,
    var mList: ArrayList<SubjectData>,
    val mCallback:BaseCallback
) : BaseBindingAdapter() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding =
            ViewHolderEvent(
                ItemAllSubBinding.inflate(
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

    inner class ViewHolderEvent(val binding: ItemAllSubBinding) : BaseViewHolder(binding.root) {
        override fun bindData(position: Int) {
            binding.video = mList[position]
            binding.executePendingBindings()
            binding.root.setOnClickListener(CustomClickListener {
                mCallback.onItemClick(position, ACTION_VIEW)
            })
            binding.tvSub.text = mList[position].subject_title.capitalize()
        }
    }

    fun updateList(List: ArrayList<SubjectData>) {
        mList = List
        notifyDataSetChanged()
    }
}