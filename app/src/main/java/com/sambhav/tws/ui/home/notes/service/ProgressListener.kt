package com.sambhav.tws.ui.home.notes.service

/**
 * Created by bodacious on 29/12/17.
 */
interface ProgressListener {
    fun onProgressUpdate(progress: Int)
    fun onStart(id:Int)
    fun onSuccess(path: String)
    fun onFileNotFound()
    fun onCancel()
}
