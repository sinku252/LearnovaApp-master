package com.sambhav.tws.ui.home.videos.model

data class VideosSubModel(
    var subject:String="",
    var subIcon:Int=0,
    var backColor:String="")

data class VideosListModel(
    var subject:String="",
    var subIcon:Int=0,
    var fileName:String = "",
    var teacherImage:String = "",
    var time:String = "",
    var isDownload:Boolean = false,
    var backColor:String="",
    var primaryColor:String=""
)