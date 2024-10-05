package com.example.socailmedia.data.remote

import com.example.socailmedia.data.mapper.toChat
import com.example.socailmedia.data.mapper.toMessage
import com.example.socailmedia.data.remote.dto.ChatDto
import com.example.socailmedia.data.remote.dto.MessageDto
import com.example.socailmedia.domain.model.Chat
import com.example.socailmedia.domain.model.Message
import com.example.socailmedia.domain.util.Result
import com.example.socailmedia.util.ChatError
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.url
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import javax.inject.Inject

class ChatSocketImpl @Inject constructor(
    private val client: HttpClient
) : ChatSocketApi {
    private var webSession: WebSocketSession? = null
    override suspend fun initSession(
        userName: String
    ): Result<Unit, ChatError> {
        return try {
            webSession = client.webSocketSession {
                url("${ChatSocketApi.API_URL}/chat?userName=$userName")
            }
            if (webSession?.isActive == true) {
                Result.Success(Unit)
            } else
                Result.Failure(ChatError.UNKNOWN)
        } catch (e: HttpException) {
            e.printStackTrace()
            Result.Failure(ChatError.UserALLREADYEXIST)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Failure(ChatError.UNKNOWN)
        }
    }

    override suspend fun sendMessage(
        text: String,
        userName: String
    ): Result<Unit, ChatError> {
        return withContext(Dispatchers.IO) {
            try {
                webSession?.send(Frame.Text(text))
                Result.Success(Unit)
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Failure(ChatError.UNKNOWN)
            }
        }
    }

    override fun observeMessages(): Flow<Message> {
        return try {
            webSession?.incoming
                ?.receiveAsFlow()
                ?.filter { it is Frame.Text }
                ?.map { frame ->
                    val json = Json.decodeFromString<MessageDto>((frame as Frame.Text).readText())
                    json.toMessage()
                } ?: flow { }
        } catch (e: Exception) {
            e.printStackTrace()
            flow { }
        }
    }

    override suspend fun closeSession(userName: String) {
        withContext(Dispatchers.IO) {
            try {
                webSession?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override suspend fun initChat(
        sender: String,
        receiver: String
    ): Result<Unit, ChatError> {
        return withContext(Dispatchers.IO) {
            try {
                webSession = client.webSocketSession {
                    url("${ChatSocketApi.API_URL}conversation?sender=$sender&receiver=$receiver")
                }
                if (webSession?.isActive != true) {
                    return@withContext Result.Failure(ChatError.UNKNOWN)
                }
                Result.Success(Unit)
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Failure(ChatError.UNKNOWN)
            }
        }
    }

    override suspend fun sendChatMessage(
        text: String,
    ): Result<Unit, ChatError> {
        return withContext(Dispatchers.IO) {
            try {
                webSession?.send(Frame.Text(text))
                Result.Success(Unit)
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Failure(ChatError.UNKNOWN)
            }
        }
    }

    override fun observeChatMessages(): Flow<Chat> {
        return webSession?.incoming?.receiveAsFlow()?.filter {
            it is Frame.Text
        }?.map {
            val decodedMessage = Json.decodeFromString<ChatDto>((it as Frame.Text).readText())
            decodedMessage.toChat()
        } ?: flow { }
    }

    override suspend fun closeChat(sender: String) {
        webSession?.close()
    }
}