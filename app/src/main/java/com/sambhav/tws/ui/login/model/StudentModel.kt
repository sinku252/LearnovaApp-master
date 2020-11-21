package com.sambhav.tws.ui.login.model

data class StudentModelResponse(
    val message: String,
    val error: Boolean,
    val data: StudentModel?=StudentModel()
)

data class StudentModel(
    var id : String="",
    var enroll_no : String="",
    var password : String="",
    var school_id : String="",
    var grade_id : String="",
    var grade_title : String="",
    var name : String="",
    var email : String="",
    var mobile : String="",
    var gender : String="",
    var profile_image : String="",
    var is_login : Boolean=false,
    var app_token : String="",
    var type : String="",
    var is_premium : String="",
    var transaction_id : String="",
    var subscription : String="",
    var stream_title : String=""
)