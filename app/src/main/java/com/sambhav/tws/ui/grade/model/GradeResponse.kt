package com.sambhav.tws.ui.grade.model

data class GradeResponse(
    var error:Boolean=false,
    var message:String="",
    var data:ArrayList<GradeData>? = ArrayList()
)

data class GradeData(
    var id:String="",
    var title:String="",
    var grade_id:String="",
    var grade_title:String="",
    var grade_image:String=""
)