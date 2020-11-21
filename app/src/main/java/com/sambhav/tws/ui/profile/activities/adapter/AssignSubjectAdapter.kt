package com.sambhav.tws.ui.profile.activities.adapter

import android.content.Context
import android.view.ViewGroup
import com.sambhav.tws.base.BaseBindingAdapter
import com.sambhav.tws.base.BaseCallback
import com.sambhav.tws.base.BaseViewHolder
import com.sambhav.tws.databinding.ItemAssignSubBinding
import com.sambhav.tws.ui.login.model.TeacherInfo

class AssignSubjectAdapter(
    private val mContext: Context,
    var mList: ArrayList<TeacherInfo>,
    val mCallback:BaseCallback?= null
) : BaseBindingAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return ViewHolder(
            ItemAssignSubBinding.inflate(
                getInflater(parent), parent,
                false
            )
        )
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
    override fun getItemCount(): Int {
        return mList.size
    }

    inner class ViewHolder(val binding: ItemAssignSubBinding) : BaseViewHolder(binding.root) {
        override fun bindData(position: Int) {
            binding.model = mList[position]
            binding.executePendingBindings()
        }
    }

    fun updateList(List: ArrayList<TeacherInfo>) {
        mList = List
        notifyDataSetChanged()
    }
}