package com.example.socailmedia.presentation.username

sealed interface UserNameEvents {
    data object OnJoinChat : UserNameEvents
    data class OnTyping(val text: String) : UserNameEvents
}