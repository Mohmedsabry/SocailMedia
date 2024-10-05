package com.example.socailmedia.presentation.details

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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsVm @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private var repository: Repository,
    private var authRepository: AuthRepository
) : ViewModel() {
    var state by mutableStateOf(DetailsState())
        private set

    fun setRepository(repository: Repository) {
        this.repository = repository
    }

    fun setAuthRepository(repository: AuthRepository) {
        this.authRepository = repository
    }

    init {
        val user = savedStateHandle["user"] ?: ""
        val postId = savedStateHandle["postId"] ?: 0
        val isShared = savedStateHandle["isShared"] ?: false
        viewModelScope.launch {
            val deferred = mutableListOf<Deferred<Unit>>()
            val profileUser = async {
                getUserByEmail(user)
            }
            val baseUser = async {
                println("base User")
                state = when (val res = authRepository.getActiveUser()) {
                    is Result.Failure -> {
                        state.copy(
                            baseUser = User("no name", ""),
                            error = res.error.name,
                        )
                    }

                    is Result.Success -> {
                        println(res.data)
                        state.copy(
                            baseUser = res.data, error = null
                        )
                    }
                }
            }
            deferred.addAll(listOf(profileUser, baseUser))
            deferred.awaitAll()
            val task1 = async {
                println("1")
                state = when (val res = repository.getSpecificPost(postId, isShared)) {
                    is Result.Failure -> {
                        println("11")
                        state.copy(
                            error = res.error.name
                        )
                    }

                    is Result.Success -> {
                        println("11")
                        state.copy(
                            error = null, post = res.data
                        )
                    }
                }
            }
            val task2 = async {
                println("2")
                state = when (val res = repository.getComments(postId)) {
                    is Result.Failure -> {
                        println("22")
                        state.copy(
                            error = res.error.name
                        )
                    }

                    is Result.Success -> {
                        println("22")
                        state.copy(
                            error = null, comments = res.data
                        )
                    }
                }
            }
            task1.await()
            task2.await()
        }
    }

    fun event(events: DetailsEvents) {
        when (events) {
            is DetailsEvents.OnLiked -> {
                viewModelScope.launch {
                    state = when (val res = repository.likePost(
                        state.post.postId, events.isLiked, state.post.isShared
                    )) {
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

            is DetailsEvents.OnProfileClicked -> {

            }

            DetailsEvents.OnSendComment -> {
                state = state.copy(
                    comments = state.comments + Comment(
                        state.baseUser, state.comment
                    )
                )
                viewModelScope.launch {
                    when (val res = repository.addComment(
                        state.post.postId, state.comment, state.post.isShared
                    )) {
                        is Result.Failure -> {
                            state = state.copy(
                                error = res.error.name,
                                comments = state.comments - Comment(
                                    state.baseUser, state.comment
                                )
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

            DetailsEvents.OnShare -> {
                state = state.copy(
                    showShare = true
                )
            }

            is DetailsEvents.OnTyping -> {
                state = state.copy(
                    comment = events.comment
                )
            }

            is DetailsEvents.OnContentChange -> {
                state = state.copy(
                    shareContent = events.type
                )
            }

            DetailsEvents.OnDismiss -> {
                state = state.copy(
                    showShare = false
                )
            }

            DetailsEvents.OnSharePost -> {
                viewModelScope.launch {
                    state = when (val res =
                        repository.sharePost(state.post.postId, state.shareContent)) {
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

            is DetailsEvents.OnTypeChange -> {
                state = state.copy(
                    type = events.type
                )
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
                    user = res.data, error = null
                )
            }
        }
    }
}