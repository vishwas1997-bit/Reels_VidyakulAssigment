package com.example.vidyakulassignment.utils.exoManagerUtils

import android.content.Context
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.Util
import androidx.media3.database.ExoDatabaseProvider
import androidx.media3.datasource.DefaultDataSourceFactory
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import java.io.File


@OptIn(UnstableApi::class)
object ExoPlayerManger {

    private const val logTag = "ExoManger"


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
            val cacheDir = File(context.cacheDir, "media_cache")
            val maxCacheSize = 100 * 1024 * 1024 // 100 MB
            val cache = SimpleCache(
                cacheDir,
                LeastRecentlyUsedCacheEvictor(maxCacheSize.toLong()),
                ExoDatabaseProvider(context)
            )

            val httpDataSourceFactory = DefaultHttpDataSource.Factory()
                .setAllowCrossProtocolRedirects(true)

            val defaultDataSourceFactory = DefaultDataSourceFactory(
                context, httpDataSourceFactory
            )

            val cacheDataSourceFactory = CacheDataSource.Factory()
                .setCache(cache)
                .setUpstreamDataSourceFactory(defaultDataSourceFactory)
                .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)

            _player = ExoPlayer.Builder(context)
                .setMediaSourceFactory(DefaultMediaSourceFactory(cacheDataSourceFactory))
                .build()
            player.playWhenReady = true
            player.prepare()
            player.addListener(eventListener)
            player.repeatMode = Player.REPEAT_MODE_ONE
        }
    }

    //    MimeTypes.APPLICATION_M3U8
    //    MimeTypes.VIDEO_MP4
    fun setMediaItem(mediaItemsPath: String) {
        val mimeTypes = if (getMimeType(mediaItemsPath) == MimeTypes.VIDEO_MP4) {
            MimeTypes.VIDEO_MP4
        } else {
            MimeTypes.APPLICATION_M3U8
        }

        val mediaItem = MediaItem.Builder()
            .setUri(Uri.parse(mediaItemsPath))
            .setMimeType(
                mimeTypes
            )
            .build()
        player.setMediaItem(
            mediaItem, true
        )
        player.play()
    }

    private fun getMimeType(url: String?): String {
        var type = ""
        val extension = MimeTypeMap.getFileExtensionFromUrl(url)
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)!!
        }
        return type
    }
}