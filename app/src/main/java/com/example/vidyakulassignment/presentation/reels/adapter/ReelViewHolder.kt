package com.example.vidyakulassignment.presentation.reels.adapter

import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.vidyakulassignment.R
import com.example.vidyakulassignment.data.model.Reel
import com.example.vidyakulassignment.databinding.ReelsItemLayoutBinding
import com.example.vidyakulassignment.utils.exoManagerUtils.ExoPlayerManger
import com.github.rubensousa.previewseekbar.PreviewBar
import com.github.rubensousa.previewseekbar.PreviewLoader
import com.github.rubensousa.previewseekbar.animator.PreviewFadeAnimator
import com.github.rubensousa.previewseekbar.media3.PreviewTimeBar

@OptIn(UnstableApi::class)
class ReelViewHolder(
    private val binding: ReelsItemLayoutBinding,
) : RecyclerView.ViewHolder(binding.root), PreviewLoader, PreviewBar.OnScrubListener {
    private val previewTimeBar: PreviewTimeBar = binding.videoView.findViewById(R.id.exo_progress)
    private val previewImage: ImageView = binding.videoView.findViewById(R.id.imageView)
    private var mediaUrl = ""
    private lateinit var player: ExoPlayer

    private val logTag = "ReelViewHolder"

    init {
        previewTimeBar.addOnScrubListener(this)
        previewTimeBar.setPreviewLoader(this)

        Log.e(logTag, "Hash Code: ${ExoPlayerManger.getExoPlayer().hashCode()}")
        Log.e(logTag, "Media Url: $mediaUrl")
        Log.e(logTag, "Position: $bindingAdapterPosition")
    }

    fun bindData(data: Reel) {
        previewTimeBar.setPreviewAnimator(PreviewFadeAnimator())
        binding.videoView.showController()
        this.mediaUrl = data.reelUrl

        binding.videoView.player = ExoPlayerManger.getExoPlayer()
        ExoPlayerManger.setMediaItem(data.reelUrl)

        binding.reelUserName.text = data.reelInfo.username
        Glide.with(binding.userImage).load(data.reelInfo.profilePicUrl)
            .placeholder(R.drawable.avatar).error(R.drawable.avatar).into(binding.userImage)

        binding.commentCount.text = data.reelInfo.comments.toString()
        binding.likeCount.text = data.reelInfo.likes.toString()

        binding.reelDesc.text = data.reelInfo.description
    }


    override fun loadPreview(currentPosition: Long, max: Long) {
        if (ExoPlayerManger.getExoPlayer().isPlaying) {
            ExoPlayerManger.getExoPlayer().playWhenReady = false
        }
        val options = RequestOptions().frame(currentPosition * 1000)
        Glide.with(previewImage)
            .load(mediaUrl)
            .apply(options)
            .into(previewImage)
    }

    override fun onScrubStart(previewBar: PreviewBar?) {
        ExoPlayerManger.getExoPlayer().playWhenReady = false
        binding.reelInfoLayout.visibility = View.INVISIBLE
    }

    override fun onScrubMove(previewBar: PreviewBar?, progress: Int, fromUser: Boolean) {
    }

    override fun onScrubStop(previewBar: PreviewBar?) {
        ExoPlayerManger.getExoPlayer().playWhenReady = true
        binding.reelInfoLayout.visibility = View.VISIBLE
    }

}