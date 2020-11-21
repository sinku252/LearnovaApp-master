package com.sambhav.tws.ui.home.exam.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.sambhav.tws.R
import com.sambhav.tws.base.BaseViewModel
import com.sambhav.tws.ui.home.exam.model.AttempQuestions
import com.sambhav.tws.ui.home.exam.model.ExamData
import com.sambhav.tws.ui.home.exam.model.QuestionData
import com.sambhav.tws.ui.home.exam.model.TestResultData
import com.sambhav.tws.ui.login.model.StudentModel
import com.sambhav.tws.ui.login.model.TeacherModel
import com.sambhav.tws.utils.*
import com.sambhav.tws.utils.networkRequest.ApiService

class ExamViewModel(
    private val mNetworkService: ApiService,
    private val pref: SharedPreferencesHelper
) : BaseViewModel() {


    val allTestList = MutableLiveData<ArrayList<ExamData>>()

    val attemptList = MutableLiveData<ArrayList<ExamData>>()
    val unattemptList = MutableLiveData<ArrayList<ExamData>>()

    val mTeacherData = Gson().fromJson(pref.teacherData, TeacherModel::class.java)
    val mStudentData = Gson().fromJson(pref.studentData, StudentModel::class.java)
    val allQuestionList = MutableLiveData<ArrayList<QuestionData>>()
    val submitExam = MutableLiveData<Boolean>()
    //val testResult = MutableLiveData<Boolean>()
    val testResult =MutableLiveData<TestResultData>()

    fun getTestList(type:Int) =
        if (pref.isStudent) getTestStudentList(type)
        else getTestTeacherList(type)

    fun getTestResult(testId: String) =
        if (pref.isStudent) getTestResultStudent(testId)
        else getTestResultTeacher(testId)

    fun getTestStudentList(type: Int) {
        val map = JsonObject()
        map.addProperty(API_KE_SCHOOL_ID,mStudentData.school_id)
        map.addProperty(API_KEY_STUDENT_ID,mStudentData.id)
        //map.addProperty(API_KEY_SUBJECT_ID,"2")
        //map.addProperty(API_KEY_SUBJECT_ID,subjectId)
        //map.addProperty(API_KEY_SUBJECT_ID,subjectId)
        //map.addProperty(API_KEY_GRADE_ID,mStudentData.grade_id)
        map.addProperty(API_KEY_GRADE_ID,mStudentData.grade_id)
       // map.addProperty(API_KEY_CHAPTER_ID,chapterId)
        map.addProperty(API_KEY_METHOD,GET_ALL_DATA)
        requestData(mNetworkService.getAllTest(pref.token, map), {
            if(it.error != false){
                noIcon.set(R.drawable.ic_add_video)
                message.set(it.message)
            }else{
                Log.e("ASDASDASDASDASD","onActivityStarted"+it.data)
                //unattemptList.postValue(it.data.unattempt_list)
                if(type.equals(0))
                {
                    unattemptList.postValue(it.data.unattempt_list)
                }
                else
                {
                    attemptList.postValue(it.data.attempt_list)
                }

            }
        })
    }

    fun getTestTeacherList(type:Int) {
        val map = JsonObject()
        map.addProperty(API_KE_SCHOOL_ID,mTeacherData.school_id)
        map.addProperty(API_KEY_STUDENT_ID,mTeacherData.id)
        //map.addProperty(API_KEY_SUBJECT_ID,"2")
        //map.addProperty(API_KEY_SUBJECT_ID,subjectId)
       // map.addProperty(API_KEY_SUBJECT_ID,subjectId)
        //map.addProperty(API_KEY_GRADE_ID,mStudentData.grade_id)
        map.addProperty(API_KEY_GRADE_ID,pref.gradeId)
        // map.addProperty(API_KEY_CHAPTER_ID,chapterId)
        map.addProperty(API_KEY_METHOD,GET_ALL_DATA)
        requestData(mNetworkService.getAllTest(pref.token, map), {
            if(it.error== false){
                noIcon.set(R.drawable.ic_add_video)
                message.set(it.message)
            }else{
                Log.e("ASDASDASDASDASD","onActivityStarted"+it.data)
                if(type.equals(1))
                allTestList.postValue(it.data.attempt_list)
                else
                allTestList.postValue(it.data.unattempt_list)
            }
        })
    }

    fun getQuestionBanks(examData: ExamData,page:String) {
        val map = JsonObject()
        map.addProperty(API_KE_SCHOOL_ID,mStudentData.school_id)
        map.addProperty(API_KEY_STUDENT_ID,mStudentData.id)
        //map.addProperty(API_KEY_SUBJECT_ID,"2")
        //map.addProperty(API_KEY_SUBJECT_ID,subjectId)
        map.addProperty(API_KEY_PAGE,page)
        //map.addProperty(API_KEY_GRADE_ID,mStudentData.grade_id)
        map.addProperty(API_KEY_TEST_ID,examData.id)
        //map.addProperty(API_KEY_CHAPTER_ID,chapterId)
        map.addProperty(API_KEY_METHOD,GET_ALL_DATA)
        requestData(mNetworkService.getQuestionBanks(pref.token, map), {
            Log.e("ASDASDASDASDASD","onActivityStarted"+it.data)
            allQuestionList.postValue(it.data)
            /*if(it.data?.isEmpty() != false){
                noIcon.set(R.drawable.ic_add_video)
                message.set(it.message)
            }else{
                Log.e("ASDASDASDASDASD","onActivityStarted"+it.data)
                allQuestionList.postValue(it.data)
            }*/
        })
    }

    fun submitExam(attempQuestionsList: ArrayList<AttempQuestions>,attemptId:String) {

        val map = JsonObject()
        map.addProperty(API_KE_SCHOOL_ID,mStudentData.school_id)
        map.addProperty(API_KEY_STUDENT_ID,mStudentData.id)
      //  map.addProperty(API_KEY_SUBJECT_ID,subjectId)
        map.addProperty(API_KEY_GRADE_ID,mStudentData.grade_id)
        map.addProperty(API_KEY_ATTEMP_QUESTIONS_LIST,Gson().toJson(attempQuestionsList))
        map.addProperty(API_KEY_ATTEMPT_ID,attemptId)
        //map.addProperty(API_KEY_CHAPTER_ID,chapterId)
        map.addProperty(API_KEY_METHOD,SAVE_TEST_ATTEMPT_DATA)
        requestData(mNetworkService.submitExam(pref.token, map), {
            submitExam.postValue(true)

        },{ _, _ ->
            submitExam.postValue(false)

        })
    }

    fun getTestResultStudent(testId:String) {
        val map = JsonObject()
        map.addProperty(API_KE_SCHOOL_ID,mStudentData.school_id)
        map.addProperty(API_KEY_STUDENT_ID,mStudentData.id)
        map.addProperty(API_KEY_TEST_ID,testId)
        map.addProperty(API_KEY_GRADE_ID,pref.gradeId)
        map.addProperty(API_KEY_METHOD,TEST_RESULT_DATA)
        requestData(mNetworkService.testResult(pref.token,map),{
            testResult.postValue(it)
        },{message,code->
            Log.e("ADASDASDASD","YESAAAQQ")
            mErrorMessage.value = message
        })
        requestData(mNetworkService.testResult(pref.token, map), {

            /*if(it.data?.isEmpty() != false){
                noIcon.set(R.drawable.ic_add_video)
                message.set(it.message)
            }else{
                Log.e("ASDASDASDASDASD","onActivityStarted"+it.data)
                testResult.postValue(it.data)
            }*/
        })
    }

    fun getTestResultTeacher(testId:String) {
        val map = JsonObject()
        map.addProperty(API_KE_SCHOOL_ID,mTeacherData.school_id)
        map.addProperty(API_KEY_STUDENT_ID,mTeacherData.id)
        map.addProperty(API_KEY_TEST_ID,testId)
        map.addProperty(API_KEY_GRADE_ID,pref.gradeId)
        map.addProperty(API_KEY_METHOD,TEST_RESULT_DATA)
        requestData(mNetworkService.testResult(pref.token,map),{
            testResult.postValue(it)
        },{message,code->
            Log.e("ADASDASDASD","YESAAAQQ")
            mErrorMessage.value = message
        })
        requestData(mNetworkService.testResult(pref.token, map), {

            /*if(it.data?.isEmpty() != false){
                noIcon.set(R.drawable.ic_add_video)
                message.set(it.message)
            }else{
                Log.e("ASDASDASDASDASD","onActivityStarted"+it.data)
                testResult.postValue(it.data)
            }*/
        })
    }

}