package com.sambhav.tws.ui.home.videos.activities

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.webkit.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.geeksmediapicker.GeeksMediaPicker
import com.geeksmediapicker.GeeksMediaType
import com.google.gson.JsonObject
import com.sambhav.tws.R
import com.sambhav.tws.apiModel.DoubtData
import com.sambhav.tws.base.BaseYoutubePlayerActivity
import com.sambhav.tws.databinding.ActivityVideosLiveBinding
import com.sambhav.tws.ui.home.doubt.adapter.AllDoubtAdapter
import com.sambhav.tws.ui.home.doubt.viewModel.DoubtViewModel
import com.sambhav.tws.utils.*
import kotlinx.android.synthetic.main.activity_videos_live.*
import kotlinx.android.synthetic.main.layout_chat_card.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 */
class LiveVideoActivity : BaseYoutubePlayerActivity() {
    private lateinit var mBinding: ActivityVideosLiveBinding
    private var mDoubtList = ArrayList<DoubtData>()
    private var adapter: AllDoubtAdapter? = null

    private val mViewModel: DoubtViewModel by viewModel()
    private var mSubjectId = ""
    private var mChapterId = ""
    private var mVideoId = ""
    private var mUrl = ""
    private var fullscreen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_videos_live)
        mBinding.viewModel = mViewModel
        mBinding.executePendingBindings()

        mSubjectId = intent.getStringExtra(EXTRA_KEY_SUBJECT_ID) ?: ""
        mChapterId = intent.getStringExtra(EXTRA_KEY_CHAPTER_ID) ?: ""
        mVideoId = intent.getStringExtra(EXTRA_KEY_VIDEO_ID) ?: ""
        mUrl = intent.getStringExtra(EXTRA_KEY_URL) ?: ""

        initClick()

        if(mPreference.isStudent){
           /* layout_chat.visibility = View.VISIBLE
            layout_btn.visibility = View.VISIBLE*/

            fetchFromViewModel()
        }else{
            layout_chat.visibility = View.GONE
            layout_btn.visibility = View.GONE

        }
        adapter = AllDoubtAdapter(this, ArrayList())
        recycler_view.adapter = adapter
        init()
        handleAuth(mViewModel)

    }

    fun init() {
        back.setOnClickListener(CustomClickListener {
            onBackPressed()
        })

        val videoType = intent.getStringExtra(EXTRA_KEY_VIDEO_TYPE) ?: ""
        Log.d("VimeoService", "extracting videoType " + videoType)

       // if (videoType == VIDEO_TYPE_YT_VDO && mUrl.contains("youtube.com",true) ) {
        if(videoType == VIDEO_TYPE_YT_VDO)
        {
            layYoutube.visibility = View.VISIBLE
            loadYouTubeThumbnail(getYouTubeId(mUrl),this);
           // initYoutube(getYouTubeId(mUrl))
        } else {
            lay_webView.visibility = View.VISIBLE
            initWebview()
        }
    }

    fun initWebview() {
        webView.setBackgroundColor(0)
        webView?.settings?.apply {
            mediaPlaybackRequiresUserGesture = true
            javaScriptEnabled = true //use this carefully
            domStorageEnabled=true
        }
        webView.loadUrl(getVideoUrl())
        webView.webChromeClient = object : WebChromeClient() {

            override fun onShowCustomView(
                fullScreenContent: View?,
                callback: WebChromeClient.CustomViewCallback
            ) {
                super.onShowCustomView(fullScreenContent, callback)
                fullScreenContent?.let { fullScreenView ->
                    videoFrame?.removeAllViews()
                    videoFrame?.visibility = View.VISIBLE
                    videoFrame?.addView(fullScreenView)
                }
                enterFullScreen()
                Log.d("WebViewAct", "str onShowCustomView ")
            }

            override fun onHideCustomView() {
                super.onHideCustomView()
                exitFullScreen()
                Log.d("WebViewAct", "str onHideCustomView ")
            }

        }
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return true
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                progress_bar_video?.visibility = View.VISIBLE

            }

            override fun onPageFinished(view: WebView?, url: String?) {
                progress_bar_video?.visibility = View.GONE

            }

            /*override fun onReceivedSslError(view: WebView,handler: SslErrorHandler,error: SslError)
            {
                handler.proceed() // Ignore SSL certificate errors
            }*/
        }
    }

    fun enterFullScreen(){
        toolbar.visibility = View.GONE
        layout_chat.visibility = View.GONE
        layout_btn.visibility = View.GONE
        back.visibility = View.GONE

        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        fullscreen = true
    }

    fun exitFullScreen(){
        toolbar.visibility = View.VISIBLE
        back.visibility = View.VISIBLE
        if(mPreference.isStudent){
           /* layout_chat.visibility = View.VISIBLE
            layout_btn.visibility = View.VISIBLE*/
        }

        videoFrame?.visibility = View.GONE
        videoFrame?.removeAllViews()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        fullscreen = false
    }

    private fun initClick() {
        iv_send.setOnClickListener(CustomClickListener {
            if (!TextUtils.isEmpty(etMsg.text.toString().trim())) {

                createDoubt()
                etMsg.setText("")
            }
        })

        iv_attach.setOnClickListener(CustomClickListener {
            GeeksMediaPicker.with(this)
                .setMediaType(GeeksMediaType.IMAGE)
                .setIncludesFilePath(true)
                .setEnableCompression(true)
                .startSingle { mediaStoreData ->
                    val fileUri = mediaStoreData.media_path
                    val file = File(fileUri)
                    mViewModel.uploadFile(file)
                }
        })
    }

    private fun fetchFromViewModel() {
        mViewModel.allDoubts.observe(this, Observer {
            mDoubtList.clear()
            mDoubtList = it
            Collections.reverse(mDoubtList)
            adapter?.updateList(mDoubtList)
            recycler_view.scrollToPosition(mDoubtList.size - 1)
        })

        mViewModel.doubt.observe(this, Observer {
            mDoubtList.add(it)
            adapter?.updateList(mDoubtList)
            recycler_view.scrollToPosition(mDoubtList.size - 1)
        })

        mViewModel.uploadFileUrl.observe(this, Observer {
            createDoubt(it)
        })

        mViewModel.getAllDocDoubts(mSubjectId, mVideoId)
    }

    private fun getYouTubeId(youTubeUrl: String): String {
        val pattern = "(?<=youtu.be/|watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*"
        val compiledPattern = Pattern.compile(pattern)
        val matcher = compiledPattern.matcher(youTubeUrl)
        return if (matcher.find()) {
            matcher.group()
        } else {
            "error"
        }
    }

    override fun onBackPressed() {
        if(fullscreen){
            exitFullScreen()
        }else{
            if (fullScreenHelper?.isFullScreen != true) {
                webView?.destroy()
                super.onBackPressed()
            } else {
                exitFullScreenMode()
            }
        }

    }

    override fun onYouTubePlayerEnterFullScreen() {
        super.onYouTubePlayerEnterFullScreen()
        toolbar.visibility = View.GONE
        layout_chat.visibility = View.GONE
        layout_btn.visibility = View.GONE
        back.visibility = View.GONE
    }

    override fun onYouTubePlayerExitFullScreen() {
        super.onYouTubePlayerExitFullScreen()
        toolbar.visibility = View.VISIBLE
        back.visibility = View.VISIBLE

        if(mPreference.isStudent){
           /* layout_chat.visibility = View.VISIBLE
            layout_btn.visibility = View.VISIBLE*/
        }
    }

    private fun getVideoUrl(): String {
        val url = if (mUrl.contains("vimeo.com")) {
            "https://kotafactory.learnoma.in/video/webview/" + mUrl.subSequence(
                mUrl.lastIndexOf("/") + 1,
                mUrl.length
            ).toString()
        } else {
            mUrl
        }
        val str = "<style>\n" +
                ".video-container { \n" +
                "position: relative; \n" +
                "padding-bottom: 56.25%; \n" +
                "padding-top: 35px; \n" +
                "height: 0; \n" +
                "overflow: hidden; \n" +
                "}\n" +
                ".video-container iframe { \n" +
                "position: absolute; \n" +
                "top:0; \n" +
                "left: 0; \n" +
                "width: 100%; \n" +
                "height: 100%; \n" +
                "}\n" +
                "</style>\n" +
                "<div class=\"video-container\">\n" +
                "    <iframe src=\"$url\" allowfullscreen=\"\" frameborder=\"0\">\n" +
                "    </iframe>\n" +
                "</div>"
        Log.d("WebViewAct", "str $url ")
        return mUrl
    }

    private fun createDoubt(url: String = "") {
        val map = JsonObject()
        map.addProperty("subject_id", mSubjectId)
        map.addProperty("grade_id", mPreference.gradeId)
        map.addProperty("comments", etMsg.text.toString().trim())
        map.addProperty("document_library_id", mVideoId)
        map.addProperty("src_url", url)
        map.addProperty("method", "")
        if (mPreference.isStudent) {
            map.addProperty("school_id", getStudentData(mPreference).school_id)
            map.addProperty("student_id", getStudentData(mPreference).id)
            map.addProperty("commenter_type", TYPE_STUDENT)
            map.addProperty("commenter_id", getStudentData(mPreference).id)
        }
        if (url == "") {
            map.addProperty("attachment_type", TYPE_TEXT)
        } else {
            map.addProperty("attachment_type", TYPE_IMAGE)
        }
        mViewModel.createDoubt(map)
    }


    private fun loadYouTubeThumbnail(name:String,mCtx:Context)
    {
        val imagePath = "https://img.youtube.com/vi/$name/maxresdefault.jpg"

        Glide.with(mCtx)
            .load(imagePath) // image url
            .into(video_thumbnail_image_view)
    }

    fun onYouTubeClick(v:View)
    {
        val intent = Intent(this, YouTubeActivity::class.java)
        intent.putExtra("videoId",getYouTubeId(mUrl))
        startActivity(intent)
        overridePendingTransition(0,0)
    }


    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onResume() {
        super.onResume()
    }
    override fun onPause() {
        super.onPause()
    }


}