package com.example.socailmedia.presentation.friends

import com.example.socailmedia.domain.model.User

sealed interface FriendsEvents {
    data class Accept(val user: User):FriendsEvents
    data class Reject(val user: User):FriendsEvents
    data object Refresh:FriendsEvents
}