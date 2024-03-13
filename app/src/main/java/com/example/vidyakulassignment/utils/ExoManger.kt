package com.example.vidyakulassignment.utils

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.Util
import androidx.media3.exoplayer.ExoPlayer


@OptIn(UnstableApi::class)
object ExoManger {

    private val logTag = "ExoManger"

    private var _player: ExoPlayer? = null
    private val player get() = _player!!

    private val eventListener: Player.Listener = @UnstableApi object : Player.Listener {
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            if (playbackState == Player.STATE_READY && playWhenReady) {
            }
        }

        override fun onPlayerError(error: PlaybackException) {
            super.onPlayerError(error)
            Log.e(logTag, error.localizedMessage!!)
        }
    }

    fun onResume() {
        if (Util.SDK_INT > 23) {
            player.mediaItemCount.let {
                player.play()
            }
        }
    }

    fun onPause() {
        if (Util.SDK_INT <= 23) {
            if (player.isPlaying) {
                player.pause()
            }
        }
    }

    fun onStop() {
        if (Util.SDK_INT > 23) {
            if (player.isPlaying) {
                player.pause()
            }
        }
    }

    fun onDestroy() {
        releasePlayers()
    }

    private fun releasePlayers() {
        player.release()
        _player = null
    }

    fun getExoPlayer(): ExoPlayer {
        return player
    }

    fun createPlayer(context: Context) {
        if (_player == null) {
            _player = ExoPlayer.Builder(context)
                .build()
            player.playWhenReady = true
            player.prepare()
            player.addListener(eventListener)
            player.repeatMode = Player.REPEAT_MODE_ONE
        }
    }

    fun addMediaItem(mediaItemsPath: String) {
        player.setMediaItem(
            MediaItem.Builder()
                .setUri(Uri.parse(mediaItemsPath))
                .build(), true
        )
        player.play()
    }
}