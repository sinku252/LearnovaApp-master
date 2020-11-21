package com.sambhav.tws.ui.home.doubt.viewModel

import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.sambhav.tws.apiModel.DoubtData
import com.sambhav.tws.base.BaseViewModel
import com.sambhav.tws.ui.home.doubt.model.DoubtRoomModel
import com.sambhav.tws.utils.*
import com.sambhav.tws.utils.networkRequest.ApiService
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class DoubtViewModel(
    private val mNetworkService: ApiService,
    private val pref: SharedPreferencesHelper
) : BaseViewModel() {
    val uploadFileUrl = MutableLiveData<String>()
    val allDoubts = MutableLiveData<ArrayList<DoubtData>>()
    val allDoubtRooms = MutableLiveData<ArrayList<DoubtRoomModel>>()
    val doubt = MutableLiveData<DoubtData>()

    fun getAllDoubts(subjectId:String ,roomId:String, page:String,docId:String="") {
        val map = JsonObject()
        if(pref.isStudent){
            map.addProperty(API_KE_SCHOOL_ID,getStudentData(pref).school_id)
            map.addProperty(API_KEY_STUDENT_ID, getStudentData(pref).id)
            map.addProperty(API_KEY_SUBJECT_ID,subjectId)
            map.addProperty(API_KEY_LIBRARY_ID,docId)
            map.addProperty(API_KEY_GRADE_ID,getStudentData(pref).grade_id)
            map.addProperty(API_KEY_PAGE,page)
            map.addProperty(API_KEY_METHOD,GET_ALL_DATA)
            requestData(mNetworkService.getAllDoubts(pref.token, map),{
                allDoubts.postValue(it.data)
            })
        }else{
            map.addProperty(API_KE_SCHOOL_ID, getTeacherData(pref).school_id)
            map.addProperty(API_KEY_SUBJECT_ID,subjectId)
            map.addProperty(API_KEY_LIBRARY_ID,docId)
            map.addProperty(API_KEY_GRADE_ID,pref.gradeId)
            map.addProperty(API_KEY_METHOD,SINGLE_DATA)
            map.addProperty(API_KEY_PAGE,page)
            map.addProperty(API_KEY_STUDENT_ID, roomId)
            requestData(mNetworkService.getDoubtsRoomDetails(pref.token, map),{
                allDoubts.postValue(it.data)
            })
        }
    }

    fun getAllDocDoubts(subjectId:String ,docId:String) {
        val map = JsonObject()
        if(pref.isStudent){
            map.addProperty(API_KE_SCHOOL_ID,getStudentData(pref).school_id)
            map.addProperty(API_KEY_STUDENT_ID, getStudentData(pref).id)
        }else{
            map.addProperty(API_KE_SCHOOL_ID, getTeacherData(pref).school_id)
            map.addProperty(API_KEY_STUDENT_ID, "")
        }

        map.addProperty(API_KEY_SUBJECT_ID,subjectId)
        map.addProperty(API_KEY_LIBRARY_ID,docId)
        map.addProperty(API_KEY_GRADE_ID,pref.gradeId)
        map.addProperty(API_KEY_PAGE,"1")
        map.addProperty(API_KEY_METHOD,GET_ALL_DATA)
        requestData(mNetworkService.getAllDoubts(pref.token, map), {
            allDoubts.postValue(it.data)
        })
    }


    fun getAllDoubtRooms(subjectId:String) {
        val map = JsonObject()
        map.addProperty(API_KE_SCHOOL_ID,getTeacherData(pref).school_id)
        map.addProperty(API_KE_TEACHER_ID, getTeacherData(pref).id)
        map.addProperty(API_KEY_SUBJECT_ID,subjectId)
        map.addProperty(API_KEY_LIBRARY_ID,"")
        map.addProperty(API_KEY_GRADE_ID,pref.gradeId)
        map.addProperty(API_KEY_METHOD,GET_ALL_DATA)
        requestData(mNetworkService.getAllDoubtRoom(pref.token, map), {
            allDoubtRooms.postValue(it.data)
            if(it.data.isEmpty()){
                message.set(it.message)
            }
        })
    }

    fun createDoubt(map:JsonObject) {
        requestData(mNetworkService.createDoubt(pref.token, map), {
            doubt.postValue(it.data)
        })
    }

    fun uploadFile(file: File) {
        val requestFile: RequestBody = RequestBody.create(
            MediaType.parse("multipart/form-data"),
            file
        )
        val multipartBody =
            MultipartBody.Part.createFormData("documentImg", file.name, requestFile)
        requestData(mNetworkService.uploadData(pref.token,"document",multipartBody),{
            uploadFileUrl.postValue(it.data?.full_path)
        },{m,c->
            mErrorMessage.postValue(m)
        })
    }
}

