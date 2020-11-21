package com.sambhav.tws.ui.home.doubt.adapter

import android.content.Context
import android.view.ViewGroup
import com.sambhav.tws.base.BaseBindingAdapter
import com.sambhav.tws.base.BaseCallback
import com.sambhav.tws.base.BaseViewHolder
import com.sambhav.tws.databinding.ItemDoubtStudentListBinding
import com.sambhav.tws.ui.home.doubt.model.DoubtRoomModel
import com.sambhav.tws.utils.ACTION_VIEW
import com.sambhav.tws.utils.CustomClickListener

class DoubtRoomAdapter(
    private val mContext : Context,
    var mList : ArrayList<DoubtRoomModel>,
    val mCallback: BaseCallback
) :  BaseBindingAdapter(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding =
            ViewHolderMenu(
                ItemDoubtStudentListBinding.inflate(
                    getInflater(parent), parent,
                    false
                )
            )
        return binding
    }

    inner class ViewHolderMenu(val binding : ItemDoubtStudentListBinding) : BaseViewHolder(binding.root){
        override fun bindData(position: Int) {
            binding.model = mList[position]
            binding.executePendingBindings()
            binding.root.setOnClickListener(CustomClickListener {
                mCallback.onItemClick(position, ACTION_VIEW)
            })
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun updateList(List : ArrayList<DoubtRoomModel>){
        mList = List
        notifyDataSetChanged()
    }
}