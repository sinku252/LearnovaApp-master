package com.sambhav.tws.ui.home.exam.adapter

import android.content.Context
import android.view.ViewGroup
import com.sambhav.tws.base.BaseBindingAdapter
import com.sambhav.tws.base.BaseCallback
import com.sambhav.tws.base.BaseViewHolder

import com.sambhav.tws.databinding.ItemEventHeaderBinding
import com.sambhav.tws.databinding.ItemExamBinding
import com.sambhav.tws.ui.home.exam.model.ExamData
import com.sambhav.tws.utils.ACTION_START
import com.sambhav.tws.utils.CustomClickListener
import getDateWithMonthName
import getScheduleTime
import getTime

class ExamListAdapter(
    private val mContext: Context,
    var mList: ArrayList<ExamData>,
    val mCallback:BaseCallback?= null
) : BaseBindingAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        if (mList[viewType].title =="") {
            return ViewHolderHeader(
                ItemEventHeaderBinding.inflate(
                    getInflater(parent), parent,
                    false
                )
            )
        } else {
            return ViewHolder(
                ItemExamBinding.inflate(
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

    inner class ViewHolder(val binding: ItemExamBinding) : BaseViewHolder(binding.root) {
        override fun bindData(position: Int) {
            binding.event = mList[position]
            binding.tvSub.text = mList[position].title.capitalize()
            binding.testDate.text=getDateWithMonthName(mList[position].test_date)

            val hours: Int = mList[position].duration / 60 //since both are ints, you get an int
            val minutes: Int = mList[position].duration % 60
            binding.testDuration.text=""+hours+" HR "+minutes+" MIN"
            binding.executePendingBindings()
            binding.rootLayout.setOnClickListener(CustomClickListener {
                mCallback?.onItemClick(position, ACTION_START)
            })
        }
    }

    inner class ViewHolderHeader(val binding: ItemEventHeaderBinding) :
        BaseViewHolder(binding.root) {
        override fun bindData(position: Int) {
            binding.tvHeader.text = mList[position].subject_title
            binding.executePendingBindings()

        }
    }


    fun updateList(List: ArrayList<ExamData>) {
        mList = List
        notifyDataSetChanged()
    }
}