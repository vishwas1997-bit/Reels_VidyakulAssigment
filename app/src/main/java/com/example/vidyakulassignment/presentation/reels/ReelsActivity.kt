package com.example.vidyakulassignment.presentation.reels

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.media3.ui.PlayerView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.vidyakulassignment.R
import com.example.vidyakulassignment.databinding.ActivityReelsBinding
import com.example.vidyakulassignment.domain.model.ReelsLocalPathList
import com.example.vidyakulassignment.presentation.reels.adapter.ReelViewPagerAdapter
import com.example.vidyakulassignment.utils.ExoManger
import com.github.rubensousa.previewseekbar.animator.PreviewFadeAnimator
import com.github.rubensousa.previewseekbar.media3.PreviewTimeBar


class ReelsActivity : AppCompatActivity(), ReelViewPagerAdapter.ReelViewPagerAdapterInteraction {

    private var _binding: ActivityReelsBinding? = null
    private val binding get() = _binding!!

    private val logTag = "ReelsActivity_Log"
    private lateinit var reelViewPagerAdapter: ReelViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_reels)

        ExoManger.createPlayer(this)
        initAdapter()

        binding.reelsViewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
//                val viewHolder = (binding.reelsViewPager[0] as RecyclerView).findViewHolderForAdapterPosition(
//                    position
//                )
//
//                Toast.makeText(applicationContext, ""+position, Toast.LENGTH_SHORT).show()
//                val videoPlayer = viewHolder?.itemView?.findViewById<PlayerView>(R.id.video_view)
////                val previewTimeBar: PreviewTimeBar = videoPlayer!!.findViewById(R.id.exo_progress)
////                val previewImage: ImageView = videoPlayer.findViewById(R.id.imageView)
//                videoPlayer?.player = ExoManger.getExoPlayer()
//                ExoManger.addMediaItem(ReelsLocalPathList.list[position])
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                Log.e(logTag, "State: $state")
            }
        })
    }

    private fun initAdapter() {
        reelViewPagerAdapter = ReelViewPagerAdapter(ReelsLocalPathList.list)
        binding.reelsViewPager.adapter = reelViewPagerAdapter
    }

    override fun onResume() {
        super.onResume()
        ExoManger.onResume()
    }

    override fun onPause() {
        super.onPause()
        ExoManger.onPause()
    }

    override fun onStop() {
        super.onStop()
        ExoManger.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        ExoManger.onDestroy()
    }


    override fun onNavigateToNewReel(
        videoPlayer: PlayerView,
        previewTimeBar: PreviewTimeBar,
        previewImage: ImageView,
        mediaPath: String
    ) {
//        exoPlayerManager?.addMediaItem(mediaPath)
    }
}