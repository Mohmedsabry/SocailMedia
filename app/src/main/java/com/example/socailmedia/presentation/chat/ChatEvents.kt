package com.example.socailmedia.presentation.chat

sealed interface ChatEvents {
    data class OnTypingMessage(val message: String) : ChatEvents
    data object OnSendMessage : ChatEvents
}