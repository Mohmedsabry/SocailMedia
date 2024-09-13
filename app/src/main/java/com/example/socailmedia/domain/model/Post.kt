package com.example.socailmedia.domain.model

import com.example.socailmedia.util.ConstObject

data class Post(
    val postId: Int = 0,
    val editor: User = User(),
    val likes: Int = 0,
    val comments: Int = 0,
    val shares: Int = 0,
    val content: String = "",
    val type: TypeOfPost = TypeOfPost.PUBLIC,
    val isShared: Boolean = false,
    val isLiked: Boolean = false,
    val sharedContent: String = ""
)

enum class TypeOfPost(val title: String) {
    PUBLIC(ConstObject.PUBLIC),
    PRIVATE(ConstObject.PRIVATE),
    ONLY_FRIENDS(ConstObject.ONLY_FRIENDS)
}
