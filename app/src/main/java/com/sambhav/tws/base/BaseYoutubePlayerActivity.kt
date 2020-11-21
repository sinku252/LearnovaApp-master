package com.sambhav.tws.base

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import com.sambhav.tws.R
import com.sambhav.tws.utils.FullScreenHelper
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

abstract class BaseYoutubePlayerActivity :
   BaseActivity(),
    YouTubePlayerFullScreenListener {

    val mTag = "BaseYoutubePlayer"
    private var youTubePlayerView: YouTubePlayerView? = null
    var mYouTubePlayer: YouTubePlayer? = null
    var fullScreenHelper : FullScreenHelper?= null
    private var mVideoId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fullScreenHelper = FullScreenHelper(this)
    }

    fun initYoutube(videoId: String) {
        mVideoId = videoId
        youTubePlayerView =
            window?.decorView?.rootView?.findViewById<YouTubePlayerView?>(R.id.youtubeView)
        Log.d("initYoutube","youTubePlayerView $youTubePlayerView ")
        youTubePlayerView?.let { ytView ->
            lifecycle.addObserver(ytView)

            ytView.initialize(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    Log.d(mTag, "onReady ")
                    mYouTubePlayer = youTubePlayer
                    youTubePlayer.loadVideo(mVideoId, 0f)
                    youTubePlayer.play()
                }

                override fun onError(
                    youTubePlayer: YouTubePlayer,
                    error: PlayerConstants.PlayerError
                ) {
                    super.onError(youTubePlayer, error)
                    Log.d(mTag, "onError " + error.name)
                }

            })
            ytView.addFullScreenListener(this)
        }

    }

    override fun onYouTubePlayerEnterFullScreen() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        fullScreenHelper?.enterFullScreen()
    }

    override fun onYouTubePlayerExitFullScreen() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        fullScreenHelper?.exitFullScreen()
    }

    fun exitFullScreenMode() {
        youTubePlayerView?.exitFullScreen()
        onYouTubePlayerExitFullScreen()
    }


}
