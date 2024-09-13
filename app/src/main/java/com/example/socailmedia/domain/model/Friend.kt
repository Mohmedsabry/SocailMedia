package com.example.socailmedia.domain.model

data class Friend(
    val user: User,
    val statues: String,
    val isClose: Boolean,
    val dateOfAcceptFriend: String?
)
