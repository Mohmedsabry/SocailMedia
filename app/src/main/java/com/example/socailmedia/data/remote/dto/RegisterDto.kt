package com.example.socailmedia.data.remote.dto

data class RegisterDto(
    val name: String,
    val email: String,
    val password: String,
    val phoneNumber: String,
    val gender: String,
    val dateOfBirth: String
)
