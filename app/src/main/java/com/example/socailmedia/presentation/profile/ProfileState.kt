package com.example.socailmedia.presentation.profile

import com.example.socailmedia.domain.model.Comment
import com.example.socailmedia.domain.model.Post
import com.example.socailmedia.domain.model.User
import com.example.socailmedia.util.ConstObject.PUBLIC

data class ProfileState(
    val user: User = User(),
    val baseUser: User = User(),
    val posts: List<Post> = emptyList(),
    val error: String? = null,
    val friends: Int = 0,
    val close: Int = 0,
    val isClose: Boolean = false,
    val isFriend: Boolean = false,
    val hisAccount: Boolean = false,
    val showComment: Boolean = false,
    val comment: String = "",
    val showShareDialog: Boolean = false,
    val isEnabled: Boolean = true,
    val comments: List<Comment> = emptyList(),
    val selectedPost: Int = 0,
    val sharedContent: String = "",
    val sharedType: String = PUBLIC,
)
