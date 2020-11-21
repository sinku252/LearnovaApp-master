package com.sambhav.tws.ui.payemnt

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.sambhav.tws.R
import com.sambhav.tws.base.BaseActivity
import com.sambhav.tws.databinding.ActivityPaymentBinding
import com.sambhav.tws.ui.grade.adapter.GradeListAdapter
import com.sambhav.tws.ui.grade.model.GradeData
import com.sambhav.tws.ui.payemnt.viewModel.PaymentViewModel
import com.sambhav.tws.utils.*
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import kotlinx.android.synthetic.main.activity_payment.*
import kotlinx.android.synthetic.main.fragment_payment.*
import kotlinx.android.synthetic.main.layout_spinner.*
import org.json.JSONObject
import org.koin.android.ext.android.get

class PaymentActivity : BaseActivity(), PaymentResultListener {

    private lateinit var binding:ActivityPaymentBinding
    private var mViewModel : PaymentViewModel = get()
    private var mTotal = 0L
    private var subList = ArrayList<PaymentSubModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Checkout().setKeyID("EyT0wQ7G8e3xd6")

        binding = DataBindingUtil.setContentView(this,R.layout.activity_payment)
        binding.viewModel = mViewModel
        binding.executePendingBindings()

        handleAuth(mViewModel)

        observerData()

        tv_total.text = mTotal.toString()
        setTopHeader(0)

        getAllSub().forEach {
            subList.add(PaymentSubModel(name = it.subject_title,
                id = it.id))
        }

        recycler_view.adapter = PaymentSubAdapter(subList,this)
        val mList= ArrayList<GradeData>().apply {
            add(
                GradeData(
                    grade_title = getStudentData(mPreference).stream_title
                )
            )
        }
        spinner.adapter = GradeListAdapter(this, mList)
        radioClass.text = getStudentData(mPreference).grade_title
        radioClass.isChecked = true

        back.setOnClickListener {
            onBackPressed()
        }

        mSubmit.setOnClickListener {
            val list = subList.filter { it.selected }
            if (list.isNotEmpty()){
                startPayment()
            }else{
                toast("Please select subject first.")
            }

        }
    }

    private fun observerData() {
        mViewModel.studentUpdate.observe(this, Observer {
            toast("Payment Status updated")
            finish()
        })
    }

    override fun onItemClick(position: Int, action: String) {
        val list = subList.filter { it.selected }
        mTotal = 0
        /*list.forEach {
            mTotal += it.price.toInt()
        }*/
        setTopHeader(list.size)
        setPrice(list.size)
    }

    private fun setPrice(size:Int){
        val subjectList = getAllSub()
        if (subjectList.size > 0){
            try {
                mTotal = when(size){
                    1 -> subjectList[0].subject_prices?.get(0)
                    2 -> subjectList[0].subject_prices?.get(1)
                    3 -> subjectList[0].subject_prices?.get(2)
                    else -> 0
                } as Long
            }catch (e:java.lang.Exception){
                Log.e("ASDASDASDASDSAD","e : ${e.message}")
            }
            tv_total.text = mTotal.toString()
        }
    }

    private fun setTopHeader(size:Int) {
        Log.d("setTopHeader","setTopHeader "+size)
        when(size){
            1->{
                setBack(tv_single,R.drawable.tab_background_selected,R.color.color_white)
                setBack(tv_custom,R.drawable.tab_background,R.color.colorTextBlack)
                setBack(tv_complete,R.drawable.tab_background,R.color.colorTextBlack)
            }
            2 ->{
                setBack(tv_single,R.drawable.tab_background,R.color.colorTextBlack)
                setBack(tv_custom,R.drawable.tab_background_selected,R.color.color_white)
                setBack(tv_complete,R.drawable.tab_background,R.color.colorTextBlack)
            }
            3->{
                setBack(tv_single,R.drawable.tab_background,R.color.colorTextBlack)
                setBack(tv_custom,R.drawable.tab_background,R.color.colorTextBlack)
                setBack(tv_complete,R.drawable.tab_background_selected,R.color.color_white)
            }

            else ->{
                setBack(tv_single,R.drawable.tab_background,R.color.colorTextBlack)
                setBack(tv_custom,R.drawable.tab_background,R.color.colorTextBlack)
                setBack(tv_complete,R.drawable.tab_background,R.color.colorTextBlack)
            }
        }
    }

    private fun setBack(view:TextView, back:Int, color:Int){
        view.setBackgroundResource(back)
        view.setTextColor(ContextCompat.getColor(this,color))

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
            options.put("image",R.mipmap.ic_launcher)
            options.put("currency","INR")
            options.put("amount",(mTotal*100).toString())

            val prefill = JSONObject()
            prefill.put("email", getStudentData(mPreference).email)
            prefill.put("contact",getStudentData(mPreference).mobile)
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
        val list = subList.filter { it.selected }
        list.forEach {
            subjectArray.add( JsonObject().apply { addProperty(API_KEY_SUBJECT_ID, it.id) })
        }
        val map= JsonObject()
        map.add(API_KEY_SUBJECT_ARRAY,subjectArray)
        map.addProperty(API_KEY_TRANSACTION_ID,p0.toString())
        mViewModel.updatePaymentStatus(map)
    }

}
