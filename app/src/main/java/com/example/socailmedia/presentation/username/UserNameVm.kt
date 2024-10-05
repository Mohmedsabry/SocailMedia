package com.example.socailmedia.presentation.username

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserNameVm @Inject constructor() : ViewModel() {
    var state by mutableStateOf(UserNameState())
        private set

    fun event(event: UserNameEvents) {
        when (event) {
            UserNameEvents.OnJoinChat -> {
                if (state.userName.isNotBlank() && state.userName.isNotEmpty()) {
                    state = state.copy(
                        navToChat = true
                    )
                }
            }

            is UserNameEvents.OnTyping -> state = state.copy(
                userName = event.text
            )
        }
    }

    fun navigate() {
        state = state.copy(
            userName = "",
            navToChat = false
        )
    }
}