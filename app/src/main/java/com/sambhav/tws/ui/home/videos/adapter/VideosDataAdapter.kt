package com.sambhav.tws.ui.home.videos.adapter

import android.content.Context
import android.view.ViewGroup
import com.sambhav.tws.R
import com.sambhav.tws.base.BaseBindingAdapter
import com.sambhav.tws.base.BaseCallback
import com.sambhav.tws.base.BaseViewHolder
import com.sambhav.tws.databinding.ItemDataListBinding
import com.sambhav.tws.ui.home.videos.model.CommonDataModel
import com.sambhav.tws.utils.itemdecoration.GridSpacingItemDecoration

class VideosDataAdapter(
    private val mContext: Context,
    var mList:ArrayList<CommonDataModel>,
    val mCallback: BaseCallback
) : BaseBindingAdapter() {
    var isStudent = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return ViewHolderHeader(
            ItemDataListBinding.inflate(
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

    inner class ViewHolderHeader(val binding: ItemDataListBinding) :
        BaseViewHolder(binding.root) {
        override fun bindData(position: Int) {
            binding.tvHeader.text = mList[position].type
            binding.recyclerView.addItemDecoration(
                GridSpacingItemDecoration(
                    2,
                    mContext.resources.getDimension(R.dimen.margin8dp).toInt(),
                    true
                )
            )
           /* VideosListAdapter(mContext, getVideosList(),mCallback).apply {
                isStudent = this@VideosDataAdapter.isStudent
                binding.recyclerView.adapter =this
            }*/

        }
    }

    fun updateList(List: ArrayList<CommonDataModel>) {
        mList = List
        notifyDataSetChanged()
    }

}