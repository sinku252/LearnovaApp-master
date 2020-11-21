package com.sambhav.tws.ui.payemnt.model

data class GradeData(
    var error:Boolean=false,
    var message:String="",
    val data: GradeModel? = GradeModel()
)

data class GradeModel(
    val title:String="",
    val short_description:String="",
    val description:String="",
    val status:String="",
    val price:String="",
    val image:String="",
    val use_for:String="",
    val created_by_type:String="",
    val created_by:String="",
    val created_date:String="",
    val modified_date:String=""
)
