package com.example.socailmedia.presentation.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.socailmedia.R
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun ChatScreen(
    chatState: ChatState,
    onAction: (ChatEvents) -> Unit,
    connect: () -> Unit,
    disConnect: () -> Unit,
) {
    val lifecycle = LocalLifecycleOwner.current
    DisposableEffect(key1 = lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                connect()
            } else if (event == Lifecycle.Event.ON_STOP) {
                disConnect()
            }
        }
        lifecycle.lifecycle.addObserver(observer)
        onDispose {
            lifecycle.lifecycle.removeObserver(observer)
        }
    }
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        LazyColumn(
            Modifier
                .weight(1f)
                .fillMaxWidth(),
            reverseLayout = true
        ) {
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
            items(chatState.messages) { message ->
                val isOwnMessage = message.user.email == chatState.sender
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = if (isOwnMessage) Alignment.CenterEnd else Alignment.CenterStart
                ) {
                    Column(
                        Modifier
                            .width(200.dp)
                            .drawBehind {
                                val path = Path().apply {
                                    if (isOwnMessage) {
                                        moveTo(size.width, size.height - 10.dp.toPx())
                                        lineTo(size.width, size.height + 20.dp.toPx())
                                        lineTo(
                                            size.width - 25.dp.toPx(),
                                            size.height - 10.dp.toPx()
                                        )
                                        close()
                                    } else {
                                        moveTo(0f, size.height - 10.dp.toPx())
                                        lineTo(0f, size.height + 20.dp.toPx())
                                        lineTo(
                                            25.dp.toPx(),
                                            size.height - 10.dp.toPx()
                                        )
                                        close()
                                    }
                                }
                                drawPath(path, if (isOwnMessage) Color.Green else Color.DarkGray)
                            }
                            .background(
                                if (isOwnMessage) Color.Green else Color.DarkGray,
                                RoundedCornerShape(10.dp)
                            )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.padding(8.dp)
                        ) {
                            if (message.user.gender == "male") {
                                Image(
                                    painter = painterResource(id = R.drawable.avatar_boy),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(40.sdp)
                                        .clip(CircleShape)
                                        .background(Color.LightGray)
                                )
                            } else {
                                Image(
                                    painter = painterResource(id = R.drawable.avatar_girl),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(40.sdp)
                                        .clip(CircleShape)
                                        .background(Color.LightGray)
                                )
                            }
                            Text(
                                text = message.user.name,
                                color = Color.White,
                                fontSize = 18.ssp,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                        Text(
                            text = message.message,
                            color = Color.White,
                            fontSize = 17.ssp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(8.dp)
                        )
                        Text(
                            text = message.time,
                            color = Color.White,
                            fontSize = 14.ssp,
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(8.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = chatState.message,
                onValueChange = {
                    onAction(ChatEvents.OnTypingMessage(it))
                }, modifier = Modifier.weight(1f)
            )
            OutlinedIconButton(onClick = { onAction(ChatEvents.OnSendMessage) }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_send_24),
                    contentDescription = "Send"
                )
            }
        }
    }
}