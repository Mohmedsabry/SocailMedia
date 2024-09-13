package com.example.socailmedia.presentation.register

data class RegisterState(
    val error: String? = null,
    val isLoading: Boolean = false,
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val phoneNumber: String = "",
    val gender: String = "",
    val dateOfBirth: String = "",
    val emails: List<String> = listOf(),
    val registered: Boolean = false,
    val registeredClicked: Boolean = false,
)
