package com.example.socailmedia.data.mapper

import com.example.socailmedia.data.remote.dto.CommentDto
import com.example.socailmedia.data.remote.dto.PostDto
import com.example.socailmedia.domain.model.Comment
import com.example.socailmedia.domain.model.Post
import com.example.socailmedia.util.toPost

fun PostDto.toPost() = Post(
    editor = this.user.toUser(),
    postId = postId,
    likes = likes,
    content = content,
    comments = comments,
    shares = shares,
    type = type.toPost(),
    isLiked = isLiked,
    isShared = isShared,
    sharedContent = sharedContent
)

fun CommentDto.toComment() = Comment(
    user = this.user.toUser(),
    comment = comment,
)