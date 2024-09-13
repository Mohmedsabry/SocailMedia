package com.example.socailmedia.data.remote.dto

data class SharePostDto(
    val postId: Int,
    val editor: String,
    val content: String,
    val isShared: Boolean = false
)
