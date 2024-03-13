package com.example.vidyakulassignment.presentation

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.Util
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.DefaultTimeBar
import com.example.vidyakulassignment.R
import com.example.vidyakulassignment.databinding.ActivityMainBinding
import com.example.vidyakulassignment.domain.model.ReelsLocalPathList
import com.example.vidyakulassignment.presentation.reels.ReelViewModel
import com.example.vidyakulassignment.presentation.reels.ReelsActivity


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var player: ExoPlayer
    private lateinit var previewFrameLayout: FrameLayout
    private lateinit var scrubbingPreview: ImageView
    private lateinit var exo_progress: DefaultTimeBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        previewFrameLayout = binding.videoView.findViewById(R.id.previewFrameLayout)
        scrubbingPreview = binding.videoView.findViewById(R.id.scrubbingPreview)
        exo_progress = binding.videoView.findViewById(R.id.exo_progress)

        binding.nxtBtn.setOnClickListener {
            startActivity(Intent(this, NewActivity::class.java))
        }

        binding.nxtBtnReels.setOnClickListener {
            startActivity(Intent(this, ReelsActivity::class.java))
        }
    }

    @OptIn(UnstableApi::class)
    private fun initializePlayer() {
        player = ExoPlayer.Builder(this).build()
        binding.videoView.player = player
        player.repeatMode = Player.REPEAT_MODE_ONE

//        https://user-images.githubusercontent.com/90382113/170887700-e405c71e-fe31-458d-8572-aea2e801eecc.mp4

        val videoUri = Uri.parse("android.resource://" + packageName + "/" + R.raw.shivali_moments)
//        val mediaItem = MediaItem.fromUri(videoUri)
        val mediaItemList = ArrayList<MediaItem>()
        for (data in ReelsLocalPathList.list){
            val mediaItem = MediaItem.fromUri(data)
            mediaItemList.add(mediaItem)
        }
        player.setMediaItems(mediaItemList)
        player.prepare()
//        player.play()
    }

    @OptIn(UnstableApi::class)
    public override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23) {
            initializePlayer()
        }
    }

    @OptIn(UnstableApi::class)
    public override fun onResume() {
        super.onResume()
//        hideSystemUi()
        if ((Util.SDK_INT <= 23)) {
            initializePlayer()
        }
    }

    @SuppressLint("InlinedApi")
    private fun hideSystemUi() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, binding.videoView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    @OptIn(UnstableApi::class)
    public override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) {
            releasePlayer()
        }
    }


    @OptIn(UnstableApi::class)
    public override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) {
            releasePlayer()
        }
    }

    private var playWhenReady = true
    private var mediaItemIndex = 0
    private var playbackPosition = 0L

    private fun releasePlayer() {
        player.let { exoPlayer ->
            playbackPosition = exoPlayer.currentPosition
            mediaItemIndex = exoPlayer.currentMediaItemIndex
            playWhenReady = exoPlayer.playWhenReady
            exoPlayer.release()
        }
    }
}