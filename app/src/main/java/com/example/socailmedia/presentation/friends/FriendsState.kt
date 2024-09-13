package com.example.socailmedia.presentation.friends

import com.example.socailmedia.domain.model.User


data class FriendsState(
    val error: String? = null,
    val isLoading: Boolean = false,
    val users: List<User> = emptyList(),
    val email: String = ""
)
