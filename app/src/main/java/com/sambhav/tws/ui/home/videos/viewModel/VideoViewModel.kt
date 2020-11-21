package com.sambhav.tws.ui.home.videos.viewModel

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.sambhav.tws.R
import com.sambhav.tws.apiModel.VideoData
import com.sambhav.tws.base.BaseViewModel
import com.sambhav.tws.ui.chapter.model.ChapterData
import com.sambhav.tws.ui.login.model.StudentModel
import com.sambhav.tws.ui.login.model.TeacherModel
import com.sambhav.tws.utils.*
import com.sambhav.tws.utils.networkRequest.ApiService
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class VideoViewModel(
    private val mNetworkService: ApiService,
    private val pref: SharedPreferencesHelper
) : BaseViewModel() {

    val uploadData = MutableLiveData<Boolean>()
    val deleteData = MutableLiveData<Boolean>()
    val uploadFileUrl = MutableLiveData<String>()
    val allVideos = MutableLiveData<ArrayList<VideoData>>()
    val allChapter = MutableLiveData<ArrayList<ChapterData>>()
    val mTeacherData = Gson().fromJson(pref.teacherData, TeacherModel::class.java)
    val mStudentData = Gson().fromJson(pref.studentData, StudentModel::class.java)

    fun getVideo(subjectId:String,chapterId: String) =
        if (pref.isStudent) getStudentVideos(subjectId,chapterId)
        else getTeacherVideos(subjectId,chapterId)

    private fun getTeacherVideos(subjectId:String, chapterId: String) {
        val map = JsonObject()
        map.addProperty(API_KE_SCHOOL_ID,mTeacherData.school_id)
        map.addProperty(API_KE_TEACHER_ID,mTeacherData.id)
        map.addProperty(API_KEY_SUBJECT_ID,subjectId)
        map.addProperty(API_KEY_GRADE_ID,pref.gradeId)
        map.addProperty(API_KEY_CHAPTER_ID,chapterId)
        map.addProperty(API_KEY_METHOD,GET_ALL_DATA)
        requestData(mNetworkService.getAllTeacherVideo(pref.token, map), {
            if(it.data?.isEmpty() != false){
                noIcon.set(R.drawable.ic_add_video)
                message.set(it.message)
            }else{
                allVideos.postValue(it.data)
            }
        })
    }

    private fun getStudentVideos(subjectId:String, chapterId: String) {
        val map = JsonObject()
        map.addProperty(API_KE_SCHOOL_ID,mStudentData.school_id)
        map.addProperty(API_KEY_STUDENT_ID,mStudentData.id)
        //map.addProperty(API_KEY_SUBJECT_ID,"2")
        map.addProperty(API_KEY_SUBJECT_ID,subjectId)
        map.addProperty(API_KEY_GRADE_ID,mStudentData.grade_id)
        map.addProperty(API_KEY_CHAPTER_ID,chapterId)
        map.addProperty(API_KEY_METHOD,GET_ALL_DATA)
        requestData(mNetworkService.getAllStudentVideo(pref.token, map), {
            if(it.data?.isEmpty() != false){
                noIcon.set(R.drawable.ic_add_video)
                message.set(it.message)
            }else{
                allVideos.postValue(it.data)
            }
        })
    }

    fun getTeacherChapters(subjectId : String){
        val map = JsonObject()
        map.addProperty(API_KE_SCHOOL_ID, getTeacherData(pref).school_id)
        map.addProperty(API_KE_TEACHER_ID,getTeacherData(pref).id)
        map.addProperty(API_KEY_SUBJECT_ID,subjectId)
        map.addProperty(API_KEY_GRADE_ID,pref.gradeId)
        map.addProperty(API_KE_METHOD, ALL_DATA)
        requestData(mNetworkService.getAllTeacherChapters(pref.token, map), {
            allChapter.postValue(it.data)
        })
    }

    fun uploadVideo(map:JsonObject) {
        requestData(mNetworkService.uploadTeacherVideo(pref.token, map), {
            uploadData.postValue(true)

        },{ _, _ ->
            uploadData.postValue(false)

        })
    }

    fun deleteVideo(id:String) {
        requestData(mNetworkService.deleteTeacherVideo(pref.token, id), {
            deleteData.postValue(true)
        },{ _, _ ->
            deleteData.postValue(false)
        })
    }

    fun uploadFile(file: File) {
        val requestFile: RequestBody = RequestBody.create(
            MediaType.parse("multipart/form-data"),
            file
        )
        val multipartBody =MultipartBody.Part.createFormData("documentImg", file.name, requestFile)
        requestData(mNetworkService.uploadData(pref.token,"videos",multipartBody),{
            uploadFileUrl.postValue(it.data?.full_path)
        },{m,c->
            mErrorMessage.postValue(m)
        })
    }
}