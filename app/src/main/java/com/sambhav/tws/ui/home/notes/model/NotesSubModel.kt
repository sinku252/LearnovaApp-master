package com.sambhav.tws.ui.home.notes.model

data class NotesResponseModel(
    var error:Boolean=false,
    var message: String="",
    var data:ArrayList<NotesSubListModel> ? = ArrayList()
)

data class NotesSubModel(
    var subject:String="",
    var subIcon:Int=0,
    var backColor:String="",
    var primaryColor:String=""
)

data class NotesSubListModel(
    var id:String="",
    var dwonLoadId:Int=0,
    var school_id:String="",
    var teacher_id:String = "",
    var grade_id:String = "",
    var subject_id:String = "",
    var chapter_id:String = "",
    var title:String = "",
    var short_description:String = "",
    var description:String = "",
    var status:String = "",
    var alt_tag:String = "",
    var format_type:String = "",
    var src_url:String = "",
    var length:String = "",
    var size:String = "",
    var created_by:String = "",
    var created_date:String = "",
    var created_time : String="",
    var modified_date:String = "",

    var backColor:String="",
    var primaryColor:String="",
    var isDownload:Boolean = false
)

data class NotesUploadResponse(
    val message: String,
    val status: Boolean,
    val statusCode: Int=0,
    val data: NotesSubListModel
)