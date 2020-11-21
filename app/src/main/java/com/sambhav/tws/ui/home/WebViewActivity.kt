package com.sambhav.tws.ui.home

import android.graphics.Bitmap
import android.graphics.Color
import android.net.http.SslError
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.*
import androidx.databinding.DataBindingUtil
import com.sambhav.tws.R
import com.sambhav.tws.base.BaseActivity
import com.sambhav.tws.databinding.ActivityWebViewBinding
import com.sambhav.tws.ui.home.videos.viewModel.VideoViewModel
import com.sambhav.tws.utils.*
import kotlinx.android.synthetic.main.activity_web_view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class WebViewActivity : BaseActivity() {

    private lateinit var mBinding : ActivityWebViewBinding
    private val mViewModel: VideoViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_web_view)
        mBinding.viewModel = mViewModel
        mViewModel.progress.set(true)

        init()
        val extension = intent.getStringExtra(EXTRA_KEY_EXTENSION)
        val url = intent.getStringExtra(EXTRA_KEY_URL)
        tvTitle.text = intent.getStringExtra(EXTRA_KEY_TITLE)

        if (extension == TYPE_IMAGE){
            imageView.visibility = View.VISIBLE
            setImage(imageView,url)
            mViewModel.progress.set(false)
        }else if(extension == FT_PDF){
            webView.visibility = View.VISIBLE
            webView.loadUrl("http://docs.google.com/gview?embedded=true&url=$url")
            Log.d("WebViewActivity","https://drive.google.com/viewerng/viewer?embedded=true&url=$url")
        }else {
            webView.visibility = View.VISIBLE
            webView.loadUrl("https://drive.google.com/viewerng/viewer?embedded=true&url=$url")
        }
    }

    override fun onBackPressed() {
        webView.destroy()
        super.onBackPressed()
    }
    private fun init(){
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
                webView.loadUrl("javascript:(function() { " +
                        "document.querySelector('[role=\"toolbar\"]').remove();})()");
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
    }

}
