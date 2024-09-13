package com.example.socailmedia.presentation.details

import com.example.socailmedia.domain.model.Post

sealed interface DetailsEvents {
    data class OnLiked(val isLiked: Boolean) : DetailsEvents
    data object OnShare : DetailsEvents
    data class OnProfileClicked(val post: Post) : DetailsEvents
    data class OnTyping(val comment: String) : DetailsEvents
    data class OnTypeChange(val type: String) : DetailsEvents
    data class OnContentChange(val type: String) : DetailsEvents
    data object OnSendComment : DetailsEvents
    data object OnSharePost : DetailsEvents
    data object OnDismiss : DetailsEvents
}