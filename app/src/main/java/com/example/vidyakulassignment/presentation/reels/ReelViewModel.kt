package com.example.vidyakulassignment.presentation.reels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vidyakulassignment.data.model.Reel
import com.example.vidyakulassignment.data.repositoryImpl.ReelsRepositoryImpl

class ReelViewModel(private val reelsRepository: ReelsRepositoryImpl) : ViewModel() {

    private val reelsMutableLiveData = MutableLiveData<ArrayList<Reel>>()
    val reelsLiveData = reelsMutableLiveData

    fun getReels() {
        reelsMutableLiveData.value = ArrayList(reelsRepository.getReels())
    }
}