package com.example.socailmedia.data.remote.dto

data class PostUpload(
    val editor: String,
    val content: String,
    val type: Int = 0
)
