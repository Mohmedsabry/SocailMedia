package com.example.socailmedia.presentation.profile

import com.example.socailmedia.domain.model.Post

sealed interface ProfileEvents {
    data class OnLikeClicked(
        val post: Post,
        val isLiked: Boolean
    ) : ProfileEvents

    data class OnShareClicked(val post: Post) : ProfileEvents
    data class OnCommentClicked(val post: Post) : ProfileEvents
    data object OnAddFriend : ProfileEvents
    data class OnRemoveFriend(val state: String) : ProfileEvents
    data object OnAddClose : ProfileEvents
    data class OnTypingComment(
        val comment: String
    ) : ProfileEvents

    data class OnTypeChange(
        val type: String
    ) : ProfileEvents

    data class OnSendComment(val selectedPost: Int) : ProfileEvents
    data object OnShare : ProfileEvents
    data object OnDismissDialog : ProfileEvents
    data object OnDismissBottomSheet : ProfileEvents
    data class OnTypingSharedContent(val content:String):ProfileEvents
}