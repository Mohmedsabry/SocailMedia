package com.example.socailmedia.presentation.username

data class UserNameState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val userName: String = "",
    val navToChat: Boolean = false
)
