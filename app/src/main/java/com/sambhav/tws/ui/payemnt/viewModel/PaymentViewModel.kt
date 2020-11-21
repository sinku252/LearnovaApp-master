package com.sambhav.tws.ui.payemnt.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.sambhav.tws.base.BaseViewModel
import com.sambhav.tws.ui.home.exam.model.TestResultData
import com.sambhav.tws.ui.login.model.StudentModelResponse
import com.sambhav.tws.ui.payemnt.model.GradeData
import com.sambhav.tws.utils.*
import com.sambhav.tws.utils.networkRequest.ApiService

class PaymentViewModel (

    private val mNetworkService: ApiService,
    private val pref: SharedPreferencesHelper
) : BaseViewModel(){

    val studentUpdate = MutableLiveData<StudentModelResponse>()
    val gradeData =MutableLiveData<GradeData>()

    fun updatePaymentStatus(map: JsonObject){
        map.addProperty(API_KEY_STUDENT_ID, getStudentData(pref).id)
        map.addProperty(API_KEY_GRADE_ID, getStudentData(pref).grade_id)
        map.addProperty(API_KE_SCHOOL_ID, getStudentData(pref).school_id)
        map.addProperty(API_KE_METHOD, "UpdatePaymentStatus")
        Log.e("SADASDASDASDASD",map.toString())
        requestData(mNetworkService.updateStudentPayment(pref.token,map),{
            pref.studentData = Gson().toJson(it.data)
            studentUpdate.value= it
        })
    }

    fun getGradeData(){
        val map= JsonObject()
        map.addProperty(API_KEY_STUDENT_ID, getStudentData(pref).id)
        map.addProperty(API_KEY_GRADE_ID, getStudentData(pref).grade_id)
        map.addProperty(API_KE_SCHOOL_ID, getStudentData(pref).school_id)
        map.addProperty(API_KE_METHOD, "getGradeData")
        requestData(mNetworkService.getGradeData(pref.token,map),{
            gradeData.postValue(it)
            //pref.studentData = Gson().toJson(it.data)
            //studentUpdate.value= it
        })
    }

}