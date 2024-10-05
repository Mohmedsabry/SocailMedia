package com.example.socailmedia.data.repositories

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.example.socailmedia.data.mapper.toComment
import com.example.socailmedia.data.mapper.toFriend
import com.example.socailmedia.data.mapper.toPost
import com.example.socailmedia.data.remote.FriendsApi
import com.example.socailmedia.data.remote.PostApi
import com.example.socailmedia.data.remote.dto.PostUpload
import com.example.socailmedia.data.remote.dto.SharePostDto
import com.example.socailmedia.domain.model.Comment
import com.example.socailmedia.domain.model.Friend
import com.example.socailmedia.domain.model.FriendShip
import com.example.socailmedia.domain.model.Post
import com.example.socailmedia.domain.repositories.Repository
import com.example.socailmedia.domain.util.Result
import com.example.socailmedia.util.GlobalError
import com.example.socailmedia.util.getEmail
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val postApi: PostApi,
    @ApplicationContext private val context: Context,
    private val friendsApi: FriendsApi
) : Repository {
    override fun getFileFromContent(uri: Uri): File {
        val cursor = context.contentResolver.query(uri, null, null, null)
        cursor?.moveToFirst()
        val index = cursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        val fileName = index?.let { cursor.getString(it) }
        val file = File(context.cacheDir,fileName!!)
        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)
        cursor.close()
        return file
    }

    override suspend fun uploadImg(
        file: File
    ): Result<Unit, GlobalError> {
        return withContext(Dispatchers.IO) {
            try {
                postApi.uploadImg(
                    MultipartBody.Part.createFormData("fileName", context.getEmail()),
                    MultipartBody.Part.createFormData("file", file.name, file.asRequestBody()),
                )
                Result.Success(Unit)
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Failure(GlobalError.UN_KNOWN)
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Failure(GlobalError.UN_KNOWN)
            }
        }
    }

    override suspend fun getAllPosts():
            Result<List<Post>, GlobalError> {
        return withContext(Dispatchers.IO) {
            try {
                val email = async { context.getEmail() }.await()
                var posts = async { postApi.getAllPosts(email) }.await()
                println(posts)
                posts = posts.filter {
                    it.user.email != email && !it.isLiked
                }
                Result.Success(posts.map { it.toPost() })
            } catch (e: HttpException) {
                e.printStackTrace()
                when (e.code()) {
                    501 -> Result.Failure(GlobalError.INTERNAL_SERVER_ERROR)
                    400 -> Result.Failure(GlobalError.BAD_REQUEST)
                    else -> Result.Failure(GlobalError.UN_KNOWN)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Failure(GlobalError.UN_KNOWN)
            }
        }
    }

    override suspend fun addPost(
        editor: String,
        content: String,
        type: Int
    ): Result<Unit, GlobalError> {
        return withContext(Dispatchers.IO) {
            try {
                val postUpload = PostUpload(editor, content, type)
                postApi.sendPost(postUpload)
                Result.Success(Unit)
            } catch (e: HttpException) {
                e.printStackTrace()
                when (e.code()) {
                    501 -> Result.Failure(GlobalError.INTERNAL_SERVER_ERROR)
                    400 -> Result.Failure(GlobalError.BAD_REQUEST)
                    else -> Result.Failure(GlobalError.UN_KNOWN)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Failure(GlobalError.UN_KNOWN)
            }
        }
    }

    override suspend fun deletePost(
        postId: Int
    ): Result<Unit, GlobalError> {
        return withContext(Dispatchers.IO) {
            try {
                postApi.deletePostById(postId)
                Result.Success(Unit)
            } catch (e: HttpException) {
                e.printStackTrace()
                when (e.code()) {
                    501 -> Result.Failure(GlobalError.INTERNAL_SERVER_ERROR)
                    400 -> Result.Failure(GlobalError.BAD_REQUEST)
                    else -> Result.Failure(GlobalError.UN_KNOWN)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Failure(GlobalError.UN_KNOWN)
            }
        }
    }

    override suspend fun updatePost(
        postId: Int,
        content: String
    ): Result<Unit, GlobalError> {
        return withContext(Dispatchers.IO) {
            try {
                postApi.updatePostById(postId, content)
                Result.Success(Unit)
            } catch (e: HttpException) {
                e.printStackTrace()
                when (e.code()) {
                    501 -> Result.Failure(GlobalError.INTERNAL_SERVER_ERROR)
                    400 -> Result.Failure(GlobalError.BAD_REQUEST)
                    else -> Result.Failure(GlobalError.UN_KNOWN)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Failure(GlobalError.UN_KNOWN)
            }
        }
    }

    override suspend fun getSpecificPost(
        postId: Int,
        isShared: Boolean
    ): Result<Post, GlobalError> {
        return withContext(Dispatchers.IO) {
            try {
                val post = postApi.getPostById(postId, isShared)
                Result.Success(post.toPost())
            } catch (e: HttpException) {
                e.printStackTrace()
                when (e.code()) {
                    501 -> Result.Failure(GlobalError.INTERNAL_SERVER_ERROR)
                    400 -> Result.Failure(GlobalError.BAD_REQUEST)
                    else -> Result.Failure(GlobalError.UN_KNOWN)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Failure(GlobalError.UN_KNOWN)
            }
        }
    }

    override suspend fun likePost(
        postId: Int,
        isLiked: Boolean,
        isShared: Boolean
    ): Result<Unit, GlobalError> {
        return withContext(Dispatchers.IO) {
            try {
                val email = context.getEmail()
                postApi.updatePostLikeStatues(
                    postId, if (isLiked) 1 else -1, email, isShared
                )
                Result.Success(Unit)
            } catch (e: HttpException) {
                e.printStackTrace()
                when (e.code()) {
                    501 -> Result.Failure(GlobalError.INTERNAL_SERVER_ERROR)
                    400 -> Result.Failure(GlobalError.BAD_REQUEST)
                    else -> Result.Failure(GlobalError.UN_KNOWN)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Failure(GlobalError.UN_KNOWN)
            }
        }
    }

    override suspend fun sharePost(
        postId: Int,
        content: String
    ): Result<Unit, GlobalError> {
        return withContext(Dispatchers.IO) {
            try {
                val email = context.getEmail()
                val sharePostDto = SharePostDto(
                    postId,
                    email,
                    content
                )
                postApi.sharePost(
                    sharePostDto = sharePostDto
                )
                Result.Success(Unit)
            } catch (e: HttpException) {
                e.printStackTrace()
                when (e.code()) {
                    501 -> Result.Failure(GlobalError.INTERNAL_SERVER_ERROR)
                    400 -> Result.Failure(GlobalError.BAD_REQUEST)
                    else -> Result.Failure(GlobalError.UN_KNOWN)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Failure(GlobalError.UN_KNOWN)
            }
        }
    }

    override suspend fun deleteShare(
        postId: Int,
        email: String
    ): Result<Unit, GlobalError> {
        return withContext(Dispatchers.IO) {
            try {
                val sharePostDto = SharePostDto(
                    postId,
                    email,
                    ""
                )
                postApi.deleteShare(
                    sharePostDto = sharePostDto
                )
                Result.Success(Unit)
            } catch (e: HttpException) {
                e.printStackTrace()
                when (e.code()) {
                    501 -> Result.Failure(GlobalError.INTERNAL_SERVER_ERROR)
                    400 -> Result.Failure(GlobalError.BAD_REQUEST)
                    else -> Result.Failure(GlobalError.UN_KNOWN)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Failure(GlobalError.UN_KNOWN)
            }
        }
    }

    override suspend fun updateShare(
        postId: Int,
        email: String
    ): Result<Unit, GlobalError> {
        return withContext(Dispatchers.IO) {
            try {
                val sharePostDto = SharePostDto(
                    postId,
                    email,
                    ""
                )
                postApi.updateShare(
                    sharePostDto = sharePostDto
                )
                Result.Success(Unit)
            } catch (e: HttpException) {
                e.printStackTrace()
                when (e.code()) {
                    501 -> Result.Failure(GlobalError.INTERNAL_SERVER_ERROR)
                    400 -> Result.Failure(GlobalError.BAD_REQUEST)
                    else -> Result.Failure(GlobalError.UN_KNOWN)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Failure(GlobalError.UN_KNOWN)
            }
        }
    }

    override suspend fun addComment(
        postId: Int,
        comment: String,
        isShared: Boolean
    ): Result<Unit, GlobalError> {
        return withContext(Dispatchers.IO) {
            try {
                val email = context.getEmail()
                postApi.addComment(postId, comment, email, isShared)
                Result.Success(Unit)
            } catch (e: HttpException) {
                e.printStackTrace()
                when (e.code()) {
                    501 -> Result.Failure(GlobalError.INTERNAL_SERVER_ERROR)
                    400 -> Result.Failure(GlobalError.BAD_REQUEST)
                    else -> Result.Failure(GlobalError.UN_KNOWN)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Failure(GlobalError.UN_KNOWN)
            }
        }
    }

    override suspend fun deleteComment(
        postId: Int,
        email: String
    ): Result<Unit, GlobalError> {
        return withContext(Dispatchers.IO) {
            try {
                postApi.deleteComment(postId, email)
                Result.Success(Unit)
            } catch (e: HttpException) {
                e.printStackTrace()
                when (e.code()) {
                    501 -> Result.Failure(GlobalError.INTERNAL_SERVER_ERROR)
                    400 -> Result.Failure(GlobalError.BAD_REQUEST)
                    else -> Result.Failure(GlobalError.UN_KNOWN)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Failure(GlobalError.UN_KNOWN)
            }
        }
    }

    override suspend fun updateComment(
        postId: Int,
        comment: String,
        email: String
    ): Result<Unit, GlobalError> {
        return withContext(Dispatchers.IO) {
            try {
                postApi.updateComment(postId, comment, email)
                Result.Success(Unit)
            } catch (e: HttpException) {
                e.printStackTrace()
                when (e.code()) {
                    501 -> Result.Failure(GlobalError.INTERNAL_SERVER_ERROR)
                    400 -> Result.Failure(GlobalError.BAD_REQUEST)
                    else -> Result.Failure(GlobalError.UN_KNOWN)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Failure(GlobalError.UN_KNOWN)
            }
        }
    }

    override suspend fun getComments(
        postId: Int
    ): Result<List<Comment>, GlobalError> {
        return withContext(Dispatchers.IO) {
            try {
                val comments = postApi.getComments(postId)
                Result.Success(comments.map { it.toComment() })
            } catch (e: HttpException) {
                e.printStackTrace()
                when (e.code()) {
                    501 -> Result.Failure(GlobalError.INTERNAL_SERVER_ERROR)
                    400 -> Result.Failure(GlobalError.BAD_REQUEST)
                    else -> Result.Failure(GlobalError.UN_KNOWN)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Failure(GlobalError.UN_KNOWN)
            }
        }
    }

    override suspend fun getMyPosts(
        email: String
    ): Result<List<Post>, GlobalError> {
        return withContext(Dispatchers.IO) {
            try {
                val posts = postApi.getMyPosts(email)
                Result.Success(posts.map { it.toPost() })
            } catch (e: HttpException) {
                e.printStackTrace()
                when (e.code()) {
                    501 -> Result.Failure(GlobalError.INTERNAL_SERVER_ERROR)
                    400 -> Result.Failure(GlobalError.BAD_REQUEST)
                    else -> Result.Failure(GlobalError.UN_KNOWN)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Failure(GlobalError.UN_KNOWN)
            }
        }
    }

    override suspend fun getMyFriends(
        email: String
    ): Result<List<Friend>, GlobalError> {
        return withContext(Dispatchers.IO) {
            try {
                val friends = friendsApi.getAllFriends(email)
                Result.Success(friends.map { it.toFriend() }.filter {
                    it.statues == "Accept"
                })
            } catch (e: HttpException) {
                e.printStackTrace()
                when (e.code()) {
                    501 -> Result.Failure(GlobalError.INTERNAL_SERVER_ERROR)
                    400 -> Result.Failure(GlobalError.BAD_REQUEST)
                    else -> Result.Failure(GlobalError.UN_KNOWN)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Failure(GlobalError.UN_KNOWN)
            }
        }
    }

    override suspend fun getMyFriendsRequest(
        email: String
    ): Result<List<Friend>, GlobalError> {
        return withContext(Dispatchers.IO) {
            try {
                val friends = friendsApi.getFriendsRequests(email)
                Result.Success(friends.map { it.toFriend() }.filter {
                    it.statues == "Pending"
                })
            } catch (e: HttpException) {
                e.printStackTrace()
                when (e.code()) {
                    501 -> Result.Failure(GlobalError.INTERNAL_SERVER_ERROR)
                    400 -> Result.Failure(GlobalError.BAD_REQUEST)
                    else -> Result.Failure(GlobalError.UN_KNOWN)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Failure(GlobalError.UN_KNOWN)
            }
        }
    }

    override suspend fun updateFriendShipStatue(
        user1: String,
        statues: String
    ): Result<Unit, GlobalError> {
        return withContext(Dispatchers.IO) {
            try {
                val user2 = context.getEmail()
                friendsApi.updateFriendShip(user1, user2, statues)
                Result.Success(Unit)
            } catch (e: HttpException) {
                e.printStackTrace()
                when (e.code()) {
                    501 -> Result.Failure(GlobalError.INTERNAL_SERVER_ERROR)
                    400 -> Result.Failure(GlobalError.BAD_REQUEST)
                    else -> Result.Failure(GlobalError.UN_KNOWN)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Failure(GlobalError.UN_KNOWN)
            }
        }
    }

    override suspend fun addToClose(
        user1: String,
        close: Boolean
    ): Result<Unit, GlobalError> {
        return withContext(Dispatchers.IO) {
            try {
                val user2 = context.getEmail()
                friendsApi.addToClose(user1, user2, close)
                Result.Success(Unit)
            } catch (e: HttpException) {
                e.printStackTrace()
                when (e.code()) {
                    501 -> Result.Failure(GlobalError.INTERNAL_SERVER_ERROR)
                    400 -> Result.Failure(GlobalError.BAD_REQUEST)
                    else -> Result.Failure(GlobalError.UN_KNOWN)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Failure(GlobalError.UN_KNOWN)
            }
        }
    }

    override suspend fun addFriend(
        user1: String,
    ): Result<Unit, GlobalError> {
        return withContext(Dispatchers.IO) {
            try {
                val user2 = context.getEmail()
                friendsApi.addFriend(user2, user1)
                Result.Success(Unit)
            } catch (e: HttpException) {
                e.printStackTrace()
                when (e.code()) {
                    501 -> Result.Failure(GlobalError.INTERNAL_SERVER_ERROR)
                    400 -> Result.Failure(GlobalError.BAD_REQUEST)
                    else -> Result.Failure(GlobalError.UN_KNOWN)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Failure(GlobalError.UN_KNOWN)
            }
        }
    }

    override suspend fun friendShipState(
        email: String
    ): Result<FriendShip, GlobalError> {
        return withContext(Dispatchers.IO) {
            try {
                val local = context.getEmail()
                val friends = friendsApi.getAllFriends(local)
                val friend = friends.firstOrNull { it.user.email == email }
                Result.Success(
                    FriendShip(
                        isFriend = friend != null,
                        isClose = friend?.isClose ?: false
                    )
                )
            } catch (e: HttpException) {
                e.printStackTrace()
                when (e.code()) {
                    501 -> Result.Failure(GlobalError.INTERNAL_SERVER_ERROR)
                    400 -> Result.Failure(GlobalError.BAD_REQUEST)
                    else -> Result.Failure(GlobalError.UN_KNOWN)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Failure(GlobalError.UN_KNOWN)
            }
        }
    }
}