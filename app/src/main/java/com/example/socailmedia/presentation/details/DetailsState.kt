package com.example.socailmedia.presentation.details

import com.example.socailmedia.domain.model.Comment
import com.example.socailmedia.domain.model.Post
import com.example.socailmedia.domain.model.User
import com.example.socailmedia.util.ConstObject.PUBLIC

data class DetailsState(
    val error: String? = null,
    val isLoading: Boolean = false,
    val post: Post = Post(),
    val comments: List<Comment> = emptyList(),
    val comment: String = "",
    val hisPost: Boolean = false,
    val user: User = User(),
    val showShare: Boolean = false,
    val shareContent: String = "",
    val type: String = PUBLIC,
    val baseUser: User = User()
)
