package com.sambhav.tws.apiModel

data class VideoLibraryListResponse(
    val message: String,
    val status: Boolean,
    val statusCode: Int=0,
    val data: ArrayList<VideoData> ?= ArrayList()
)

data class VideoData(
    val id : Int=0,
    val school_id : Int=0,
    val teacher_id : Int=0,
    val grade_id : Int=0,
    val subject_id : Int=0,
    val title : String="",
    val short_description : String="",
    val description : String="",
    val status : Int=0,
    val alt_tag : String="",
    val format_type : String="",
    val src_url : String="",
    val length : String="",
    val size : String="",
    val created_by : String="",
    var created_date : String="",
    var created_time : String="",
    val modified_date : String="",
    val short_code : String="",
    val image : String="",
    val created_by_type : String="",
    val teacher_first_name : String="",
    val teacher_last_name : String="",
    val teacher_image : String=""

)
data class VideoUploadResponse(
    val message: String,
    val status: Boolean,
    val statusCode: Int=0,
    val data: VideoData
)
data class VideoDeleteResponse(
    val message: String,
    val status: Boolean,
    val statusCode: Int=0,
    val data: String
)
