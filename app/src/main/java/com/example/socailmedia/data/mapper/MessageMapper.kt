package com.example.socailmedia.data.mapper

import com.example.socailmedia.data.remote.dto.ChatDto
import com.example.socailmedia.data.remote.dto.MessageDto
import com.example.socailmedia.domain.model.Chat
import com.example.socailmedia.domain.model.Message
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun MessageDto.toMessage(): Message =
    Message(
        userName = userName,
        time = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            .format(Date(time)).toString(),
        text = text
    )

fun ChatDto.toChat() = Chat(
    user = user.toUser(),
    message = message,
    time = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        .format(Date(time)).toString()
)