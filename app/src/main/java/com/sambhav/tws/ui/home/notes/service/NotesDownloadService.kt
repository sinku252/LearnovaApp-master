package com.sambhav.tws.ui.home.notes.service

import android.app.IntentService
import android.content.Intent
import android.util.Log
import com.downloader.*
import com.sambhav.tws.utils.BASE_DOC_RECEIVE_PATH
import com.sambhav.tws.utils.EXTRA_KEY_URL
import java.io.File


class NotesDownloadService :IntentService("") {
    val TAG =  "NotesDownload"
    var notesId:String =""
    var url:String =""
    var downloadId:Int =-1

    companion object{
        fun cancel(id:String,dwonId:Int){
            for (i in 0 until downloadQueue.size){
                if (downloadQueue[i]==id) {
                    downloadQueue.removeAt(i)
                    if (progressListeners.containsKey(id)){
                        progressListeners[id]?.onCancel()
                    }
                    break
                }

            }

            for (i in 0 until downloadingTasks.size){
                if (downloadingTasks[i]==id) {
                    downloadingTasks.removeAt(i)
                    if (progressListeners.containsKey(id)){
                        progressListeners[id]?.onCancel()
                    }
                    progressListeners.remove(id)
                    PRDownloader.pause(dwonId)
                    break
                }
            }

        }
        var progressListeners = HashMap<String,ProgressListener>()
        var downloadQueue = ArrayList<String>()
        var downloadingTasks = ArrayList<String>()

        fun containsTask(messageLocalId: String): Boolean{
            val q = downloadQueue.indexOfFirst { it==messageLocalId }
            return (q >0)
        }
        fun addProgressListener(messageLocalId: String, progressListener: ProgressListener){
            progressListeners[messageLocalId] = progressListener
        }
        fun cancelTask(id: String,downloadId:Int){
            for (i in 0 until downloadQueue.size){
                if (downloadQueue[i]==id) {
                    downloadQueue.removeAt(i)
                    if (progressListeners.containsKey(id)){
                        progressListeners[id]?.onCancel()
                    }
                    break
                }

            }

            for (i in 0 until downloadingTasks.size){
                if (downloadingTasks[i]==id) {
                   // val downloadId = downloadingTasks[i].dwonLoadId
                    downloadingTasks.removeAt(i)
                    if (progressListeners.containsKey(id)){
                        progressListeners[id]?.onCancel()
                    }
                    progressListeners.remove(id)
                    PRDownloader.pause(downloadId)
                    break
                }
            }

        }
    }
    override fun onHandleIntent(intent: Intent?) {
        url = intent?.getStringExtra(EXTRA_KEY_URL)?:""
        notesId = intent?.getStringExtra("id")?:""

       // url ="https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf"
        val ind = downloadQueue.indexOfFirst { it==notesId }
        if(ind >=0){
            return
        }

        val ind1 = downloadingTasks.indexOfFirst { it==notesId }
        if(ind1 >=0){
            return
        }
        if (downloadingTasks.size>1){
            downloadQueue.add(notesId)
        }else{
            downloadingTasks.add(notesId)
            startDownload()
        }
    }

    private fun startDownload(){
        Log.d(TAG,"startDownload "+url)
        val filePath = BASE_DOC_RECEIVE_PATH+"DOC_$notesId"
        val file = File(BASE_DOC_RECEIVE_PATH)
        if(!file.exists()){
            file.mkdirs()
        }
        var exe = url
        val j = exe.lastIndexOf(".")
        if (j >= 0) {
            exe = exe.substring(j)
        }

         downloadId = PRDownloader.download(url, BASE_DOC_RECEIVE_PATH, "DOC_$notesId$exe")
            .build()
            .setOnStartOrResumeListener {
                Log.d(TAG,"setOnPauseListener")
                startDwonLoading()
            }
            .setOnPauseListener {
                Log.d(TAG,"setOnPauseListener ")

                progressListeners[notesId]?.onCancel()
                for (i in 0 until downloadQueue.size){
                    if (downloadQueue[i]==notesId){
                        downloadQueue.removeAt(i)
                        break
                    }
                }
                for (i in 0 until downloadingTasks.size){
                    if (downloadingTasks[i]== notesId) {
                        downloadingTasks.removeAt(i)
                        break
                    }
                }
                progressListeners.remove(notesId)
                startNextDownload()
            }
            .setOnCancelListener {
                Log.d(TAG,"setOnCancelListener ")
                progressListeners[notesId]?.onCancel()}
            .setOnProgressListener {
                val progressPercent = it.currentBytes * 100 / it.totalBytes
                val progress = progressPercent.toInt()
                Log.d(TAG,"setOnProgressListener $progress .")

                progressListeners[notesId]?.onProgressUpdate(progress)

            }
            .start(object : OnDownloadListener {
                override fun onDownloadComplete() {
                    if (progressListeners.containsKey(notesId)){
                        progressListeners[notesId]?.onSuccess(filePath)
                    }
                    for (i in 0 until downloadQueue.size){
                        if (downloadQueue[i]==notesId){
                            downloadQueue.removeAt(i)
                            break
                        }
                    }
                    for (i in 0 until downloadingTasks.size){
                        if (downloadingTasks[i] == notesId) {
                            downloadingTasks.removeAt(i)
                            break
                        }
                    }
                    progressListeners.remove(notesId)
                    startNextDownload()
                }

                override fun onError(error: com.downloader.Error?) {
                    Log.d(TAG,"onError "+error?.connectionException?.message)

                    if (progressListeners.containsKey(notesId)){
                        progressListeners[notesId]?.onCancel()
                    }
                    for (i in 0 until downloadQueue.size){
                        if (downloadQueue[i]==notesId){
                            downloadQueue.removeAt(i)
                            break
                        }
                    }
                    for (i in 0 until downloadingTasks.size){
                        if (downloadingTasks[i] == notesId) {
                            downloadingTasks.removeAt(i)
                            break
                        }
                    }
                    progressListeners.remove(notesId)
                    startNextDownload()
                }
            })
    }

    private fun startDwonLoading() {
        progressListeners[notesId]?.onStart(downloadId)
    }


    private fun startNextDownload() {
        if (downloadQueue.size>0){
            val message = downloadQueue[0]
            downloadQueue.removeAt(0)
            downloadingTasks.add(message)
            startDownload()
        }
    }
}