package com.example.socailmedia.presentation.login

sealed interface LoginEvent {
    data class OnTypingEmail(val email: String) : LoginEvent
    data class OnTypingPassword(val password: String) : LoginEvent
    object OnLoginClicked : LoginEvent
}