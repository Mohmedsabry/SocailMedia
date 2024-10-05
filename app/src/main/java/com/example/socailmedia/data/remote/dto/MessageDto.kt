package com.example.socailmedia.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class MessageDto(
    val text: String,
    val userName: String,
    val time: Long,
    val id: String,
)
