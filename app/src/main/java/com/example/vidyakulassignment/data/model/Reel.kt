package com.example.vidyakulassignment.data.model

import java.util.UUID

data class Reel(
    val reelUrl: String,
    val isFollowed: Boolean,
    val reelInfo: ReelInfo
)

data class ReelInfo(
    val username: String,
    val profilePicUrl: String,
    val description: String? = null,
    val isLiked: Boolean,
    val likes: Int,
    val comments: Int,
    val audio: String = "$username • Original Audio",
    val audioPicUrl: String = profilePicUrl,
    val filter: String? = null,
    val location: String? = null,
    val taggedPeople: List<String>? = null,
    val id: String = UUID.randomUUID().toString()
)