package com.example.socailmedia.domain.repositories

import com.example.socailmedia.domain.model.Chat
import com.example.socailmedia.domain.model.Message
import com.example.socailmedia.domain.util.Result
import com.example.socailmedia.util.ChatError

interface ChatRepository {
    suspend fun getAllMessages(): Result<List<Message>, ChatError>
    suspend fun getChatMessages(
        sender: String,
        receiver: String
    ): Result<List<Chat>, ChatError>
}