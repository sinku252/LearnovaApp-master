package com.sambhav.tws.ui.home.classes.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import canStartClass
import com.sambhav.tws.apiModel.LiveClassData
import com.sambhav.tws.base.BaseBindingAdapter
import com.sambhav.tws.base.BaseCallback
import com.sambhav.tws.base.BaseViewHolder
import com.sambhav.tws.databinding.ItemEventBinding
import com.sambhav.tws.databinding.ItemEventHeaderBinding
import com.sambhav.tws.databinding.ItemEventStudentBinding
import com.sambhav.tws.utils.*

class LiveClassAdapter(
    private val mContext: Context,
    var mList: ArrayList<LiveClassData>,
    val mCallback: BaseCallback? = null
) : BaseBindingAdapter() {
    var isStudent: Boolean = true
    var isStart: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        if (mList[viewType].id == "") {
            return ViewHolderHeader(
                ItemEventHeaderBinding.inflate(
                    getInflater(parent), parent,
                    false
                )
            )
        } else {
            if (isStudent) {
                return ViewHolderEventStudent(
                    ItemEventStudentBinding.inflate(
                        getInflater(parent), parent,
                        false
                    )
                )
            } else {
                return ViewHolderEventTeacher(
                    ItemEventBinding.inflate(
                        getInflater(parent), parent,
                        false
                    )
                )
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class ViewHolderEventStudent(val binding: ItemEventStudentBinding) :
        BaseViewHolder(binding.root) {
        override fun bindData(position: Int) {
            mList[position].teacherName =
                mList[position].teacher_first_name + " " + mList[position].teacher_last_name
            binding.event = mList[position]
            binding.executePendingBindings()
            binding.root.setOnClickListener(CustomClickListener {
                mCallback?.onItemClick(position, ACTION_VIEW)
            })
        }
    }

    inner class ViewHolderEventTeacher(val binding: ItemEventBinding) :
        BaseViewHolder(binding.root) {
        override fun bindData(position: Int) {
            val model = mList[position]
            model.teacherName = model.teacher_first_name + " " + model.teacher_last_name

            binding.event = mList[position]
            binding.executePendingBindings()
            val cStart = canStartClass(model.schedule_date, model.schedule_time)
            if (cStart) {
                binding.cardStart.visibility = View.VISIBLE
                binding.cardEdit.visibility = View.GONE
                binding.cardDelete.visibility = View.GONE
            } else {
                binding.cardStart.visibility = View.VISIBLE
                binding.cardEdit.visibility = View.GONE
                binding.cardDelete.visibility = View.GONE
            }

            binding.cardStart.setOnClickListener(CustomClickListener {
                mCallback?.onItemClick(position, ACTION_START)
            })

            binding.cardEdit.setOnClickListener(CustomClickListener {
                mCallback?.onItemClick(position, ACTION_EDIT)
            })

            binding.cardDelete.setOnClickListener(CustomClickListener {
                mCallback?.onItemClick(position, ACTION_DELETE)
            })
        }
    }

    inner class ViewHolderHeader(val binding: ItemEventHeaderBinding) :
        BaseViewHolder(binding.root) {
        override fun bindData(position: Int) {
            binding.tvHeader.text = mList[position].schedule_date
            binding.executePendingBindings()
        }
    }

    fun updateList(List: ArrayList<LiveClassData>) {
        mList = List
        notifyDataSetChanged()
    }
}