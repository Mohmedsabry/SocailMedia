package com.example.socailmedia.domain.repositories

import com.example.socailmedia.domain.model.Comment
import com.example.socailmedia.domain.model.Friend
import com.example.socailmedia.domain.model.FriendShip
import com.example.socailmedia.domain.model.Post
import com.example.socailmedia.domain.util.Result
import com.example.socailmedia.util.GlobalError

interface Repository {
    suspend fun getAllPosts(
    ): Result<List<Post>, GlobalError>

    suspend fun addPost(
        editor: String,
        content: String,
        type: Int
    ): Result<Unit, GlobalError>

    suspend fun deletePost(
        postId: Int
    ): Result<Unit, GlobalError>

    suspend fun updatePost(
        postId: Int,
        content: String
    ): Result<Unit, GlobalError>

    suspend fun getSpecificPost(
        postId: Int,
        isShared: Boolean
    ): Result<Post, GlobalError>

    suspend fun likePost(
        postId: Int,
        isLiked: Boolean,
        isShared: Boolean
    ): Result<Unit, GlobalError>

    suspend fun sharePost(
        postId: Int,
        content: String
    ): Result<Unit, GlobalError>

    suspend fun deleteShare(
        postId: Int,
        email: String
    ): Result<Unit, GlobalError>

    suspend fun updateShare(
        postId: Int,
        email: String
    ): Result<Unit, GlobalError>

    suspend fun addComment(
        postId: Int,
        comment: String,
        isShared: Boolean
    ): Result<Unit, GlobalError>

    suspend fun deleteComment(
        postId: Int,
        email: String
    ): Result<Unit, GlobalError>

    suspend fun updateComment(
        postId: Int,
        comment: String,
        email: String
    ): Result<Unit, GlobalError>

    suspend fun getComments(
        postId: Int,
    ): Result<List<Comment>, GlobalError>

    suspend fun getMyPosts(
        email: String
    ): Result<List<Post>, GlobalError>

    suspend fun getMyFriends(
        email: String
    ): Result<List<Friend>, GlobalError>

    suspend fun getMyFriendsRequest(
        email: String
    ): Result<List<Friend>, GlobalError>

    suspend fun updateFriendShipStatue(
        user1: String,
        statues: String
    ): Result<Unit, GlobalError>

    suspend fun addToClose(
        user1: String,
        close: Boolean
    ): Result<Unit, GlobalError>
    suspend fun addFriend(
        user1: String,
    ): Result<Unit, GlobalError>
    suspend fun friendShipState(
        email: String
    ):Result<FriendShip,GlobalError>
}