package com.example.socailmedia.data.remote

import com.example.socailmedia.domain.model.Chat
import com.example.socailmedia.domain.model.Message
import com.example.socailmedia.domain.util.Result
import com.example.socailmedia.util.ChatError
import kotlinx.coroutines.flow.Flow

interface ChatSocketApi {
    suspend fun initSession(
        userName: String
    ): Result<Unit, ChatError>

    suspend fun sendMessage(
        text: String,
        userName: String
    ): Result<Unit, ChatError>

    fun observeMessages(): Flow<Message>

    suspend fun closeSession(userName: String)

    // social media
    suspend fun initChat(
        sender: String,
        receiver: String
    ): Result<Unit, ChatError>

    suspend fun sendChatMessage(
        text: String,
    ): Result<Unit, ChatError>

    fun observeChatMessages(): Flow<Chat>

    suspend fun closeChat(sender: String)

    companion object {
//        const val API_URL = "ws://10.0.2.2:8080/"
        const val API_URL = "ws://192.168.1.4:8080/"
    }
}