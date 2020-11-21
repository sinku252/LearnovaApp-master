package com.sambhav.tws.ui.home.viewModel

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.sambhav.tws.ui.subject.model.SubjectData
import com.sambhav.tws.base.BaseViewModel
import com.sambhav.tws.ui.grade.model.GradeData
import com.sambhav.tws.ui.login.model.LogoutModelResponse
import com.sambhav.tws.utils.*
import com.sambhav.tws.utils.networkRequest.ApiService

class HomeDataViewModel(
    private val mNetworkService: ApiService,
    private val pref: SharedPreferencesHelper
) : BaseViewModel() {

    init {
        if (pref.isStudent)
            getStudentSubject()
        else{
            getTeacherSubject()
            getTeacherGrade()
        }
    }

    val allSubjects = MutableLiveData<ArrayList<SubjectData>>()
    val allGrade = MutableLiveData<ArrayList<GradeData>>()
    val logout = MutableLiveData<LogoutModelResponse>()

    private fun getStudentSubject() {
        val map = JsonObject()
        map.addProperty(API_KE_SCHOOL_ID,getStudentData(pref).school_id)
        map.addProperty(API_KEY_STUDENT_ID, getStudentData(pref).id)
        map.addProperty(API_KEY_GRADE_ID,getStudentData(pref).grade_id)
        map.addProperty(API_KE_METHOD,ALL_DATA)
        requestData(mNetworkService.getAllStudentSubject(pref.token, map), {
            allSubjects.postValue(it.data)
            pref.allSubject = Gson().toJson(it.data)
        })
    }

    fun getTeacherSubject() {
        val map = JsonObject()
        map.addProperty(API_KE_TEACHER_ID, getTeacherData(pref).id)
        map.addProperty(API_KEY_GRADE_ID,pref.gradeId)
        map.addProperty(API_KE_METHOD,ALL_DATA)
        requestData(mNetworkService.getAllTeacherSubject(pref.token, map), {
            allSubjects.postValue(it.data)
            pref.allSubject = Gson().toJson(it.data)
        })
    }

    private fun getTeacherGrade() {
        val map = JsonObject()
        map.addProperty(API_KE_TEACHER_ID, getTeacherData(pref).id)
        map.addProperty(API_KE_METHOD,ALL_DATA)
        requestData(mNetworkService.getAllTeacherGrade(pref.token, map), {
            allGrade.value = it.data
            pref.allGrade = Gson().toJson(it.data)
        })
    }

    fun logout() = if (pref.isStudent) studentLogout() else teacherLogout()

    fun teacherLogout() {
        val map = JsonObject()
        map.addProperty(API_KE_TEACHER_ID, getTeacherData(pref).id)
        map.addProperty(API_KE_SCHOOL_ID,getTeacherData(pref).school_id)
        requestData(mNetworkService.teacherLogout(pref.token, map), {
            logout.value = it
        })
    }

    fun studentLogout() {
        val map = JsonObject()
        map.addProperty(API_KEY_STUDENT_ID, getStudentData(pref).id)
        map.addProperty(API_KE_SCHOOL_ID,getStudentData(pref).school_id)
        requestData(mNetworkService.studentLogout(pref.token, map), {
            logout.value = it
        })
    }

}