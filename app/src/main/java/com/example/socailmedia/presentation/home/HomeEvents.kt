package com.example.socailmedia.presentation.home

import com.example.socailmedia.domain.model.Post
import com.example.socailmedia.domain.model.User

sealed interface HomeEvents {
    data class GetUserFromNav(val user: User) : HomeEvents
    data object GetAllPosts : HomeEvents
    data class OnTypingComment(val comment: String) : HomeEvents
    data class OnSendComment(val postId: Int) : HomeEvents
    data class OnLikeClicked(val postId: Int, val isLiked: Boolean, val isShared: Boolean) :
        HomeEvents

    data class OnShareClicked(val postId: Int) : HomeEvents
    data object OnRefresh : HomeEvents
    data class OnNavigate(val navigationItem: NavigationItem) : HomeEvents
    data object OnAddPost : HomeEvents
    data class OnNavigateIntoScreen(val post: Post) : HomeEvents
    data class OnCommentClicked(val postId: Int) : HomeEvents
    data object OnDismissBottomSheet : HomeEvents
    data object OnDismissDialog : HomeEvents
    data object OnShare : HomeEvents
    data class OnTypeChange(val type: String) : HomeEvents
    data class OnContentChange(val content: String) : HomeEvents
}