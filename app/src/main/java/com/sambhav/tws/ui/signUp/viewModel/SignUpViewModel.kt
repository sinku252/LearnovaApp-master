package com.sambhav.tws.ui.signUp.viewModel

import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.sambhav.tws.base.BaseViewModel
import com.sambhav.tws.ui.grade.model.GradeData
import com.sambhav.tws.utils.*
import com.sambhav.tws.utils.networkRequest.ApiService

class SignUpViewModel (
    private val mNetworkService : ApiService,
    private val pref : SharedPreferencesHelper
): BaseViewModel(){

    init {
        getAllGrade()
        getAllStream()
    }

    val signUp = MutableLiveData<String>()
    val allGrade = MutableLiveData<ArrayList<GradeData>>()
    val allStream = MutableLiveData<ArrayList<GradeData>>()

    private fun getAllGrade(){
        val map= JsonObject()
        map.addProperty(API_KE_SCHOOL_ID,KOTA_FACTORY_SCHOOL_ID)
        map.addProperty(API_KEY_METHOD,GET_ALL_DATA)
        requestData(mNetworkService.getAllStudentGrade(map),{
            allGrade.postValue(it.data)
        })
    }

    private fun getAllStream(){
        val map= JsonObject()
        map.addProperty(API_KE_SCHOOL_ID,KOTA_FACTORY_SCHOOL_ID)
        map.addProperty(API_KEY_METHOD,GET_ALL_DATA)
        requestData(mNetworkService.getAllStudentStream(map),{
            allStream.postValue(it.data)
        })
    }

    fun signUpStudent(map:JsonObject){
        map.addProperty(API_KE_SCHOOL_ID,KOTA_FACTORY_SCHOOL_ID)
        map.addProperty(API_KEY_METHOD,"")
        requestData(mNetworkService.studentSignUp(map),{
           signUp.postValue(it.message)
        },{ message, code ->
            mErrorMessage.postValue(message)
        })
    }

}