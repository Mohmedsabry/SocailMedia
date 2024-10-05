package com.example.socailmedia.presentation.friends

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socailmedia.domain.repositories.Repository
import com.example.socailmedia.domain.util.Result
import com.example.socailmedia.util.ConstObject.ACCEPT
import com.example.socailmedia.util.ConstObject.CANCEL
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendsVM @Inject constructor(
    private var repository: Repository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    var state by mutableStateOf(FriendsState())
        private set

    fun setRepository(repository: Repository) {
        this.repository = repository
    }

    init {
        val email = savedStateHandle["email"] ?: ""
        state = state.copy(
            isLoading = true,
            email = email
        )
        viewModelScope.launch {
            when (val res = repository.getMyFriendsRequest(email)) {
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
                        users = res.data.map { it.user }
                    )
                }
            }
        }
    }

    fun event(events: FriendsEvents) {
        when (events) {
            is FriendsEvents.Accept -> {
                state = state.copy(
                    users = state.users - events.user
                )
                viewModelScope.launch {
                    state =
                        when (val res =
                            repository.updateFriendShipStatue(events.user.email, ACCEPT)) {
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

            is FriendsEvents.Reject -> {
                state = state.copy(
                    users = state.users - events.user
                )
                viewModelScope.launch {
                    state =
                        when (val res =
                            repository.updateFriendShipStatue(events.user.email, CANCEL)) {
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

            FriendsEvents.Refresh -> {
                viewModelScope.launch {
                    when (val res = repository.getMyFriendsRequest(state.email)) {
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
                                users = res.data.map { it.user }
                            )
                        }
                    }
                }
            }
        }
    }
}