package com.sambhav.tws.ui.profile.activities

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.sambhav.tws.R
import com.sambhav.tws.base.BaseActivity
import com.sambhav.tws.databinding.ActivityStudentProfileBinding
import com.sambhav.tws.ui.profile.activities.adapter.GenderAdapter
import com.sambhav.tws.ui.profile.activities.viewModel.ProfileViewModel
import com.sambhav.tws.utils.*
import kotlinx.android.synthetic.main.activity_student_profile.*
import kotlinx.android.synthetic.main.layout_login_header.*
import kotlinx.android.synthetic.main.layout_spinner.*
import kotlinx.android.synthetic.main.layout_spinner.view.*
import org.koin.android.ext.android.get

class StudentProfileActivity : BaseActivity() {

    private lateinit var mBinding : ActivityStudentProfileBinding
    private var mViewModel : ProfileViewModel = get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_student_profile)
        mBinding.viewModel = mViewModel
        mBinding.executePendingBindings()

        init()
        getDataFromViewModel()
        handleAuth(mViewModel)

    }


    private fun init(){

        setIcon(mProfileImage,R.drawable.ic_student)
        tvTitle.text = "Student Profile"
        tvTitleDes.text = ""

        val genderList = resources.getStringArray(R.array.gender)
        gender.spinner.adapter = GenderAdapter(applicationContext,genderList)
        gender.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
            }
        }
        back.setOnClickListener(CustomClickListener {
            onBackPressed()
        })
        mSubmit.setOnClickListener(CustomClickListener {
            if (checkValidation()) {
                updateProfile()
            }
        })
    }

    private fun checkValidation():Boolean{
        if (TextUtils.isEmpty(et_name.text.toString())){
            toast("fill name")
            return false
        }
        if (TextUtils.isEmpty(et_email.text.toString())){
            toast("fill email")
            return false
        }
        if (!et_email.text.toString().isValidEmail()){
            toast("fill correct email")
            return false
        }
        return true
    }

    private fun updateProfile(){
        val map= JsonObject()
        map.addProperty(API_KEY_NAME,et_name.text.toString())
        map.addProperty(API_KEY_EMAIL,et_email.text.toString())
        map.addProperty(API_KEY_GENDER,(spinner.adapter as GenderAdapter).mList[spinner.selectedItemPosition])

        if (!TextUtils.isEmpty(etv_password.text.toString()))
            map.addProperty(API_KEY_PASSWORD,etv_password.text.toString())

        mViewModel.updateStudent(map)
    }

    private fun getDataFromViewModel(){
        mViewModel.studentUpdate.observe(this, Observer {
            mPreference.studentData = Gson().toJson(it.data)
            toast(it.message)
            finish()
        })
        mViewModel.studentModel.observe(this, Observer {
            mBinding.student = it
            spinner.setSelection((spinner.adapter as GenderAdapter).mList.indexOf(it.gender))
        })
    }

}
