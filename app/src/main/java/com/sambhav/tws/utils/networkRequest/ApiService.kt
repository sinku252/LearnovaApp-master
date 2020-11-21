package com.sambhav.tws.utils.networkRequest

import com.google.gson.JsonObject
import com.sambhav.tws.apiModel.*
import com.sambhav.tws.ui.chapter.model.ChapterResponse
import com.sambhav.tws.ui.grade.model.GradeResponse
import com.sambhav.tws.ui.home.doubt.model.DoubtRoomModel
import com.sambhav.tws.ui.home.exam.model.ExamListResponse
import com.sambhav.tws.ui.home.exam.model.QuestionsListResponse
import com.sambhav.tws.ui.home.exam.model.TestResultData
import com.sambhav.tws.ui.home.home.model.NotificationResponseModel
import com.sambhav.tws.ui.home.notes.model.NotesResponseModel
import com.sambhav.tws.ui.home.notes.model.NotesUploadResponse
import com.sambhav.tws.ui.login.model.LogoutModelResponse
import com.sambhav.tws.ui.login.model.StudentModelResponse
import com.sambhav.tws.ui.login.model.TeacherModelResponse
import com.sambhav.tws.ui.payemnt.model.GradeData
import com.sambhav.tws.ui.subject.model.SubjectResponse
import okhttp3.MultipartBody
import retrofit2.http.*
import rx.Observable


interface ApiService {

    @FormUrlEncoded
    @POST("students/login")
    fun getStudentLogin(@FieldMap map: Map<String, String>): Observable<StudentModelResponse>

    @FormUrlEncoded
    @POST("Teachers/login")
    fun getTeacherLogin(@FieldMap map: Map<String, String>): Observable<TeacherModelResponse>

    @Headers( "Accept: application/json", "Content-Type: application/json")
    @POST("subjects")
    fun getAllStudentSubject(
        @Header("auth-key") token: String,
        @Body map: JsonObject
    ): Observable<SubjectResponse>

    @Headers( "Accept: application/json", "Content-Type: application/json")
    @POST("TeacherSubjects")
    fun getAllTeacherSubject(
        @Header("auth-key") token: String,
        @Body map: JsonObject
    ): Observable<SubjectResponse>

    @Headers( "Accept: application/json", "Content-Type: application/json")
    @POST("TeacherNotes")
    fun getAllTeacherNotes(
        @Header("auth-key") token: String,
        @Body map: JsonObject
    ): Observable<NotesResponseModel>

    @Headers( "Accept: application/json", "Content-Type: application/json")
    @POST("StudentNotes/show")
    fun getAllStudentNotes(
        @Header("auth-key") token: String,
        @Body map: JsonObject
    ): Observable<NotesResponseModel>

    @Headers( "Accept: application/json", "Content-Type: application/json")
    @POST("TeacherVideo")
    fun getAllTeacherVideo(
        @Header("auth-key") token: String,
        @Body map: JsonObject
    ): Observable<VideoLibraryListResponse>

    @Headers( "Accept: application/json", "Content-Type: application/json")
    @POST("StudentVideos/show")
    fun getAllStudentVideo(
        @Header("auth-key") token: String,
        @Body map: JsonObject
    ): Observable<VideoLibraryListResponse>

    @Headers( "Accept: application/json", "Content-Type: application/json")
    @POST("TeacherGrade")
    fun getAllTeacherGrade(
        @Header("auth-key") token: String,
        @Body map: JsonObject
    ): Observable<GradeResponse>

    @Headers( "Accept: application/json", "Content-Type: application/json")
    @POST("TeacherVideo")
    fun uploadTeacherVideo(
        @Header("auth-key") token: String,
        @Body map: JsonObject
    ): Observable<VideoUploadResponse>

    @Headers( "Accept: application/json", "Content-Type: application/json")
    @POST("TeacherNotes")
    fun uploadTeacherNotes(
        @Header("auth-key") token: String,
        @Body map: JsonObject
    ): Observable<NotesUploadResponse>

    @Headers( "Accept: application/json", "Content-Type: application/json")
    @DELETE("TeacherVideo/delete/{id}")
    fun deleteTeacherVideo(
        @Header("auth-key") token: String,
        @Path("id") id:String
    ): Observable<VideoDeleteResponse>

    @Headers( "Accept: application/json", "Content-Type: application/json")
    @DELETE("TeacherNotes/delete/{id}")
    fun deleteTeacherNotes(
        @Header("auth-key") token: String,
        @Path("id") id:String
    ): Observable<VideoDeleteResponse>

    @Headers( "Accept: application/json", "Content-Type: application/json")
    @POST("TeacherLive")
    fun teacherLiveClasses(
        @Header("auth-key") token: String,
        @Body map: JsonObject
    ): Observable<LiveClassResponse>

    @Headers( "Accept: application/json", "Content-Type: application/json")
    @POST("TeacherLive")
    fun createSchedule(
        @Header("auth-key") token: String,
        @Body map: JsonObject
    ): Observable<CreateScheduleResponse>

    @Headers( "Accept: application/json", "Content-Type: application/json")
    @POST("Teachers/logout")
    fun teacherLogout(
        @Header("auth-key") token: String,
        @Body map: JsonObject
    ): Observable<LogoutModelResponse>

    @Headers( "Accept: application/json", "Content-Type: application/json")
    @POST("students/logout")
    fun studentLogout(
        @Header("auth-key") token: String,
        @Body map: JsonObject
    ): Observable<LogoutModelResponse>

    @Headers( "Accept: application/json", "Content-Type: application/json")
    @POST("teachers/show")
    fun getTeacher(
        @Header("auth-key") token: String,
        @Body map: JsonObject
    ): Observable<TeacherModelResponse>

    @Headers( "Accept: application/json", "Content-Type: application/json")
    @PUT("teachers")
    fun updateTeacher(
        @Header("auth-key") token: String,
        @Body map: JsonObject
    ): Observable<TeacherModelResponse>

    @Headers( "Accept: application/json", "Content-Type: application/json")
    @PUT("Students")
    fun updateStudent(
        @Header("auth-key") token: String,
        @Body map: JsonObject
    ): Observable<StudentModelResponse>

    @Headers( "Accept: application/json", "Content-Type: application/json")
    @POST("Students")
    fun updateStudentPayment(
        @Header("auth-key") token: String,
        @Body map: JsonObject
    ): Observable<StudentModelResponse>

    @Headers( "Accept: application/json", "Content-Type: application/json")
    @POST("Students")
    fun getStudent(
        @Header("auth-key") token: String,
        @Body map: JsonObject
    ): Observable<StudentModelResponse>

    @Headers( "Accept: application/json", "Content-Type: application/json")
    @POST("TeacherChapters")
    fun getAllTeacherChapters(
        @Header("auth-key") token: String,
        @Body map: JsonObject
    ): Observable<ChapterResponse>

    @Headers( "Accept: application/json", "Content-Type: application/json")
    @POST("Chapters")
    fun getAllStudentChapters(
        @Header("auth-key") token: String,
        @Body map: JsonObject
    ): Observable<ChapterResponse>

    @Multipart
    @POST("UplaodFiles")
    fun uploadData(
        @Header("auth-key") token: String,
        @Query("folderLocation") folderLocation :String,
        @Part file : MultipartBody.Part
    ): Observable<FileUploadResponse>


    @Headers( "Accept: application/json", "Content-Type: application/json")
    @POST("Doubts")
    fun getAllDoubts(
        @Header("auth-key") token: String,
        @Body map: JsonObject
    ): Observable<DoubtListResponse<ArrayList<DoubtData>>>

    @Headers( "Accept: application/json", "Content-Type: application/json")
    @POST("Doubtroom")
    fun getDoubtsRoomDetails(
        @Header("auth-key") token: String,
        @Body map: JsonObject
    ): Observable<DoubtListResponse<ArrayList<DoubtData>>>

    @Headers( "Accept: application/json", "Content-Type: application/json")
    @POST("Doubts")
    fun createDoubt(
        @Header("auth-key") token: String,
        @Body map: JsonObject
    ): Observable<DoubtListResponse<DoubtData>>

    @Headers( "Accept: application/json", "Content-Type: application/json")
    @POST("Notifications")
    fun getNotification(
        @Header("auth-key") token: String,
        @Body map: JsonObject
    ): Observable<NotificationResponseModel>

    @Headers( "Accept: application/json", "Content-Type: application/json")
    @POST("Doubtroom")
    fun getAllDoubtRoom(
        @Header("auth-key") token: String,
        @Body map: JsonObject
    ): Observable<DoubtListResponse<ArrayList<DoubtRoomModel>>>

    @Headers( "Accept: application/json", "Content-Type: application/json")
    @POST("Live")
    fun getAllStudentLiveClass(
        @Header("auth-key") token: String,
        @Body map: JsonObject
    ): Observable<LiveClassResponse>

    @Headers( "Accept: application/json", "Content-Type: application/json")
    @POST("Grades")
    fun getAllStudentGrade(
        @Body map: JsonObject
    ): Observable<GradeResponse>

    @Headers( "Accept: application/json", "Content-Type: application/json")
    @POST("SignupStreams")
    fun getAllStudentStream(
        @Body map: JsonObject
    ): Observable<GradeResponse>

    @Headers( "Accept: application/json", "Content-Type: application/json")
    @POST("Signup")
    fun studentSignUp(@Body map: JsonObject): Observable<StudentModelResponse>

    @Multipart
    @POST("/upload")
    fun uploadFile(@Part file: MultipartBody.Part?): Observable<FileUploadResponse>

    @Headers( "Accept: application/json", "Content-Type: application/json")
    @POST("Test/create")
    fun getAllTest(
        @Header("auth-key") token: String,
        @Body map: JsonObject
    ): Observable<ExamListResponse>

    @Headers( "Accept: application/json", "Content-Type: application/json")
    @POST("QuestionBanks/create")
    fun getQuestionBanks(
        @Header("auth-key") token: String,
        @Body map: JsonObject
    ): Observable<QuestionsListResponse>

    @Headers( "Accept: application/json", "Content-Type: application/json")
    @POST("Test/create")
    fun submitExam(
        @Header("auth-key") token: String,
        @Body map: JsonObject
    ): Observable<QuestionsListResponse>

    @Headers( "Accept: application/json", "Content-Type: application/json")
    @POST("Test/create")
    fun testResult(
        @Header("auth-key") token: String,
        @Body map: JsonObject
    ): Observable<TestResultData>


    @Headers( "Accept: application/json", "Content-Type: application/json")
    @POST("Grades/create")
    fun getGradeData(
        @Header("auth-key") token: String,
        @Body map: JsonObject
    ): Observable<GradeData>


}