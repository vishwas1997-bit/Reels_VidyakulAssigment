package com.example.vidyakulassignment.presentation.reels.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.media3.ui.PlayerView
import androidx.recyclerview.widget.RecyclerView
import com.example.vidyakulassignment.R
import com.example.vidyakulassignment.data.model.Reel
import com.example.vidyakulassignment.databinding.ReelsItemLayoutBinding

class ReelViewPagerAdapter :
    RecyclerView.Adapter<ReelViewHolder>() {

    private var reelsList = ArrayList<Reel>()

    fun submitList(reelsList: ArrayList<Reel>) {
        this.reelsList = reelsList
        notifyDataSetChanged()
    }

    init {
        hasStableIds()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReelViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ReelsItemLayoutBinding.inflate(inflater, parent, false)
        return ReelViewHolder(binding = binding)
    }

    override fun getItemCount(): Int {
        return reelsList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onBindViewHolder(holder: ReelViewHolder, position: Int) {

    }

    private lateinit var nextHolder: ReelViewHolder
    private lateinit var currentHolder: ReelViewHolder

    override fun onViewAttachedToWindow(holder: ReelViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.bindData(data = reelsList[holder.bindingAdapterPosition])
    }

    override fun onViewDetachedFromWindow(holder: ReelViewHolder) {
        super.onViewDetachedFromWindow(holder)
        val videoPlayer = holder.itemView.findViewById<PlayerView>(R.id.video_view)
        videoPlayer.player = null
    }
}