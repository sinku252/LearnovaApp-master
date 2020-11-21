package com.sambhav.tws.ui.chapter.model

data class ChapterResponse(
    var error:Boolean=false,
    var message:String="",
    var data: ArrayList<ChapterData> ?= ArrayList()
)

data class ChapterData(
    var chapter_id:String="",
    var chapter_title:String="",
    var chapter_image:String="",
    var chapter_short_code:String=""
)