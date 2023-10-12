package com.example.player_x

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.player_x.databinding.ActivityPlayerBinding
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.Timeline.Window
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import java.util.SimpleTimeZone

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding

    companion object
    {
        lateinit var player:SimpleExoPlayer
        lateinit var playerList: ArrayList<VideoClass>
        var position :Int =-1
        private var isFullScreen:Boolean = false
    }

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(android.view.Window.FEATURE_NO_TITLE)
        window.attributes.layoutInDisplayCutoutMode =
            WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window,binding.root).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior=
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
        setTheme(R.style.playerTheme)
        initializeLayout()
        initializeBinding()
    }

    private fun initializeLayout() {
        when(intent.getStringExtra("class"))
        {
            "AllVideos"->{
                playerList = ArrayList()
                playerList.addAll(MainActivity.videoList)
            }
        }
        createPlayer()
    }

    private fun initializeBinding()
    {
        binding.backBtn.setOnClickListener {
            finish()
        }
        binding.playPauseBtn.setOnClickListener {
            if (player.isPlaying) pauseVideo()
            else playVideo()
        }

        binding.playPauseBtn.setOnClickListener {
            if (isFullScreen)
            {
                isFullScreen = false
                playFullScreen(enable = false)
            }
            else
            {
                isFullScreen = true
                playFullScreen(enable = true)
            }
        }

    }
    private fun createPlayer() {
        binding.videoTitle.text = playerList[position].title
        binding.videoTitle.isSelected = true
        player = SimpleExoPlayer.Builder(this).build()
        binding.exoPlayer.player = player
        val mediaItem  = MediaItem.fromUri(MainActivity.videoList[position].artUri)
        player.setMediaItem(mediaItem)
        player.prepare()
        playVideo()
        playFullScreen(isFullScreen)
    }

    private fun playVideo()
    {
        binding.playPauseBtn.setImageResource(R.drawable.pause_icon)
        player.play()
    }

    private fun pauseVideo()
    {
        binding.playPauseBtn.setImageResource(R.drawable.play_icon)
        player.pause()
    }

    private fun playFullScreen(enable:Boolean)
    {
        if (enable)
        {
            binding.exoPlayer.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
            player.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
            binding.fullScreen.setImageResource(R.drawable.fullscreen_exit)
        }
        else
        {
            binding.exoPlayer.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
            player.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT
            binding.fullScreen.setImageResource(R.drawable.fullscreen_on)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }
}