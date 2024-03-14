package com.example.vidyakulassignment.data.repositoryImpl

import com.example.vidyakulassignment.data.model.Reel
import com.example.vidyakulassignment.data.model.ReelsModel
import com.example.vidyakulassignment.domain.repository.ReelsRepository

class ReelsRepositoryImpl: ReelsRepository {
    override fun getReels(): List<Reel> {
        return ReelsModel.reelsWithDiffUrl
    }
}