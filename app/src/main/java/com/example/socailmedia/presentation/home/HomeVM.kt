package com.example.socailmedia.presentation.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socailmedia.domain.model.Comment
import com.example.socailmedia.domain.repositories.AuthRepository
import com.example.socailmedia.domain.repositories.Repository
import com.example.socailmedia.domain.util.Result
import com.example.socailmedia.util.ConstObject.PRIVATE
import com.example.socailmedia.util.ConstObject.PUBLIC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeVM @Inject constructor(
    private var authRepository: AuthRepository,
    private var repository: Repository
) : ViewModel() {
    var state by mutableStateOf(HomeState())
        private set

    fun setRepository(repository: Repository) {
        this.repository = repository
    }

    fun setAuthRepository(repository: AuthRepository) {
        this.authRepository = repository
    }

    fun event(events: HomeEvents) {
        when (events) {
            is HomeEvents.GetUserFromNav -> {
                val user = events.user
                state = state.copy(isLoading = true)
                if (user.age < 0) {
                    viewModelScope.launch {
                        when (val res = authRepository.getUserByEmail(user.email)) {
                            is Result.Failure -> {
                                state = state.copy(
                                    error = res.error.name,
                                    isLoading = false
                                )
                            }

                            is Result.Success -> {
                                state = state.copy(
                                    error = null,
                                    isLoading = false,
                                    user = res.data
                                )
                                event(HomeEvents.GetAllPosts)
                            }
                        }
                    }
                } else {
                    state = state.copy(
                        user = user,
                        isLoading = false
                    )
                    event(HomeEvents.GetAllPosts)
                }
            }

            HomeEvents.GetAllPosts -> {
                viewModelScope.launch {
                    state = when (val res = repository.getAllPosts()) {
                        is Result.Failure -> {
                            state.copy(
                                error = res.error.name
                            )
                        }

                        is Result.Success -> state.copy(
                            error = null,
                            posts = res.data
                        )
                    }
                }
            }

            HomeEvents.OnAddPost -> {
                state = state.copy(
                    showShareDialog = true,
                    selectedPost = -1
                )
            }

            is HomeEvents.OnCommentClicked -> {
                viewModelScope.launch {
                    state = when (val res = repository.getComments(events.postId)) {
                        is Result.Failure -> {
                            state.copy(
                                error = res.error.name
                            )
                        }

                        is Result.Success -> {
                            state.copy(
                                error = null,
                                comments = res.data,
                                showBottomSheet = true,
                                selectedPost = events.postId
                            )
                        }
                    }
                }
            }

            HomeEvents.OnDismissBottomSheet -> {
                state = state.copy(
                    showBottomSheet = false,
                    selectedPost = -1
                )
            }

            is HomeEvents.OnLikeClicked -> {
                state = state.copy(
                    posts = state.posts.map {
                        if (it.postId == events.postId) {
                            it.copy(
                                isLiked = events.isLiked,
                                likes = if (events.isLiked) it.likes + 1 else it.likes - 1
                            )
                        } else it
                    }
                )
                viewModelScope.launch {
                    when (val res = repository.likePost(
                        events.postId,
                        events.isLiked,
                        events.isShared
                    )) {
                        is Result.Failure -> {
                            state = state.copy(
                                error = res.error.name
                            )
                        }

                        is Result.Success -> {
                            state = state.copy(
                                error = null,
                            )
                            println("success ${events.postId} ${events.isLiked} ${state.user.email}")
                        }
                    }
                }
            }

            HomeEvents.OnRefresh -> {
                viewModelScope.launch {
                    state = when (val res = repository.getAllPosts()) {
                        is Result.Failure -> {
                            state.copy(
                                error = res.error.name
                            )
                        }

                        is Result.Success -> state.copy(
                            error = null,
                            posts = res.data
                        )
                    }
                }
            }

            is HomeEvents.OnSendComment -> {
                viewModelScope.launch {
                    state = state.copy(
                        comments = state.comments + Comment(
                            state.user,
                            state.comment,
                        ),
                        posts = state.posts.map {
                            if (it.postId == events.postId) {
                                it.copy(
                                    comments = it.comments + 1
                                )
                            } else it
                        }
                    )
                    when (val res = repository.addComment(
                        events.postId,
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

            is HomeEvents.OnShareClicked -> {
                state = state.copy(
                    showShareDialog = true,
                    selectedPost = events.postId
                )
            }

            is HomeEvents.OnTypingComment -> {
                state = state.copy(
                    comment = events.comment
                )
            }

            is HomeEvents.OnContentChange -> {
                state = state.copy(
                    sharedContent = events.content
                )
            }

            HomeEvents.OnDismissDialog -> {
                state = state.copy(
                    showShareDialog = false,
                    sharedContent = "",
                    sharedType = PUBLIC
                )
            }

            HomeEvents.OnShare -> {
                if (state.selectedPost != -1) {
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
                } else {
                    viewModelScope.launch {
                        when (val res = repository.addPost(
                            state.user.email,
                            state.sharedContent,
                            if (state.sharedType == PUBLIC) 0 else if (state.sharedType == PRIVATE) 1 else 2
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
            }

            is HomeEvents.OnTypeChange -> {
                state = state.copy(
                    sharedType = events.type
                )
            }

            else -> {}
        }
    }
}