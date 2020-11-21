package com.sambhav.tws.ui.payemnt

import android.view.ViewGroup
import com.sambhav.tws.base.BaseBindingAdapter
import com.sambhav.tws.base.BaseCallback
import com.sambhav.tws.base.BaseViewHolder
import com.sambhav.tws.databinding.ItemPaymentSubBinding
import com.sambhav.tws.utils.CustomClickListener

class PaymentSubAdapter (val list:ArrayList<PaymentSubModel>,
                         var mCallback:BaseCallback): BaseBindingAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
      return ViewHolder(
            ItemPaymentSubBinding.inflate(
                getInflater(parent),parent,false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(val binding:ItemPaymentSubBinding):BaseViewHolder(binding.root) {
        override fun bindData(position: Int) {
            binding.payment = list[position]
            binding.executePendingBindings()

            binding.root.setOnClickListener(CustomClickListener {
                binding.appCompatCheckBox.isChecked = !binding.appCompatCheckBox.isChecked
            })

            binding.appCompatCheckBox.setOnCheckedChangeListener { compoundButton, b ->
                list[position].selected = b
                mCallback.onItemClick(position,"")
            }
        }
    }
}
