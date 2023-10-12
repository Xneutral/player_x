package com.example.player_x

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.player_x.databinding.ActivityPlayerBinding
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import java.util.SimpleTimeZone

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding

    companion object
    {
        lateinit var player:SimpleExoPlayer
        lateinit var playerList: ArrayList<VideoClass>
        var position :Int =-1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeLayout()
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

    private fun createPlayer() {
        binding.videoTitle.text = playerList[position].title
        binding.videoTitle.isSelected = true
        player = SimpleExoPlayer.Builder(this).build()
        binding.exoPlayer.player = player
        val mediaItem  = MediaItem.fromUri(MainActivity.videoList[position].artUri)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }
}