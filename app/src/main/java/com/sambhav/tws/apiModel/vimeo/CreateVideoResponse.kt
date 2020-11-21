package com.sambhav.tws.apiModel.vimeo

data class CreateVideoResponse(
    var uri: String = "",
    var link: String = "",
    var name: String = "",
    var description: String = "",
    var type: String = "",
    var duration: String = "",
    var language: String = "",
    var width: String = "",
    var height: String = "",
    var created_time: String = "",
    var modified_time: String = "",
    var release_time: String = "",
    var upload: Upload = Upload()
)

data class Upload(
    var status: String = "",
    var upload_link: String = "",
    var form: String = "",
    var complete_uri: String = "",
    var approach: String = "",
    var size: String = "",
    var redirect_url: String = "",
    var link: String = ""
)
