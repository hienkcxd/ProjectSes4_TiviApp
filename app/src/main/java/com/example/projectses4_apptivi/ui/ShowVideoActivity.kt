package com.example.projectses4_apptivi.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.example.projectses4_apptivi.R
import com.example.projectses4_apptivi.databinding.ActivityMainBinding
import com.example.projectses4_apptivi.databinding.ActivityShowVideoBinding
import com.example.projectses4_apptivi.io.ManageSharePreference.JwtSharePreference
import com.example.projectses4_apptivi.io.service.DeviceService
import com.example.projectses4_apptivi.model.DetailDeviceJson
import com.example.projectses4_apptivi.model.DeviceModel
import com.example.projectses4_apptivi.model.FileStorage
import com.example.projectses4_apptivi.model.VideoModel
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import retrofit2.awaitResponse
import java.io.File
class ShowVideoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShowVideoBinding
    private lateinit var sharedPreferences: JwtSharePreference
    private var storage : FirebaseStorage = FirebaseStorage.getInstance()
    private var storageRef = storage.reference
    private var videoList:List<String> = listOf()
    private var uriVideoList : List<String> = listOf()
    private var videoPlayList : List<FileStorage> = listOf()
    private var videoName:String = "bot_chien_taky.mp4"
    private var uriVideo:String = "https://firebasestorage.googleapis.com/v0/b/dsproject-f2707.appspot.com/o/Videos%2Fbot_chien_taky.mp4?alt=media&token=f0fbacdc-f110-4132-b2a6-3dafc0f4d1e2"
    private var currentVideo = 0
    private var sizeListVideo = 0
    private val deviceService: DeviceService by lazy {
        DeviceService.create(getJwt().toString())
    }
    private var count = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = JwtSharePreference(this)

//        showVideo()
        lifecycleScope.launch{
            //lấy danh sách filestorage của device
            getVideoOfDevice()
            sizeListVideo = videoPlayList.size
            showVideo(sizeListVideo)
        }
    }
    private fun getJwt():String?{
        return sharedPreferences.getJwt()
    }
    private fun getDeviceId():String?{
        return sharedPreferences.getDeviceId()
    }
    private fun showVideo(index : Int){
        if (index < videoPlayList.size) {
            count++
            val video = videoPlayList[index]
            val videoRef = storageRef.child("Videos/${video.fileName}")
            val videoUrlTask = videoRef.downloadUrl
            videoUrlTask.addOnSuccessListener { uri ->
                // Khởi tạo ExoPlayer
                val player = SimpleExoPlayer.Builder(this).build()
                // Thiết lập PlayerView để hiển thị video
                val playerView = binding.vvShowVideo
                playerView.player = player

                // Tạo MediaSource từ đường dẫn uri của video
                val mediaItem = MediaItem.fromUri(uri)
                val mediaSource = ProgressiveMediaSource.Factory(DefaultDataSourceFactory(this, "exoplayer"))
                    .createMediaSource(mediaItem)
                // Phát video
                player.prepare(mediaSource)
                player.play()
                // Chờ đến khi video phát xong
                player.addListener(object : Player.Listener  {
                    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                        if (playbackState == Player.STATE_ENDED) {
                            player.repeatMode = Player.REPEAT_MODE_OFF
                            player.release()
                            showVideo(index + 1)
                        }
                    }
                })
                playerView.useController = false
                playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                player.playWhenReady = true
                Log.i("jwt_Phat_Video","phat lan $count")
            }
        } else {
            // Quay lại phát video đầu tiên khi đã phát hết danh sách video
            Log.i("jwt_Phat_Video","phat lan $count")
            showVideo(0)
        }
    }
    private suspend fun getVideoOfDevice(){
        try {
            val callGroup = deviceService.getDeviceDetail(getJwt().toString(), getDeviceId().toString())
            val response = callGroup.awaitResponse()
            if (response.isSuccessful) {
                val deviceResponse: DetailDeviceJson? = response.body()
                Log.i("jwt", "jwt lay duoc ${deviceResponse.toString()} ")
                if (deviceResponse == null) {
                    Toast.makeText(applicationContext, "không có dữ liệu!!!", Toast.LENGTH_LONG).show()
                    Log.i("jwt_ShowVideo", "group trống")
                    return
                } else {
                    videoPlayList = deviceResponse.fileStorage
                    Log.i("jwt_ShowVideo", "danh sach uri video: ${videoPlayList.toString()}")
                }
            } else {
                Log.i("jwt_ShowVideo", "khong lay duoc du lieu")
                Log.i("jwt_ShowVideo", sharedPreferences.getDeviceId().toString())
                Toast.makeText(applicationContext, "error 403!!!", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Log.e("jwt_ShowVideo", e.message ?: "Unknown error")
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
        finish()
    }
}

