package com.sambhav.tws.ui.payemnt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import com.sambhav.tws.R
import com.sambhav.tws.base.BaseActivity
import com.sambhav.tws.databinding.ActivityBasePaymentBinding
import com.sambhav.tws.databinding.ActivityExamResultBinding
import com.sambhav.tws.databinding.ActivityStartExamBinding
import com.sambhav.tws.ui.home.exam.model.ExamData
import com.sambhav.tws.ui.home.exam.viewModel.ExamViewModel
import com.sambhav.tws.ui.payemnt.viewModel.PaymentViewModel
import com.sambhav.tws.utils.*
import kotlinx.android.synthetic.main.activity_chapter.*
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.viewModel

class BasePaymentActivity :  BaseActivity() , PaymentResultListener {
    private val mViewModel: PaymentViewModel by viewModel()
    private lateinit var mBinding: ActivityBasePaymentBinding
    private var subList = ArrayList<PaymentSubModel>()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        Checkout().setKeyID("CsfQfz1leFnKja")

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_base_payment)
        mBinding.viewModel = mViewModel
        mBinding.executePendingBindings()


        mViewModel.getGradeData()
        fetchFromViewModel()

        mBinding.back.setOnClickListener(CustomClickListener {
            onBackPressed()
        })

        mBinding.llStartPayment.setOnClickListener(CustomClickListener {
            startPayment()
        })


    }


    private fun startPayment() {
        /*
        *  You need to pass current activity in order to let Razorpay create CheckoutActivity
        * */
        val co = Checkout()

        try {
            val options = JSONObject()
            options.put("name",getString(R.string.app_name))
            options.put("description","Demoing Charges")
            //You can omit the image option to fetch the image from dashboard
            options.put("image",R.drawable.logo)
            options.put("currency","INR")
            //options.put("amount",(mTotal*100).toString())
            options.put("amount",(mBinding.gradeModel!!.price.toInt()*100).toString())

            val prefill = JSONObject()
            prefill.put("email", getStudentData(mPreference).email)
            prefill.put("contact", getStudentData(mPreference).mobile)
            options.put("prefill",prefill)
            co.open(this,options)
        }catch (e: Exception){
            Toast.makeText(this,"Error in payment: "+ e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }


    override fun onPaymentError(p0: Int, p1: String?) {
        Log.e("PAYMENT_ERROR_LOG",p0.toString()+"    "+p1.toString())
        p1?.let {
            Toast.makeText(this,p1, Toast.LENGTH_LONG).show()
        }
    }

    override fun onPaymentSuccess(p0: String?) {
        Log.e("PAYMENT_LOG",p0.toString())
        val subjectArray= JsonArray()
        /*val list = subList.filter { it.selected }
        list.forEach {
            subjectArray.add( JsonObject().apply { addProperty(API_KEY_SUBJECT_ID, it.id) })
        }*/
        val map= JsonObject()
        map.add(API_KEY_SUBJECT_ARRAY,subjectArray)
        map.addProperty(API_KEY_TRANSACTION_ID,p0.toString())
        mViewModel.updatePaymentStatus(map)
    }

    fun fetchFromViewModel(){
        mViewModel.gradeData.observe(this, Observer {
            mBinding.gradeModel= it.data
        })

    }
}