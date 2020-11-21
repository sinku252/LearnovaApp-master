package com.sambhav.tws

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.downloader.PRDownloader
import com.downloader.PRDownloaderConfig
import com.sambhav.tws.di.module.appModule
import com.sambhav.tws.di.module.myViewModel
import com.sambhav.tws.di.module.networkModule
import com.sambhav.tws.ui.home.doubt.AllDoubtActivity
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
/*import us.zoom.sdk.ZoomSDK
import us.zoom.sdk.ZoomSDKInitParams
import us.zoom.sdk.ZoomSDKInitializeListener*/


class MyApp : MultiDexApplication(), Application.ActivityLifecycleCallbacks {

    override fun onActivityPaused(p0: Activity) {

    }

    override fun onActivityStarted(p0: Activity) {
        Log.e("ASDASDASDASDASD","onActivityStarted"+(p0 is AllDoubtActivity).toString())
        if (p0 is AllDoubtActivity)
            isAppInForground = true
    }

    override fun onActivityDestroyed(p0: Activity) {
        Log.e("ASDASDASDASDASD","onActivityDestroyed"+(p0 is AllDoubtActivity).toString())
        isAppInForground = false
    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
    }

    override fun onActivityStopped(p0: Activity) {
        Log.e("ASDASDASDASDASD","onActivityStopped"+(p0 is AllDoubtActivity).toString())
       // isAppInForground = false
    }

    override fun onActivityCreated(p0: Activity, p1: Bundle?) {
    }

    override fun onActivityResumed(p0: Activity) {
    }

    companion object {
        var isAppInForground: Boolean = false
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(this)
        startKoin {
            androidLogger()
            androidContext(this@MyApp)
            modules(appModule, networkModule, myViewModel)
        }

        //initializeSdk(this);

        val config = PRDownloaderConfig.newBuilder()
            .setDatabaseEnabled(true)
            .build()
        PRDownloader.initialize(this, config)

        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

    }

   /* fun initializeSdk(context: Context?) {
        val sdk = ZoomSDK.getInstance()
        // TODO: For the purpose of this demo app, we are storing the credentials in the client app itself. However, you should not use hard-coded values for your key/secret in your app in production.
        val params = ZoomSDKInitParams()
        params.appKey = getString(R.string.zoom_sdk_key) // TODO: Retrieve your SDK key and enter it here
        params.appSecret = getString(R.string.zoom_sdk_secret) // TODO: Retrieve your SDK secret and enter it here
        params.domain = "zoom.us"
        params.enableLog = true
        // TODO: Add functionality to this listener (e.g. logs for debugging)
        val listener: ZoomSDKInitializeListener = object : ZoomSDKInitializeListener {
            *//**
             * @param errorCode [us.zoom.sdk.ZoomError.ZOOM_ERROR_SUCCESS] if the SDK has been initialized successfully.
             *//*
            override fun onZoomSDKInitializeResult(
                errorCode: Int,
                internalErrorCode: Int
            ) {
            }

            override fun onZoomAuthIdentityExpired() {}
        }
        sdk.initialize(context, listener, params)
    }*/


}