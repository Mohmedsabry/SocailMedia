package com.example.socailmedia.data.repositories

import com.example.socailmedia.domain.model.Comment
import com.example.socailmedia.domain.model.Friend
import com.example.socailmedia.domain.model.FriendShip
import com.example.socailmedia.domain.model.Post
import com.example.socailmedia.domain.model.User
import com.example.socailmedia.domain.repositories.Repository
import com.example.socailmedia.domain.util.Result
import com.example.socailmedia.util.GlobalError

class FakeRepositoryImplTest() : Repository {
    private var shouldReturnNetworkError: Boolean = false

    fun setError(value: Boolean) {
        shouldReturnNetworkError = value
    }

    override suspend fun getAllPosts(): Result<List<Post>, GlobalError> {
        return if (shouldReturnNetworkError) {
            Result.Failure(GlobalError.UN_KNOWN)
        } else {
            Result.Success(listOf())
        }
    }

    override suspend fun addPost(
        editor: String,
        content: String,
        type: Int
    ): Result<Unit, GlobalError> {
        return if (shouldReturnNetworkError) {
            Result.Failure(GlobalError.UN_KNOWN)
        } else {
            Result.Success(Unit)
        }
    }

    override suspend fun deletePost(
        postId: Int
    ): Result<Unit, GlobalError> {
        return if (shouldReturnNetworkError) {
            Result.Failure(GlobalError.UN_KNOWN)
        } else {
            Result.Success(Unit)
        }
    }

    override suspend fun updatePost(
        postId: Int,
        content: String
    ): Result<Unit, GlobalError> {
        return if (shouldReturnNetworkError) {
            Result.Failure(GlobalError.UN_KNOWN)
        } else {
            Result.Success(Unit)
        }
    }

    override suspend fun getSpecificPost(
        postId: Int,
        isShared: Boolean
    ): Result<Post, GlobalError> {
        return if (shouldReturnNetworkError) {
            Result.Failure(GlobalError.UN_KNOWN)
        } else {
            Result.Success(Post(postId = postId, isShared = isShared))
        }
    }

    override suspend fun likePost(
        postId: Int,
        isLiked: Boolean,
        isShared: Boolean
    ): Result<Unit, GlobalError> {
        return if (shouldReturnNetworkError) {
            Result.Failure(GlobalError.UN_KNOWN)
        } else {
            Result.Success(Unit)
        }
    }

    override suspend fun sharePost(
        postId: Int,
        content: String
    ): Result<Unit, GlobalError> {
        return if (shouldReturnNetworkError) {
            Result.Failure(GlobalError.UN_KNOWN)
        } else {
            Result.Success(Unit)
        }
    }

    override suspend fun deleteShare(
        postId: Int,
        email: String
    ): Result<Unit, GlobalError> {
        return if (shouldReturnNetworkError) {
            Result.Failure(GlobalError.UN_KNOWN)
        } else {
            Result.Success(Unit)
        }
    }

    override suspend fun updateShare(
        postId: Int,
        email: String
    ): Result<Unit, GlobalError> {
        return if (shouldReturnNetworkError) {
            Result.Failure(GlobalError.UN_KNOWN)
        } else {
            Result.Success(Unit)
        }
    }

    override suspend fun addComment(
        postId: Int,
        comment: String,
        isShared: Boolean
    ): Result<Unit, GlobalError> {
        return if (shouldReturnNetworkError) {
            Result.Failure(GlobalError.UN_KNOWN)
        } else {
            Result.Success(Unit)
        }
    }

    override suspend fun deleteComment(
        postId: Int,
        email: String
    ): Result<Unit, GlobalError> {
        return if (shouldReturnNetworkError) {
            Result.Failure(GlobalError.UN_KNOWN)
        } else {
            Result.Success(Unit)
        }
    }

    override suspend fun updateComment(
        postId: Int,
        comment: String,
        email: String
    ): Result<Unit, GlobalError> {
        return if (shouldReturnNetworkError) {
            Result.Failure(GlobalError.UN_KNOWN)
        } else {
            Result.Success(Unit)
        }
    }

    override suspend fun getComments(
        postId: Int
    ): Result<List<Comment>, GlobalError> {
        return if (shouldReturnNetworkError) {
            Result.Failure(GlobalError.UN_KNOWN)
        } else {
            Result.Success(listOf(Comment(User(age = 0f), "")))
        }
    }

    override suspend fun getMyPosts(
        email: String
    ): Result<List<Post>, GlobalError> {
        return if (shouldReturnNetworkError) {
            Result.Failure(GlobalError.UN_KNOWN)
        } else {
            Result.Success(listOf())
        }
    }

    override suspend fun getMyFriends(
        email: String
    ): Result<List<Friend>, GlobalError> {
        return if (shouldReturnNetworkError) {
            Result.Failure(GlobalError.UN_KNOWN)
        } else {
            Result.Success(listOf())
        }
    }

    override suspend fun getMyFriendsRequest(
        email: String
    ): Result<List<Friend>, GlobalError> {
        return if (shouldReturnNetworkError) {
            Result.Failure(GlobalError.UN_KNOWN)
        } else {
            Result.Success(
                listOf(
                    Friend(
                        user = User(email = email),
                        "Pending",
                        true,
                        dateOfAcceptFriend = null
                    )
                )
            )
        }
    }

    override suspend fun updateFriendShipStatue(
        user1: String,
        statues: String
    ): Result<Unit, GlobalError> {
        return if (shouldReturnNetworkError) {
            Result.Failure(GlobalError.UN_KNOWN)
        } else {
            Result.Success(Unit)
        }
    }

    override suspend fun addToClose(
        user1: String,
        close: Boolean
    ): Result<Unit, GlobalError> {
        return if (shouldReturnNetworkError) {
            Result.Failure(GlobalError.UN_KNOWN)
        } else {
            Result.Success(Unit)
        }
    }

    override suspend fun addFriend(
        user1: String
    ): Result<Unit, GlobalError> {
        return if (shouldReturnNetworkError) {
            Result.Failure(GlobalError.UN_KNOWN)
        } else {
            Result.Success(Unit)
        }
    }

    override suspend fun friendShipState(
        email: String
    ): Result<FriendShip, GlobalError> {
        return if (shouldReturnNetworkError) {
            Result.Failure(GlobalError.UN_KNOWN)
        } else {
            Result.Success(FriendShip())
        }
    }
}