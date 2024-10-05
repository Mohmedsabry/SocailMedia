package com.example.socailmedia.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ChatDto(
    val message: String,
    val user: UserDto,
    val time: Long
)
