package com.example.socailmedia.presentation.profile

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socailmedia.domain.model.Comment
import com.example.socailmedia.domain.model.User
import com.example.socailmedia.domain.repositories.AuthRepository
import com.example.socailmedia.domain.repositories.Repository
import com.example.socailmedia.domain.util.Result
import com.example.socailmedia.util.ConstObject.CANCEL
import com.example.socailmedia.util.ConstObject.PUBLIC
import com.example.socailmedia.util.ConstObject.REMOVE_CLOSE
import com.example.socailmedia.util.ConstObject.REMOVE_FRIEND
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ProfileVM @Inject constructor(
    private var repository: Repository,
    private val savedStateHandle: SavedStateHandle,
    private var authRepository: AuthRepository
) : ViewModel() {
    var state by mutableStateOf(ProfileState())
        private set

    private val _url = MutableStateFlow<Uri?>(null)
    val url: StateFlow<Uri?> = _url

    fun uploadImg(url: Uri) {
        viewModelScope.launch {
            _url.emit(url)
            val file = getFileFromUri(url)
            repository.uploadImg(file)
        }
    }

    private fun getFileFromUri(url: Uri): File {
        return repository.getFileFromContent(url)
    }

    fun setRepository(repository: Repository) {
        this.repository = repository
    }

    fun setAuthRepository(repository: AuthRepository) {
        this.authRepository = repository
    }

    init {
        val email = savedStateHandle["email"] ?: ""
        val hisAccount = savedStateHandle["hisAccount"] ?: false
        state = state.copy(
            hisAccount = hisAccount,
        )
        viewModelScope.launch {
            val deferred = mutableListOf<Deferred<Unit>>()
            val profileUser = async {
                getUserByEmail(email)
            }
            val baseUser = async {
                println("base User")
                state = when (val res = authRepository.getActiveUser()) {
                    is Result.Failure -> {
                        state.copy(
                            baseUser = User("no name", email),
                            error = res.error.name,
                        )
                    }

                    is Result.Success -> {
                        println(res.data)
                        state.copy(
                            baseUser = res.data,
                            error = null
                        )
                    }
                }
            }
            deferred.addAll(listOf(profileUser, baseUser))
            deferred.awaitAll()
            state = when (val res = repository.getMyPosts(state.user.email)) {
                is Result.Failure -> {
                    state.copy(
                        error = res.error.name,
                    )
                }

                is Result.Success -> {
                    println("2 ${res.data}")
                    state.copy(
                        posts = state.posts + res.data
                    )
                }
            }
            state = when (val res = repository.getMyFriends(state.user.email)) {
                is Result.Failure -> {
                    state.copy(
                        error = res.error.name
                    )
                }

                is Result.Success -> {
                    println("3 ${res.data}")
                    state.copy(
                        friends = res.data.size,
                        close = res.data.filter { it.isClose }.size,
                    )
                }
            }
            if (!state.hisAccount) {
                state = when (val res = repository.friendShipState(state.user.email)) {
                    is Result.Failure -> {
                        state.copy(
                            error = res.error.name
                        )
                    }

                    is Result.Success -> {
                        println("4 ${res.data}")
                        state.copy(
                            isFriend = res.data.isFriend,
                            isClose = res.data.isClose
                        )
                    }
                }
            }
        }
    }

    private suspend fun getUserByEmail(email: String) {
        println("profile User")
        state = when (val res = authRepository.getUserByEmail(email)) {
            is Result.Failure -> {
                state.copy(
                    user = User("no name", email),
                    error = res.error.name,
                )
            }

            is Result.Success -> {
                state.copy(
                    user = res.data,
                    error = null
                )
            }
        }
    }

    fun event(event: ProfileEvents) {
        when (event) {
            ProfileEvents.OnAddClose -> {
                state = state.copy(
                    isClose = true,
                    close = state.close + 1,
                )
                viewModelScope.launch {
                    state = when (val res = repository.addToClose(state.user.email, true)) {
                        is Result.Failure -> state.copy(error = res.error.name)
                        is Result.Success -> state.copy(error = null)
                    }
                }
            }

            ProfileEvents.OnAddFriend -> {
                state = state.copy(
                    isEnabled = false
                )
                viewModelScope.launch {
                    state = when (val res = repository.addFriend(state.user.email)) {
                        is Result.Failure -> state.copy(error = res.error.name)
                        is Result.Success -> state.copy(error = null)
                    }
                }
            }

            is ProfileEvents.OnCommentClicked -> {
                state = state.copy(
                    showComment = true,
                    selectedPost = event.post.postId
                )
                viewModelScope.launch {
                    state = when (val res = repository.getComments(event.post.postId)) {
                        is Result.Failure -> {
                            state.copy(
                                error = res.error.name
                            )
                        }

                        is Result.Success -> {
                            state.copy(
                                comments = res.data
                            )
                        }
                    }
                }
            }

            is ProfileEvents.OnLikeClicked -> {
                viewModelScope.launch {
                    state = when (val res = repository.likePost(
                        postId = event.post.postId,
                        isLiked = event.isLiked,
                        isShared = event.post.isShared
                    )) {
                        is Result.Failure -> state.copy(error = res.error.name)
                        is Result.Success -> {
                            state.copy(
                                error = null
                            )
                        }
                    }
                }
            }

            is ProfileEvents.OnRemoveFriend -> {
                when (event.state) {
                    REMOVE_FRIEND -> {
                        state = state.copy(
                            isFriend = false,
                            isEnabled = true,
                            isClose = false
                        )
                        viewModelScope.launch {
                            when (val res = repository.updateFriendShipStatue(
                                state.user.email,
                                CANCEL
                            )) {
                                is Result.Failure -> {
                                    state = state.copy(
                                        error = res.error.name
                                    )
                                }

                                is Result.Success -> {
                                    state = state.copy(
                                        error = null
                                    )
                                }
                            }
                        }
                    }

                    REMOVE_CLOSE -> {
                        state = state.copy(
                            isClose = false
                        )
                        viewModelScope.launch {
                            state =
                                when (val res = repository.addToClose(state.user.email, false)) {
                                    is Result.Failure -> {
                                        state.copy(
                                            error = res.error.name
                                        )
                                    }

                                    is Result.Success -> {
                                        state.copy(
                                            error = null
                                        )
                                    }
                                }
                        }
                    }
                }
            }

            is ProfileEvents.OnShareClicked -> {
                state = state.copy(
                    showShareDialog = true,
                    selectedPost = event.post.postId
                )
            }

            is ProfileEvents.OnTypingComment -> {
                state = state.copy(
                    comment = event.comment
                )
            }

            ProfileEvents.OnDismissBottomSheet -> {
                state = state.copy(
                    showComment = false,
                    selectedPost = -1
                )
            }

            ProfileEvents.OnDismissDialog -> state = state.copy(
                showShareDialog = false,
                sharedContent = "",
                sharedType = PUBLIC
            )

            is ProfileEvents.OnSendComment -> {
                viewModelScope.launch {
                    state = state.copy(
                        comments = state.comments + Comment(
                            state.baseUser,
                            state.comment,
                        ),
                        posts = state.posts.map {
                            if (it.postId == event.selectedPost) {
                                it.copy(
                                    comments = it.comments + 1
                                )
                            } else it
                        }
                    )
                    when (val res = repository.addComment(
                        event.selectedPost,
                        state.comment,
                        false
                    )) {
                        is Result.Failure -> {
                            state = state.copy(
                                error = res.error.name
                            )
                        }

                        is Result.Success -> {
                            state = state.copy(
                                error = null,
                                comment = ""
                            )
                        }
                    }
                }
            }

            ProfileEvents.OnShare -> {
                viewModelScope.launch {
                    when (val res = repository.sharePost(
                        state.selectedPost,
                        state.sharedContent
                    )) {
                        is Result.Failure -> {
                            state = state.copy(
                                error = res.error.name
                            )
                        }

                        is Result.Success -> {
                            state = state.copy(
                                error = null,
                                showShareDialog = false,
                                sharedContent = "",
                                sharedType = PUBLIC
                            )
                        }
                    }
                }
            }

            is ProfileEvents.OnTypeChange -> state = state.copy(
                sharedType = event.type
            )

            is ProfileEvents.OnTypingSharedContent -> state = state.copy(
                sharedContent = event.content
            )
        }
    }
}