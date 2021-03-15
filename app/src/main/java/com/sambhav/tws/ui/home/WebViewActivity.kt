package com.sambhav.tws.ui.home

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.*
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import com.sambhav.tws.BuildConfig
import com.sambhav.tws.R
import com.sambhav.tws.base.BaseActivity
import com.sambhav.tws.databinding.ActivityWebViewBinding
import com.sambhav.tws.ui.home.videos.viewModel.VideoViewModel
import com.sambhav.tws.utils.*
import isDocDownload
import kotlinx.android.synthetic.main.activity_web_view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class WebViewActivity : BaseActivity() {

    private lateinit var mBinding : ActivityWebViewBinding
    private val mViewModel: VideoViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_web_view)
        mBinding.viewModel = mViewModel


       // init()
        val extension = intent.getStringExtra(EXTRA_KEY_EXTENSION)
        val url = intent.getStringExtra(EXTRA_KEY_URL)
        tvTitle.text = intent.getStringExtra(EXTRA_KEY_TITLE)
        var filePath = intent.getStringExtra(EXTRA_KEY_FILE_PATH)


        if (extension == TYPE_IMAGE){
            imageView.visibility = View.VISIBLE
            setImage(imageView, url)
            mViewModel.progress.set(false)
        }
        else
        {
           // Log.e("gagag", filePath+"."+extension)
            showPdf(filePath+"."+extension.toLowerCase())
        }
    /*else if(extension == FT_PDF){
            webView.visibility = View.VISIBLE
            webView.loadUrl("http://docs.google.com/gview?embedded=true&url=$url")
            Log.d("WebViewActivity","https://drive.google.com/viewerng/viewer?embedded=true&url=$url")
        }else {
            webView.visibility = View.VISIBLE
            webView.loadUrl("https://drive.google.com/viewerng/viewer?embedded=true&url=$url")
        }*/

        back.setOnClickListener(CustomClickListener {
            onBackPressed()
        })
    }

    override fun onBackPressed() {
     //   webView.destroy()
        super.onBackPressed()
    }

    private fun showPdf(file: String)
    {

        pdfView.visibility = View.VISIBLE
        var uri: Uri
        if (Build.VERSION.SDK_INT > 20) {
            uri = FileProvider.getUriForFile(
                this, BuildConfig.APPLICATION_ID + ".provider",
                File(file)
            )
        } else {
            uri = Uri.fromFile(File(file))
        }

       // isDocDownload()
       // var kkss:File=FileUtils.getPathWithoutFilename(File(file))
        pdfView.fromUri(uri)
            .defaultPage(0)
            .spacing(10)
            .load()

        /*pdfView.fromUri(mImageCaptureUri)
            .load()!!*/
    }

   /* private fun init(){
        webView.setBackgroundColor(Color.TRANSPARENT)
        webView.settings.javaScriptEnabled = true
        webView.webChromeClient = WebChromeClient()
        webView.webViewClient = object : WebViewClient(){
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                Log.e("WebViewActivity","shouldOverrideUrlLoading : "+(view==null).toString())
                return false
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                Log.e("WebViewActivity","onPageStarted : "+url.toString())
                mViewModel.progress.set(true)
              *//*  webView.loadUrl("javascript:(function() { " +
                        "document.querySelector('[role=\"toolbar\"]').remove();})()");*//*
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                Log.e("WebViewActivity","onPageFinished : "+url.toString())
                webView.loadUrl("javascript:(function() { " +
                        "document.querySelector('[role=\"toolbar\"]').remove();})()");
                mViewModel.progress.set(false)
            }

            override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
                Log.e("WebViewActivity",handler.toString()+error.toString())
                mViewModel.progress.set(false)
                handler.proceed() // Ignore SSL certificate errors
            }
        }

        back.setOnClickListener(CustomClickListener {
            onBackPressed()
        })
    }*/

}
