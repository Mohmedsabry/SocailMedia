package com.example.socailmedia.domain.model

data class Chat(
    val user: User,
    val time: String,
    val message: String
)
