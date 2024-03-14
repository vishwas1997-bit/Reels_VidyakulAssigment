package com.example.vidyakulassignment.domain.repository

import com.example.vidyakulassignment.data.model.Reel

interface ReelsRepository {
    fun getReels(): List<Reel>
}