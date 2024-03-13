package com.example.vidyakulassignment.presentation.reels.adapter

import android.net.Uri
import android.util.Log
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.vidyakulassignment.R
import com.example.vidyakulassignment.databinding.ReelsItemLayoutBinding
import com.example.vidyakulassignment.domain.model.ReelsLocalPathList
import com.example.vidyakulassignment.utils.ExoManger
import com.github.rubensousa.previewseekbar.PreviewBar
import com.github.rubensousa.previewseekbar.PreviewLoader
import com.github.rubensousa.previewseekbar.animator.PreviewFadeAnimator
import com.github.rubensousa.previewseekbar.media3.PreviewTimeBar

class ReelViewHolder(
    private val binding: ReelsItemLayoutBinding,
//    private val exoManger: ExoManger = ExoManger()
) : RecyclerView.ViewHolder(binding.root), PreviewLoader, PreviewBar.OnScrubListener {
    private val previewTimeBar: PreviewTimeBar = binding.videoView.findViewById(R.id.exo_progress)
    private val previewImage: ImageView = binding.videoView.findViewById(R.id.imageView)
    private var mediaUrl = ""
    private lateinit var player: ExoPlayer

    private val logTag = "ReelViewHolder"

    init {
        previewTimeBar.addOnScrubListener(this)
        previewTimeBar.setPreviewLoader(this)
    }

    fun bindData(mediaUrl: String) {
        previewTimeBar.setPreviewAnimator(PreviewFadeAnimator())
        this.mediaUrl = mediaUrl

        Log.e(logTag, "Hash Code: ${ExoManger.getExoPlayer().hashCode()}")
        Log.e(logTag, "Media Url: $mediaUrl")

        Log.e(logTag, "Position: $bindingAdapterPosition")
        binding.videoView.player = ExoManger.getExoPlayer()
        ExoManger.addMediaItem(mediaUrl)
    }


    override fun loadPreview(currentPosition: Long, max: Long) {
        if (ExoManger.getExoPlayer().isPlaying) {
            ExoManger.getExoPlayer().playWhenReady = false
        }
        val options = RequestOptions().frame(currentPosition * 1000)
        Glide.with(previewImage)
            .load(mediaUrl)
            .apply(options)
            .into(previewImage)
    }

    override fun onScrubStart(previewBar: PreviewBar?) {
        ExoManger.getExoPlayer().playWhenReady = false
    }

    override fun onScrubMove(previewBar: PreviewBar?, progress: Int, fromUser: Boolean) {
    }

    override fun onScrubStop(previewBar: PreviewBar?) {
        ExoManger.getExoPlayer().playWhenReady = true
    }

}