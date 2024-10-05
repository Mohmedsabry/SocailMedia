package com.example.socailmedia.data.remote

import com.example.socailmedia.data.remote.dto.ChatDto
import com.example.socailmedia.data.remote.dto.MessageDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ChatAPi {
    @GET("/messages")
    suspend fun getAllMessages(): List<MessageDto>

    @GET("/chatMessages")
    suspend fun getMessages(
        @Query("sender") sender: String,
        @Query("receiver") receiver: String
    ): List<ChatDto>

    companion object {
//        const val API_URL = "http://10.0.2.2:8080/"
        const val API_URL = "http://192.168.1.4:8080/"
    }
}