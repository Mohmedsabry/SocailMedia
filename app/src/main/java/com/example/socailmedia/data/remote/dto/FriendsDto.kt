package com.example.socailmedia.data.remote.dto

data class FriendsDto(
    val user: UserDto,
    val statues: String,
    val isClose: Boolean,
    val dateOfAcceptFriend: String?
)
