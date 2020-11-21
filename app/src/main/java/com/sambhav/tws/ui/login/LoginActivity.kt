package com.sambhav.tws.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.Gson
import com.sambhav.tws.R
import com.sambhav.tws.base.BaseActivity
import com.sambhav.tws.databinding.ActivityStudentViewBinding
import com.sambhav.tws.ui.home.MainActivity
import com.sambhav.tws.ui.signUp.activity.SignUpActivity
import com.sambhav.tws.utils.SharedPreferencesHelper
import com.sambhav.tws.utils.CustomClickListener
import com.sambhav.tws.utils.isValueNotEmpty
import com.sambhav.tws.utils.setUserImageRound
import kotlinx.android.synthetic.main.activity_student_view.*
import kotlinx.android.synthetic.main.layout_login_header.*
import org.koin.android.ext.android.get

class LoginActivity : BaseActivity() {
    private lateinit var binding:ActivityStudentViewBinding
    private var isForStudent = true
    private val viewModel:  LoginDataViewModel = get()
    private val mAppPrefer: SharedPreferencesHelper = get()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_student_view)
        binding.viewModel = viewModel
        binding.executePendingBindings()
        isForStudent = intent.getBooleanExtra("FOR_STUDENT",true)
        Log.d("isForStudent","isForStudent $isForStudent")
        getFirebaseToken()
        init()
    }

    private fun init(){
        setUserImageRound(mProfileImage, R.drawable.ic_student)

        if (isForStudent){
            etv_student_id.inputType = InputType.TYPE_CLASS_TEXT
            tvTitle.text =getString(R.string.student_title_text)
            tvStudentTitle.text = getString(R.string.student_id)
            btnSignup.visibility = View.VISIBLE
        }else{
            etv_student_id.inputType = InputType.TYPE_CLASS_NUMBER
            tvTitle.text = getString(R.string.teacher_title_text)
            tvStudentTitle.text = getString(R.string.mobile)
            btnSignup.visibility = View.GONE
        }

        btnSignup.setOnClickListener(CustomClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        })

        btn_submit.setOnClickListener(CustomClickListener {
            if (!isValueNotEmpty(etv_student_id.text.toString())
                && !isValueNotEmpty(etv_password.text.toString())
            ) {
                if (isForStudent) {
                    val map = HashMap<String, String>()
                    map.put("enroll_no", etv_student_id.text.toString())
                    map.put("password", etv_password.text.toString())
                    map.put("device_id", mPreference.firebaseToken)
                    /*map.put("enroll_no","1-20-21-DAV78852")
                    map.put("password","123456")
                    map.put("enroll_no","7014569040")
                    map.put("password","624593")*/
                    viewModel.studentLogin(map)
                } else {
                    val map = HashMap<String, String>()
                    map.put("mobile", etv_student_id.text.toString())
                    map.put("password", etv_password.text.toString())
                    map.put("device_id", mPreference.firebaseToken)
                    /*map.put("mobile","7896321456")
                    map.put("password","123456")*/
                    viewModel.teacherLogin(map)
                }
            } else {
                toast("Fill Value")
            }
        })

        viewModel.mStudentModel.observe(this, Observer {
            mAppPrefer.isLogin = true
            mAppPrefer.gradeId = it.grade_id
            mAppPrefer.isStudent = true
            mAppPrefer.token = it.app_token
            mAppPrefer.studentData = Gson().toJson(it)
            startActivity(Intent(this, MainActivity::class.java)
                .putExtra("FOR_STUDENT", isForStudent))
            finishAffinity()
        })

        viewModel.mTeacherModel.observe(this, Observer {
            mAppPrefer.isLogin = true
            mAppPrefer.isStudent = false
            mAppPrefer.token = it.app_token
            mAppPrefer.teacherData = Gson().toJson(it)
            startActivity(Intent(this, MainActivity::class.java)
                .putExtra("FOR_STUDENT", isForStudent))
            finishAffinity()
        })
        viewModel.mErrorMessage.observe(this, Observer {
            toast("Invalid Details")
        })
    }
    
    private fun getFirebaseToken(){
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.e("FIREBASE_TOKEN", "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }
                // Get new Instance ID token
                mPreference.firebaseToken = task.result?.token?:""
                Log.e("FIREBASE_TOKEN", "getInstanceId : "+mPreference.firebaseToken)
            })

    }

}
