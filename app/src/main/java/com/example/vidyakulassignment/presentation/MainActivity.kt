package com.example.vidyakulassignment.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaFormat
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import androidx.media3.common.Format
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.Util
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.video.VideoFrameMetadataListener
import androidx.media3.ui.DefaultTimeBar
import androidx.media3.ui.TimeBar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.vidyakulassignment.R
import com.example.vidyakulassignment.databinding.ActivityMainBinding
import com.example.vidyakulassignment.utils.GlideThumbnailTransformation


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
    }

    @OptIn(UnstableApi::class)
    private fun initializePlayer() {
        player = ExoPlayer.Builder(this).build()
        binding.videoView.player = player
        player.repeatMode = Player.REPEAT_MODE_ONE

//        https://user-images.githubusercontent.com/90382113/170887700-e405c71e-fe31-458d-8572-aea2e801eecc.mp4

        val videoUri = Uri.parse("android.resource://" + packageName + "/" + R.raw.shivali_moments)
        val mediaItem = MediaItem.fromUri(videoUri)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()

        player.setVideoFrameMetadataListener(object : VideoFrameMetadataListener {
            override fun onVideoFrameAboutToBeRendered(
                presentationTimeUs: Long,
                releaseTimeNs: Long,
                format: Format,
                mediaFormat: MediaFormat?
            ) {
//                val frame = getVideoFrame(this@MainActivity, videoUri, presentationTimeUs) ?: return
//                binding.preview.setImageBitmap(frame)
            }

        })



        player.addListener(object : Player.Listener {
            override fun onPlayerError(error: PlaybackException) {
                Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT).show()
                super.onPlayerError(error)
            }
        })

//        initPreview()
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

    fun getVideoFrame(context: Context, uri: Uri, time: Long): Bitmap? {
        var bitmap: Bitmap? = null
        val retriever = MediaMetadataRetriever()
        try {
            retriever.setDataSource(context, uri)
            bitmap = retriever.getFrameAtTime(time)
        } catch (ex: RuntimeException) {
            ex.printStackTrace()
        } finally {
            try {
                retriever.release()
            } catch (ex: RuntimeException) {
                ex.printStackTrace()
            }
        }
        return bitmap
    }

    @OptIn(UnstableApi::class)
    private fun initPreview() {
        val thumbnailUrl =
            "https://bitdash-a.akamaihd.net/content/MI201109210084_1/thumbnails/f08e80da-bf1d-4e3d-8899-f0f6155f6efa.jpg"
        player.addListener(object : TimeBar.OnScrubListener, Player.Listener {
            override fun onScrubStart(timeBar: TimeBar, position: Long) {

            }

            override fun onScrubMove(timeBar: TimeBar, position: Long) {
                previewFrameLayout.visibility = View.VISIBLE
                val targetX = updatePreviewX(position.toInt(), player.duration.toInt())
                previewFrameLayout.x = targetX.toFloat()
                Glide.with(scrubbingPreview)
                    .load(thumbnailUrl)
                    .transform(GlideThumbnailTransformation(position))
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(scrubbingPreview)
            }

            override fun onScrubStop(timeBar: TimeBar, position: Long, canceled: Boolean) {
                previewFrameLayout.visibility = View.INVISIBLE
            }

        })

    }


    @OptIn(UnstableApi::class)
    private fun updatePreviewX(progress: Int, max: Int): Int {
        if (max == 0) {
            return 0
        }

        val parent = previewFrameLayout.parent as ViewGroup
        val layoutParams = previewFrameLayout.layoutParams as ViewGroup.MarginLayoutParams
        val offset = progress.toFloat() / max
        val minimumX: Int = previewFrameLayout.left
        val maximumX = (parent.width - parent.paddingRight - layoutParams.rightMargin)

// We remove the padding of the scrubbing, if you have a custom size juste use dimen to calculate this
        val previewPaddingRadius: Int =
            dpToPx(resources.displayMetrics, DefaultTimeBar.DEFAULT_SCRUBBER_DRAGGED_SIZE_DP).div(2)
        val previewLeftX = (exo_progress as View).left.toFloat()
        val previewRightX = (exo_progress as View).right.toFloat()
        val previewSeekBarStartX: Float = previewLeftX + previewPaddingRadius
        val previewSeekBarEndX: Float = previewRightX - previewPaddingRadius
        val currentX = (previewSeekBarStartX + (previewSeekBarEndX - previewSeekBarStartX) * offset)
        val startX: Float = currentX - previewFrameLayout.width / 2f
        val endX: Float = startX + previewFrameLayout.width

        // Clamp the moves
        return if (startX >= minimumX && endX <= maximumX) {
            startX.toInt()
        } else if (startX < minimumX) {
            minimumX
        } else {
            maximumX - previewFrameLayout.width
        }
    }

    private fun dpToPx(displayMetrics: DisplayMetrics, dps: Int): Int {
        return (dps * displayMetrics.density).toInt()
    }
}