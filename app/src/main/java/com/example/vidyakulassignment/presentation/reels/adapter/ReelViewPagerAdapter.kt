package com.example.vidyakulassignment.presentation.reels.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.media3.ui.PlayerView
import androidx.recyclerview.widget.RecyclerView
import com.example.vidyakulassignment.R
import com.example.vidyakulassignment.databinding.ReelsItemLayoutBinding
import com.github.rubensousa.previewseekbar.media3.PreviewTimeBar

class ReelViewPagerAdapter(private val list: List<String>) :
    RecyclerView.Adapter<ReelViewHolder>() {

    init {
        hasStableIds()
    }

    interface ReelViewPagerAdapterInteraction {
        fun onNavigateToNewReel(
            videoPlayer: PlayerView,
            previewTimeBar: PreviewTimeBar,
            previewImage: ImageView,
            mediaPath: String
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReelViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ReelsItemLayoutBinding.inflate(inflater, parent, false)
        return ReelViewHolder(binding = binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onBindViewHolder(holder: ReelViewHolder, position: Int) {
//        if (!ExoManger.getExoPlayer().isPlaying){
//            holder.bindData(mediaUrl = list[holder.bindingAdapterPosition])
//        }
    }

    private lateinit var nextHolder: ReelViewHolder
    private lateinit var currentHolder: ReelViewHolder

    override fun onViewAttachedToWindow(holder: ReelViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.bindData(mediaUrl = list[holder.bindingAdapterPosition])
//        if (ExoManger.getExoPlayer().isPlaying){
//            nextHolder = holder
//        } else{
//            currentHolder = holder
//            currentHolder.bindData(mediaUrl = list[holder.bindingAdapterPosition])
//        }
//        Toast.makeText(holder.itemView.context,"On Attach", Toast.LENGTH_SHORT).show()
    }

    override fun onViewDetachedFromWindow(holder: ReelViewHolder) {
        super.onViewDetachedFromWindow(holder)
        val videoPlayer = holder.itemView.findViewById<PlayerView>(R.id.video_view)
        videoPlayer.player = null
//        nextHolder.bindData(mediaUrl = list[nextHolder.bindingAdapterPosition])
//        Toast.makeText(holder.itemView.context,"On Detach", Toast.LENGTH_SHORT).show()
    }
}