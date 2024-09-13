package com.example.socailmedia.data.remote.dto

data class PostDto(
    val postId: Int,
    val user: UserDto,
    val likes: Int,
    val comments: Int,
    val shares: Int,
    val content: String,
    val type: String,
    val isShared: Boolean,
    val isLiked: Boolean,
    val sharedContent: String
)
