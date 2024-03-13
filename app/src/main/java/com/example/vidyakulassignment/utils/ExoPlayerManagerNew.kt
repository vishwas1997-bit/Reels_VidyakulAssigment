package com.example.vidyakulassignment.utils

import android.net.Uri
import android.widget.ImageView
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.Util
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.vidyakulassignment.R
import com.github.rubensousa.previewseekbar.PreviewBar
import com.github.rubensousa.previewseekbar.PreviewLoader
import com.github.rubensousa.previewseekbar.media3.PreviewTimeBar

@OptIn(UnstableApi::class)
class ExoPlayerManagerNew : PreviewLoader, PreviewBar.OnScrubListener {


    private var playerView: PlayerView? = null
    private var player: ExoPlayer? = null
    private var previewTimeBar: PreviewTimeBar? = null
    private var imageView: ImageView? = null
    private var resumeVideoOnPreviewStop = false
    private val eventListener: Player.Listener = @UnstableApi object : Player.Listener {
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            if (playbackState == Player.STATE_READY && playWhenReady) {
                previewTimeBar!!.hidePreview()
            }
        }
    }

    fun initializePlayer(
        playerView: PlayerView,
        previewTimeBar: PreviewTimeBar,
        imageView: ImageView
    ) {
//        releaseViews()
        this.playerView = playerView
        this.imageView = imageView
        this.previewTimeBar = previewTimeBar
        this.previewTimeBar!!.addOnScrubListener(this)
        this.previewTimeBar!!.setPreviewLoader(this)
        resumeVideoOnPreviewStop = true
    }

    fun onStart() {
        if (Util.SDK_INT > 23) {
            createPlayers()
        }
    }

    fun onResume() {
        if (Util.SDK_INT <= 23) {
            createPlayers()
        }
    }

    fun onPause() {
        if (Util.SDK_INT <= 23) {
            releasePlayers()
        }
    }

    fun onStop() {
        if (Util.SDK_INT > 23) {
            releasePlayers()
        }
    }

    private fun releasePlayers() {
        if (player != null) {
            player!!.release()
            player = null
        }
    }

    private fun createPlayers() {
        if (player != null) {
            player!!.release()
        }
        player = createPlayer()
        playerView?.player = player
        playerView?.controllerShowTimeoutMs = 0
        playerView?.controllerHideOnTouch = false
    }

    private fun createPlayer(): ExoPlayer {
        val player = ExoPlayer.Builder(playerView!!.context)
            .build()
        player.playWhenReady = true
        player.addListener(eventListener)
        player.repeatMode = Player.REPEAT_MODE_ONE
        return player
    }


    override fun loadPreview(currentPosition: Long, max: Long) {
        if (player!!.isPlaying) {
            player!!.playWhenReady = false
        }
        val thumbnailUrl = "android.resource://com.example.vidyakulassignment/" + R.raw.feel_god
        val options = RequestOptions().frame(currentPosition * 1000)
        Glide.with(imageView!!)
            .load(thumbnailUrl)
            .apply(options)
            .into(imageView!!)
    }

    fun addMediaItem(mediaItemsPath: String) {
        createPlayers()
        player?.setMediaItem(
            MediaItem.Builder()
                .setUri(Uri.parse(mediaItemsPath))
                .build()
        )
        player?.prepare()
        player?.play()
    }

    override fun onScrubStart(previewBar: PreviewBar?) {
        player!!.playWhenReady = false
    }

    override fun onScrubMove(previewBar: PreviewBar?, progress: Int, fromUser: Boolean) {

    }

    override fun onScrubStop(previewBar: PreviewBar?) {
        if (resumeVideoOnPreviewStop) {
            player!!.playWhenReady = true
        }
    }
}