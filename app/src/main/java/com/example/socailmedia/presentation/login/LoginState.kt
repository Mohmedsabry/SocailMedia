package com.example.socailmedia.presentation.login

import com.example.socailmedia.domain.model.User
import com.example.socailmedia.util.AuthErrors

data class LoginState(
    val error: String? = null,
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val loginSuccessfully: Boolean = false,
    val user: User = User(),
    val errorType: AuthErrors? = null
)
