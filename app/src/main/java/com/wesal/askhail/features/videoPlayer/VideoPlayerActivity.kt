package com.wesal.askhail.features.videoPlayer

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import cn.jzvd.Jzvd
import com.wesal.askhail.R
import com.wesal.askhail.databinding.ActivityVideoPlayerBinding

class VideoPlayerActivity : AppCompatActivity() {
    lateinit var binding: ActivityVideoPlayerBinding;

    private var url: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoPlayerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setContentView(R.layout.activity_video_player)
        url = intent.getStringExtra("url")
        binding.videoPlayer.fullscreenButton.visibility = View.GONE
        binding.videoPlayer.setUp(
            url
            , " ", Jzvd.SCREEN_NORMAL
        )
        binding.videoPlayer.startVideo()
    }

    override fun onPause() {
        super.onPause()
        try {
            Jzvd.resetAllVideos()
        } catch (e: Exception) {
        }
    }
}