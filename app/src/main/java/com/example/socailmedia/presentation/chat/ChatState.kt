package com.example.socailmedia.presentation.chat

import com.example.socailmedia.domain.model.Chat

data class ChatState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val messages: List<Chat> = emptyList(),
    val sender: String = "",
    val receiver: String = "",
    val message: String = "",
)
