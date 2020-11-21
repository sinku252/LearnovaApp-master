package com.sambhav.tws.ui.subject.model

data class SubjectResponse(
    var error:Boolean=false,
    var message:String="",
    var data: ArrayList<SubjectData> ?= ArrayList()
)

data class SubjectData(
    var id:String="",
    var grade_id:String="",
    var subject_id:String="",
    var subject_image:String="",
    var subject_title:String="",
    var subject_short_code:String="",
    var backColor:String="#207DFF",
    var subName:String = subject_title,
    var subIcon:Long = 0L,
    var subject_prices:ArrayList<Long>? = ArrayList()
)

data class SubjectBody(
    var school_id:String="1",
    var student_id:String="1",
    var grade_id:String="1"
)

