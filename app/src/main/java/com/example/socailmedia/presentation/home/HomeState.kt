package com.example.socailmedia.presentation.home

import com.example.socailmedia.domain.model.Comment
import com.example.socailmedia.domain.model.Post
import com.example.socailmedia.domain.model.User
import com.example.socailmedia.util.ConstObject.PUBLIC

data class HomeState(
    val error: String? = null,
    val isLoading: Boolean = false,
    val user: User = User(),
    val posts: List<Post> = listOf(),
    val comment: String = "",
    val showBottomSheet: Boolean = false,
    val comments: List<Comment> = emptyList(),
    val selectedPost: Int = 0,
    val sharedContent: String = "",
    val sharedType: String = PUBLIC,
    val showShareDialog: Boolean = false
)
