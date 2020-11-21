package com.sambhav.tws.ui.home.exam.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.sambhav.tws.base.BaseBindingAdapter
import com.sambhav.tws.base.BaseCallback
import com.sambhav.tws.base.BaseViewHolder
import com.sambhav.tws.databinding.ItemQuestionBinding
import com.sambhav.tws.ui.home.exam.model.QuestionData
import com.sambhav.tws.utils.CustomClickListener


class QuestionAdapter(
    private val mContext: Context,
    var mList: ArrayList<QuestionData>,
    val mCallback:BaseCallback?= null
) : BaseBindingAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
       /* if (mList[viewType].chapter_id =="") {
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
        }*/
        return ViewHolder(
            ItemQuestionBinding.inflate(
                getInflater(parent), parent,
                false
            )
        )
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
    override fun getItemCount(): Int {
        return mList.size
    }

    inner class ViewHolder(val binding: ItemQuestionBinding) : BaseViewHolder(binding.root) {
        override fun bindData(position: Int) {
            binding.event = mList[position]
            var qNo:Int

            if(position==0)
            {
                qNo=1
            }
            else
            {
                qNo=position+1;
            }
            binding.tvQuestionNo.text ="Q. "+qNo

            if(mList[position].option_3_text.length>0)
            {
                binding.rbOption3.visibility= View.VISIBLE
            }
            else if(mList[position].option_4_text.length>0)
            {
                binding.rbOption4.visibility= View.VISIBLE
            }
            else if(mList[position].option_5_text.length>0)
            {
                binding.rbOption5.visibility= View.VISIBLE
            }

            binding.rbOption1.setOnClickListener(CustomClickListener {
                mCallback?.onItemClick(position, binding.rbOption1.text.toString())
            })
            binding.rbOption2.setOnClickListener(CustomClickListener {
                mCallback?.onItemClick(position, binding.rbOption2.text.toString())
            })
            binding.rbOption3.setOnClickListener(CustomClickListener {
                mCallback?.onItemClick(position, binding.rbOption3.text.toString())
            })
            binding.rbOption4.setOnClickListener(CustomClickListener {
                mCallback?.onItemClick(position, binding.rbOption4.text.toString())
            })
            binding.rbOption5.setOnClickListener(CustomClickListener {
                mCallback?.onItemClick(position, binding.rbOption5.text.toString())
            })


            /*binding.rgExam.setOnCheckedChangeListener(
                RadioGroup.OnCheckedChangeListener { group, checkedId ->
                    val radio: RadioButton = group.findViewById(checkedId)

                    Toast.makeText(context," On checked change :"+
                            " ${radio.text}",
                        Toast.LENGTH_SHORT).show()
                })*/
            //binding.ra
            binding.executePendingBindings()
            /*binding.rootLayout.setOnClickListener(CustomClickListener {
                mCallback?.onItemClick(position, ACTION_START)
            })*/
        }
    }

    /*inner class ViewHolderHeader(val binding: ItemEventHeaderBinding) :
        BaseViewHolder(binding.root) {
        override fun bindData(position: Int) {
            binding.tvHeader.text = mList[position].chapter_title
            binding.executePendingBindings()

        }
    }*/



    fun updateList(List: ArrayList<QuestionData>) {

        mList = List
        notifyDataSetChanged()
    }

    fun clearResponse(position: Int,questionData: QuestionData)
    {
        //this.mList.set(position, questionData);
        notifyItemChanged(position,questionData);
    }

}