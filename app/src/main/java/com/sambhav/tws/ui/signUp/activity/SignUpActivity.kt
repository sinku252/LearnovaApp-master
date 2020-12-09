package com.sambhav.tws.ui.signUp.activity

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.gson.JsonObject
import com.sambhav.tws.R
import com.sambhav.tws.base.BaseActivity
import com.sambhav.tws.databinding.ActivitySignUpBinding
import com.sambhav.tws.ui.signUp.adapter.StreamListAdapter
import com.sambhav.tws.ui.signUp.viewModel.SignUpViewModel
import com.sambhav.tws.utils.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.layout_login_header.view.*
import kotlinx.android.synthetic.main.layout_spinner.view.*
import org.koin.android.ext.android.get

class SignUpActivity : BaseActivity() {

    private lateinit var mBinding : ActivitySignUpBinding
    private var mViewModel : SignUpViewModel = get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_sign_up)
        mBinding.viewModel = mViewModel
        mBinding.executePendingBindings()

        observerData()
        init()
    }

    private fun init(){
        //mBinding.header.tvTitleDes.text = getString(R.string.sign_up_title_text)
        mBinding.header.tvTitleDes.text = getString(R.string.sign_up_title_text)
        mBinding.btnSubmit.setOnClickListener {
            if (checkValidation()){
                val map = JsonObject()
                map.addProperty(API_KEY_EMAIL,etv_email.text.toString().trim())
                map.addProperty(API_KEY_MOBILE,etv_mobile.text.toString().trim())
                map.addProperty(API_KEY_NAME,etv_name.text.toString().trim())
                map.addProperty(API_KEY_GRADE_ID,
                    (spinner_class.spinner.adapter as StreamListAdapter).mList[spinner_class.spinner.selectedItemPosition].id
                )
                map.addProperty(API_KEY_STREAM_ID,
                    (spinner_stream.spinner.adapter as StreamListAdapter).mList[spinner_stream.spinner.selectedItemPosition].id
                )
                mViewModel.signUpStudent(map)
            }
        }
    }

    private fun observerData() {
        mViewModel.signUp.observe(this, Observer {
            toast(it)
            finish()
        })
        mViewModel.mErrorMessage.observe(this, Observer {
            toast(it)
        })
        mViewModel.allGrade.observe(this, Observer {
            spinner_class.spinner.adapter = StreamListAdapter(this, it)
        })
        mViewModel.allStream.observe(this, Observer {
            spinner_stream.spinner.adapter = StreamListAdapter(this, it)
        })
    }

    private fun checkValidation(): Boolean{
        if (etv_name.text.toString().trim().isEmpty()){
            toast("Fill name")
            return false
        }
        if (etv_mobile.text.toString().trim().isEmpty()){
            toast("Fill mobile number")
            return false
        }
        if (etv_email.text.toString().trim().isEmpty()){
            toast("Fill email")
            return false
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(etv_email.text.toString().trim()).matches()){
            toast("Fill correct email address")
            return false
        }
        if (spinner_class.spinner.selectedItem.toString().trim().isEmpty()){
            toast("Select class")
            return false
        }
        if (spinner_stream.spinner.selectedItem.toString().trim().isEmpty()){
            toast("Select stream")
            return false
        }
        return true
    }


}
