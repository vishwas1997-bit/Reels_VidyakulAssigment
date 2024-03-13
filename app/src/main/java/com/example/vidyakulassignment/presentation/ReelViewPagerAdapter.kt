package com.example.vidyakulassignment.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vidyakulassignment.databinding.ReelsItemLayoutBinding

class ReelViewPagerAdapter : RecyclerView.Adapter<ReelViewPagerAdapter.ReelViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReelViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ReelsItemLayoutBinding.inflate(inflater, parent, false)
        return ReelViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: ReelViewHolder, position: Int) {

    }

    open class ReelViewHolder(itemView: ReelsItemLayoutBinding) : RecyclerView.ViewHolder(itemView.root) {

    }
}