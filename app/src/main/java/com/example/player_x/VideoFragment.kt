package com.example.player_x

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.player_x.databinding.FragmentVideoBinding

class VideoFragment : Fragment()
{
      override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
          val view = inflater.inflate(R.layout.fragment_video, container, false)
          val binding = FragmentVideoBinding.bind(view)


          binding.videoRV.setHasFixedSize(true)
          binding.videoRV.setItemViewCacheSize(10)
          binding.videoRV.layoutManager = LinearLayoutManager(requireContext())
          binding.videoRV.adapter =VideoAdapter(requireContext(),MainActivity.videoList)
          return view
    }
}

data class VideoClass(val id:String, val title:String, val duration:Long = 0,
    val folderName:String,val size:String, val path:String, val artUri:Uri)