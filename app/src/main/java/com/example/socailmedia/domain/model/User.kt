package com.example.socailmedia.domain.model

data class User(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val age: Float = 0.0f,
    val phoneNumber: String = "",
    val gender: String = "",
    val dateOfBirth: String = ""
)
