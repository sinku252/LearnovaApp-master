package com.sambhav.tws.ui.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.sambhav.tws.base.BaseViewModel
import com.sambhav.tws.ui.login.model.StudentModel
import com.sambhav.tws.ui.login.model.TeacherModel
import com.sambhav.tws.utils.networkRequest.ApiService

class LoginDataViewModel constructor(val mNetworkService: ApiService) : BaseViewModel() {

    val mStudentModel = MutableLiveData<StudentModel>()
    val mTeacherModel = MutableLiveData<TeacherModel>()

    fun studentLogin(map: Map<String,String>){
        requestData(mNetworkService.getStudentLogin(map),{
            mStudentModel.value = it.data
        },{message,code->
            Log.e("ADASDASDASD","YESAAAQQWWE")
            mErrorMessage.value = message
        })
    }

    fun teacherLogin(map: Map<String,String>){
        requestData(mNetworkService.getTeacherLogin(map),{
            mTeacherModel.value = it.data
        },{message,code->
            Log.e("ADASDASDASD","YESAAAQQ")
            mErrorMessage.value = message
        })
    }

}