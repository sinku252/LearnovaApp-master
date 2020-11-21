package com.sambhav.tws.apiModel

data class FileUploadResponse(
    var error:Boolean=false,
    var message:String="",
    var data: FileData?= FileData()
)

data class FileData(
    var file_name:String="",
    var file_type:String="",
    var file_path:String="",
    var full_path:String="",
    var raw_name:String="",
    var orig_name:String="",
    var client_name:String="",
    var file_ext:String="",
    var file_size:String="",
    var is_image:String="",
    var image_width:String="",
    var image_height:String="",
    var image_type:String="",
    var image_size_str:String=""
)