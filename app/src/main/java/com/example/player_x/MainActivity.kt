package com.example.player_x

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.player_x.databinding.ActivityMainBinding
import java.io.File

class MainActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityMainBinding

    companion object
    {
        lateinit var videoList: ArrayList<VideoClass>
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setTheme(R.style.coolPinkNav)
        setContentView(binding.root)
        if (requestRunTimePermission())
        {
            videoList = getAllVideos()
            setFragment(VideoFragment())
        }
        binding.bottomNav.setOnItemSelectedListener {
           when(it.itemId) {
               R.id.videoView -> setFragment(VideoFragment())
               R.id.folderView -> setFragment(FolderFragment())
           }
               return@setOnItemSelectedListener true
        }

    }
    private fun setFragment(fragment: Fragment)
    {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer,fragment)
        transaction.disallowAddToBackStack()
        transaction.commit()
    }
    private fun requestRunTimePermission():Boolean =
        if(ActivityCompat.checkSelfPermission(this,WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(WRITE_EXTERNAL_STORAGE),13)
            false
        } else
            true

    override fun onRequestPermissionsResult(requestCode: Int,permissions: Array<out String>,grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 13) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show()
            else
                ActivityCompat.requestPermissions(this, arrayOf(WRITE_EXTERNAL_STORAGE),13)
        }
    }

    private fun getAllVideos() : ArrayList<VideoClass>
    {
        val tempList = ArrayList<VideoClass>()
        val projection = arrayOf(MediaStore.Video.Media.TITLE, MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media._ID,MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Video.Media.DATA, MediaStore.Video.Media.DATE_ADDED,
            MediaStore.Video.Media.DURATION)
        val cursor = this.contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection,
            null,null, MediaStore.Video.Media.DATE_ADDED +" DESC ")
        if(cursor!=null)
            if (cursor.moveToNext())
            {
                do
                {
                    val titleC = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE))
                    val idC = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID))
                    val folderC = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME))
                    val sizeC = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE))
                    val pathC = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA))
                    val durationC = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)).toLong()
                    try
                    {
                        val file = File(pathC)
                        val artUriC = Uri.fromFile(file)
                        val video = VideoClass(title= titleC,id = idC, folderName = folderC,
                            path =  pathC, artUri = artUriC, duration = durationC, size = sizeC)
                        if (file.exists()) tempList.add(video)
                    }catch (e:Exception){}

                } while (cursor.moveToNext())
            }
        cursor?.close()
        return tempList
    }
}