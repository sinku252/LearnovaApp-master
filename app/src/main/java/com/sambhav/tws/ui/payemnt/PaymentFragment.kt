package com.sambhav.tws.ui.payemnt


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sambhav.tws.base.BaseFragment
import com.sambhav.tws.databinding.FragmentDoubtBinding
import com.sambhav.tws.databinding.FragmentPaymentBinding
import com.sambhav.tws.utils.getSubjects
import kotlinx.android.synthetic.main.fragment_payment.*

class PaymentFragment : BaseFragment() {

    private lateinit var mBinding: FragmentPaymentBinding
    private var  totalAmount = 0

    companion object {
        @JvmStatic
        fun newInstance(name:String): PaymentFragment {
            return PaymentFragment().apply {
                arguments = Bundle().apply {

                }
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentPaymentBinding.inflate(inflater, container, false)
        return mBinding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val list = ArrayList<PaymentSubModel>()
        getSubjects().forEach {
            list.add(PaymentSubModel(
                name = it.subject_title,
                id = it.id))
        }
        recycler_view.adapter = PaymentSubAdapter(list,this)
    }

}
