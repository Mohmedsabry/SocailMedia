package com.example.socailmedia.presentation.chat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socailmedia.data.remote.ChatSocketApi
import com.example.socailmedia.domain.repositories.ChatRepository
import com.example.socailmedia.domain.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatVm @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val chatSocketApi: ChatSocketApi,
    private val chatRepository: ChatRepository,
) : ViewModel() {
    var state by mutableStateOf(ChatState())

    init {
        val sender = savedStateHandle["sender"] ?: ""
        val receiver = savedStateHandle["receiver"] ?: ""
        state = state.copy(
            sender = sender,
            receiver = receiver
        )
    }

    fun connectToServer() {
        initSession()
        getAllMessages()
    }

    fun event(event: ChatEvents) {
        when (event) {
            ChatEvents.OnSendMessage -> {
                viewModelScope.launch {
                    when (val result = chatSocketApi.sendChatMessage(
                        state.message,
                    )) {
                        is Result.Failure -> {
                            state = state.copy(
                                error = result.error.name
                            )
                        }

                        is Result.Success -> {
                            state = state.copy(
                                error = null,
                                message = ""
                            )
                        }
                    }
                }
            }

            is ChatEvents.OnTypingMessage -> {
                state = state.copy(
                    message = event.message
                )
            }
        }
    }

    private fun getAllMessages() {
        viewModelScope.launch {
            state = when (val res = chatRepository.getChatMessages(
                state.sender,
                state.receiver
            )) {
                is Result.Failure -> {
                    state.copy(
                        error = res.error.name
                    )
                }

                is Result.Success -> {
                    state.copy(
                        messages = res.data
                    )
                }
            }
        }
    }

    private fun observeMessages() {
        viewModelScope.launch {
            chatSocketApi.observeChatMessages().onEach { message ->
                val newList = state.messages.toMutableList()
                newList.add(0, message)
                state = state.copy(
                    messages = newList
                )
            }.launchIn(viewModelScope)
        }
    }

    private fun initSession() {
        viewModelScope.launch {
            when (chatSocketApi.initChat(state.sender, state.receiver)) {
                is Result.Failure -> {
                    state = state.copy(error = "UNKNOWN")
                }

                is Result.Success -> {
                    state = state.copy(error = null)
                    observeMessages()
                }
            }
        }
    }

    fun disconnect() {
        viewModelScope.launch {
            chatSocketApi.closeChat(state.sender)
        }
    }

    override fun onCleared() {
        super.onCleared()
        disconnect()
    }
}