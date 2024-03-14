package com.example.vidyakulassignment.presentation.reels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vidyakulassignment.data.repositoryImpl.ReelsRepositoryImpl

class ReelsViewModelFactory (private val repository: ReelsRepositoryImpl) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReelViewModel::class.java)) {
            return ReelViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}