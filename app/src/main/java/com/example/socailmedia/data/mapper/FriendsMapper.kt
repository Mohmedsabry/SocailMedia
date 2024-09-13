package com.example.socailmedia.data.mapper

import com.example.socailmedia.data.remote.dto.FriendsDto
import com.example.socailmedia.domain.model.Friend

fun FriendsDto.toFriend(): Friend =
    Friend(
        this.user.toUser(),
        this.statues,
        this.isClose,
        this.dateOfAcceptFriend
    )