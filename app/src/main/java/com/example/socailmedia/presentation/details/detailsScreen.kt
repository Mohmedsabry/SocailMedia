package com.example.socailmedia.presentation.details

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.socailmedia.R
import com.example.socailmedia.presentation.compenent.CommentCard
import com.example.socailmedia.presentation.compenent.PostDesign
import com.example.socailmedia.presentation.compenent.SharedPost
import ir.kaaveh.sdpcompose.sdp

@Composable
fun DetailsScreen(
    state: DetailsState,
    onEvent: (DetailsEvents) -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.sdp)
    ) {
        when (!state.post.isShared) {
            true -> {
                PostDesign(
                    post = state.post,
                    onPostClicked = {},
                    onLikedClicked = {
                        onEvent(DetailsEvents.OnLiked(it))
                    },
                    onCommentClicked = { },
                    onShareClicked = { onEvent(DetailsEvents.OnShare) },
                    onProfileClicked = { onEvent(DetailsEvents.OnProfileClicked(it)) }
                )
            }

            false -> {
                SharedPost(
                    user = state.user,
                    post = state.post,
                    onPostClicked = {},
                    onLikedClicked = { onEvent(DetailsEvents.OnLiked(it)) },
                    onCommentClicked = { },
                    onShareClicked = { onEvent(DetailsEvents.OnShare) },
                    onProfileClicked = { onEvent(DetailsEvents.OnProfileClicked(it)) }
                )
            }
        }
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(state.comments) { comment ->
                CommentCard(comment = comment)
            }
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(5.sdp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = state.comment,
                onValueChange = { onEvent(DetailsEvents.OnTyping(it)) },
                modifier = Modifier.weight(1f),
                placeholder = { Text(text = "add comment") },
                shape = RoundedCornerShape(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Icon(
                painter = painterResource(id = R.drawable.baseline_send_24),
                contentDescription = null,
                modifier = Modifier
                    .clickable {
                        onEvent(DetailsEvents.OnSendComment)
                    }
                    .size(30.sdp)
            )
        }
        Spacer(modifier = Modifier.height(20.sdp))
    }
}