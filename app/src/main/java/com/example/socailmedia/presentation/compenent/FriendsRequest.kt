package com.example.socailmedia.presentation.compenent

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.socailmedia.R
import com.example.socailmedia.domain.model.User
import com.example.socailmedia.ui.theme.SocailMediaTheme
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun FriendRequest(
    friend: User,
    onAcceptClicked: (User) -> Unit,
    onRejectClicked: (User) -> Unit,
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(5.sdp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(5.sdp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (friend.gender == "male") {
                Image(
                    painter = painterResource(id = R.drawable.avatar_boy),
                    contentDescription = null,
                    modifier = Modifier
                        .size(60.sdp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.avatar_girl),
                    contentDescription = null,
                    modifier = Modifier
                        .size(60.sdp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                )
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(10.sdp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.sdp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.name),
                        contentDescription = "name",
                        modifier = Modifier.size(30.sdp),
                    )
                    val editorText = if (friend.name.length > 17) friend.name.take(12)
                        .plus("...") else friend.name.take(15)
                    Text(
                        text = editorText,
                        color = Color.Black,
                        modifier = Modifier.padding(start = 5.sdp),
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.sdp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.age_limit_10490460),
                        contentDescription = "name",
                        modifier = Modifier.size(35.sdp),
                    )
                    Text(
                        text = friend.age.toString(),
                        color = Color.Black,
                        modifier = Modifier.padding(start = 5.sdp),
                    )
                }
            }
        }
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = { onAcceptClicked(friend) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Green,
                )
            ) {
                Text(
                    text = "Accept",
                    color = Color.Black,
                    fontSize = 16.ssp
                )
            }
            Spacer(modifier = Modifier.padding(10.sdp))
            OutlinedButton(
                onClick = { onRejectClicked(friend) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                )
            ) {
                Text(
                    text = "Reject",
                    color = Color.White,
                    fontSize = 16.ssp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FriendsPrev() {
    SocailMediaTheme {
        FriendRequest(
            friend = User(
                name = "mohammed Sabri",
                gender = "male"
            ),
            onAcceptClicked = {},
            onRejectClicked = {}
        )
    }
}