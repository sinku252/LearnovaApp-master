package com.sambhav.tws.utils.networkRequest

import android.os.AsyncTask
import android.util.Log
import com.google.gson.Gson
import com.sambhav.tws.apiModel.vimeo.CreateVideoResponse
import okhttp3.*
import org.json.JSONObject
import java.io.DataInputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException

class VimeoService {
    companion object {
        val CREATE_VIDEO = "https://api.vimeo.com/me/videos"
        val JSON_TYPE = MediaType.parse("application/json; charset=utf-8");
        val BYTE_TYPE = MediaType.parse("application/offset+octet-stream");
        @JvmStatic
        fun createVideo(
            client: OkHttpClient,
            jsonStr: String,
            result: (str: String?) -> Unit
        ) {
            Log.d("VimeoService", "createVideo " + jsonStr)
            object : AsyncTask<String, Int, String>() {
                override fun doInBackground(vararg p0: String?): String {
                    val reBdy = RequestBody.create(JSON_TYPE, jsonStr)

                    val request = Request.Builder()
                        .header("Authorization", "Bearer 6ccf6e2214783890eaafc64b56ca71ab")
                        .header("Content-Type", "application/json")
                        .header("Accept", "application/vnd.vimeo.video;version=3.4")
                        .post(reBdy)
                        .url(CREATE_VIDEO)
                        .build()
                    client.newCall(request).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            Log.d("VimeoService", "createVideo " + e.message)

                        }

                        override fun onResponse(call: Call, response: Response) {
                            try {
                                val res = response.body()?.string()
                                val mdel = Gson().fromJson(res, CreateVideoResponse::class.java)
                                Log.d("VimeoService", " " +  Gson().toJson(mdel.upload))
                                result( Gson().toJson(mdel))
                            }catch (e:Exception){
                                //Expected a string but was BEGIN_OBJECT at line 1 column 36 path $.name
                            }
                        }
                    })
                    return ""
                }

                override fun onPostExecute(result: String?) {
                    super.onPostExecute(result)
                    Log.d("VimeoService", "onPostExecute " + result)
                }
            }.execute()
        }

        @JvmStatic
        fun uploadFile(
            client: OkHttpClient,
            url: String,
            file: File,
            result: (str: String?) -> Unit
        ) {
            Log.d("VimeoService", "File uploading..."+url)

            object :AsyncTask<String,Int,String>(){
                override fun doInBackground(vararg p0: String?): String {
                    val fileData = ByteArray(file.length().toInt())
                    val dis = DataInputStream(FileInputStream(file))
                    dis.readFully(fileData)
                    dis.close()
                    val  body = RequestBody.create(BYTE_TYPE,fileData)
                    val request = Request.Builder()
                        .url(url)
                        .patch(body)
                        .addHeader("Content-Type", "application/offset+octet-stream")
                        .addHeader("Accept", "application/vnd.vimeo.video;version=3.4")
                        .addHeader("Tus-Resumable", "1.0.0")
                        .addHeader("Upload-Offset", "0")
                        .build()

                    client.newCall(request).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            Log.d("VimeoService", "uploadFile onFailure " + e?.message)
                            result("")
                        }

                        override fun onResponse(call: Call, response: Response) {
                            val res = response.body()?.string()
                            Log.d("VimeoService", "uploadFile success " + res)
                            result(res)
                        }
                    })
                    return ""
                }
            }.execute()
        }

        @JvmStatic
        fun extractUrl(
            client: OkHttpClient,
            url: String,
            result: (str: String?) -> Unit
        ) {
            Log.d("VimeoService", "extractUrl ")
            object : AsyncTask<String, Int, String>() {
                override fun doInBackground(vararg p0: String?): String {

                    val request = Request.Builder()
                        .get()
                        .url(url)
                        .build()
                    client.newCall(request).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            Log.d("VimeoService", "createVideo " + e.message)

                        }

                        override fun onResponse(call: Call, response: Response) {
                            val res = response.body()?.string()
                            val json = JSONObject(res)

                            try {
                                val nUrl = json.getJSONObject("request").getJSONObject("files")
                                    .getJSONObject("hls").getJSONObject("cdns")
                                    .getJSONObject("akfire_interconnect_quic")
                                    .getString("url")

                                result(nUrl)
                            }catch (e:Exception){
                                result(url)
                            }

                        }
                    })
                    return ""
                }

                override fun onPostExecute(result: String?) {
                    super.onPostExecute(result)
                    Log.d("VimeoService", "onPostExecute " + result)
                }
            }.execute()
        }
    }


}