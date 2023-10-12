package com.example.player_x

import android.app.DownloadManager.Request
import android.content.Context
import android.content.Intent
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.player_x.databinding.VideosLayoutBinding


class VideoAdapter(
    private val context: Context,
    private val videoList: ArrayList<VideoClass>
) : RecyclerView.Adapter<VideoAdapter.ViewHolder>()
{

    class ViewHolder(binding : VideosLayoutBinding) : RecyclerView.ViewHolder(binding.root)
    {
        val title = binding.videoName
        val folder = binding.folderName
        val duration = binding.duration
        val image = binding.videoImg
        val root = binding.root
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(VideosLayoutBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = videoList[position].title
        holder.folder.text = videoList[position].folderName
        holder.duration.text = DateUtils.formatElapsedTime(videoList[position].duration/1000)
        Glide.with(context)
            .asBitmap()
            .load(videoList[position].artUri)
            .apply(RequestOptions().placeholder(R.mipmap.ic_video_player).centerCrop())
            .into(holder.image)
        holder.root.setOnClickListener {
            sendIntent(pos = position,ref = "AllVideos")
        }
    }

    override fun getItemCount(): Int {
        return videoList.size
    }

    private  fun sendIntent(pos:Int, ref:String)
    {
        PlayerActivity.position= pos
        val intent = Intent(context,PlayerActivity::class.java)
        intent.putExtra("class",ref)
        ContextCompat.startActivity(context,intent,null)
    }


}