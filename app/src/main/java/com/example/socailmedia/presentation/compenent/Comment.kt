package com.example.socailmedia.presentation.compenent

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.socailmedia.R
import com.example.socailmedia.domain.model.Comment
import com.example.socailmedia.domain.model.User
import com.example.socailmedia.ui.theme.SocailMediaTheme
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun CommentCard(
    comment: Comment
) {
    Box(
        modifier = Modifier
            .padding(5.sdp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color.LightGray)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(5.sdp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (comment.user.gender == "male") {
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
                text = comment.comment,
                fontSize = 18.ssp,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CommentPrev() {
    SocailMediaTheme {
        CommentCard(
            comment = Comment(
                user = User(gender = "male"),
                comment = "nice post"
            )
        )
    }
}