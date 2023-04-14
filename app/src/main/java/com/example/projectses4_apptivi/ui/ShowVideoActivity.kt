package com.example.projectses4_apptivi.ui

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.FileProvider
import com.example.projectses4_apptivi.R
import com.example.projectses4_apptivi.databinding.ActivityMainBinding
import com.example.projectses4_apptivi.databinding.ActivityShowVideoBinding
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.firebase.storage.FirebaseStorage
import java.io.File
class ShowVideoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShowVideoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val videoRef = storageRef.child("Videos/video_kem_taco")
        val videoUrlTask = videoRef.downloadUrl

        videoUrlTask.addOnSuccessListener { uri ->
            val playerView = binding.vvShowVideo
            val player = SimpleExoPlayer.Builder(this).build()
            playerView.player = player

            val uri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/dsproject-f2707.appspot.com/o/Videos%2Fvideo_kem_taco?alt=media&token=db08e382-1728-4c5a-bfe2-93ca5c29b9b0")
            val mediaItem = MediaItem.fromUri(uri)
            val mediaSource = ProgressiveMediaSource.Factory(DefaultDataSourceFactory(this, "exoplayer"))
                .createMediaSource(mediaItem)
            player.prepare(mediaSource)
            player.playWhenReady = true
            player.repeatMode = Player.REPEAT_MODE_ONE
            playerView.player = player
            playerView.useController = false
            playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
            player.playWhenReady = true
        }
    }
}

