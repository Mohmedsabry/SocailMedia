package com.example.socailmedia.data.remote.dto

data class CommentDto(
    val user: UserDto,
    val comment: String,
)