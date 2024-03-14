package com.example.vidyakulassignment.presentation.reels

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.vidyakulassignment.R
import com.example.vidyakulassignment.data.repositoryImpl.ReelsRepositoryImpl
import com.example.vidyakulassignment.databinding.ActivityReelsBinding
import com.example.vidyakulassignment.presentation.reels.adapter.ReelViewPagerAdapter
import com.example.vidyakulassignment.utils.exoManagerUtils.ExoPlayerManger


class ReelsActivity : AppCompatActivity() {

    private var _binding: ActivityReelsBinding? = null
    private val binding get() = _binding!!

    private val logTag = "ReelsActivity_Log"
    private lateinit var reelViewPagerAdapter: ReelViewPagerAdapter
    private lateinit var reelViewModel: ReelViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_reels)
        ExoPlayerManger.createPlayer(this)
        initActivity()
        uiObserver()

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

    private fun initActivity() {
        reelViewPagerAdapter = ReelViewPagerAdapter()
        binding.reelsViewPager.adapter = reelViewPagerAdapter
        reelViewModel = ViewModelProvider(
            this,
            ReelsViewModelFactory(ReelsRepositoryImpl())
        )[ReelViewModel::class.java]
        reelViewModel.getReels()
    }

    private fun uiObserver() {
        reelViewModel.reelsLiveData.observe(this) { reels ->
            reelViewPagerAdapter.submitList(reels)
        }
    }

    override fun onResume() {
        super.onResume()
        ExoPlayerManger.onResume()
    }

    override fun onPause() {
        super.onPause()
        ExoPlayerManger.onPause()
    }

    override fun onStop() {
        super.onStop()
        ExoPlayerManger.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        ExoPlayerManger.onDestroy()
    }
}