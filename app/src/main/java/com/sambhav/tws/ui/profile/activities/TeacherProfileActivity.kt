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
import com.sambhav.tws.databinding.ActivityTeacherProfileBinding
import com.sambhav.tws.ui.profile.activities.adapter.AssignSubjectAdapter
import com.sambhav.tws.ui.profile.activities.adapter.GenderAdapter
import com.sambhav.tws.ui.profile.activities.viewModel.ProfileViewModel
import com.sambhav.tws.utils.*
import kotlinx.android.synthetic.main.activity_login.mSubmit
import kotlinx.android.synthetic.main.activity_teacher_profile.*
import kotlinx.android.synthetic.main.layout_login_header.*
import kotlinx.android.synthetic.main.layout_spinner.*
import kotlinx.android.synthetic.main.layout_spinner.view.spinner
import org.koin.android.ext.android.get

class TeacherProfileActivity : BaseActivity() {

    private lateinit var mBinding : ActivityTeacherProfileBinding
    private var mViewModel : ProfileViewModel = get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_teacher_profile)
        mBinding.viewModel = mViewModel
        mBinding.executePendingBindings()

        init()
        getDataFromViewModel()
        handleAuth(mViewModel)

    }

    private fun init(){

        setIcon(mProfileImage,R.drawable.ic_insititute)
        tvTitle.text = "Teacher Profile"
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
        if (TextUtils.isEmpty(et_firstName.text.toString())){
            toast("fill first name")
            return false
        }
        if (TextUtils.isEmpty(et_LastName.text.toString())){
            toast("fill last name")
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
        map.addProperty(API_KEY_FIRST_NAME,et_firstName.text.toString())
        map.addProperty(API_KEY_LAST_NAME,et_LastName.text.toString())
        map.addProperty(API_KEY_EMAIL,et_email.text.toString())
        map.addProperty(API_KEY_GENDER,(spinner.adapter as GenderAdapter).mList[spinner.selectedItemPosition])

        if (!TextUtils.isEmpty(etv_password.text.toString()))
            map.addProperty(API_KEY_PASSWORD,etv_password.text.toString())

        mViewModel.updateTeacher(map)
    }

    private fun getDataFromViewModel(){
        mViewModel.teacherUpdate.observe(this, Observer {
            mPreference.teacherData = Gson().toJson(it.data)
            mBinding.teacher = it.data
            toast(it.message)
            finish()
        })
        mViewModel.teacherModel.observe(this, Observer {
            mBinding.teacher = it
            spinner.setSelection((spinner.adapter as GenderAdapter).mList.indexOf(it.gender))
            it?.techerInfo?.let{
                recycler_view.adapter = AssignSubjectAdapter(this,it)
            }
        })
    }

}
