package com.example.socailmedia.data.repositories

import com.example.socailmedia.data.mapper.toChat
import com.example.socailmedia.data.mapper.toMessage
import com.example.socailmedia.data.remote.ChatAPi
import com.example.socailmedia.domain.model.Chat
import com.example.socailmedia.domain.model.Message
import com.example.socailmedia.domain.repositories.ChatRepository
import com.example.socailmedia.domain.util.Result
import com.example.socailmedia.util.ChatError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ChatRepoImpl @Inject constructor(
    private val chatAPi: ChatAPi
) : ChatRepository {
    override suspend fun getAllMessages(): Result<List<Message>, ChatError> {
        return withContext(Dispatchers.IO) {
            try {
                val messages = chatAPi.getAllMessages()
                Result.Success(messages.map { it.toMessage() })
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Failure(ChatError.UNKNOWN)
            }
        }
    }

    override suspend fun getChatMessages(
        sender: String,
        receiver: String
    ): Result<List<Chat>, ChatError> {
        return withContext(Dispatchers.IO) {
            try {
                val data = chatAPi.getMessages(sender, receiver).map {
                    it.toChat()
                }
                Result.Success(data)
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Failure(ChatError.UNKNOWN)
            }
        }
    }
}