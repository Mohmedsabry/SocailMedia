package com.example.socailmedia.presentation.compenent

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.socailmedia.R
import com.example.socailmedia.domain.model.Friend
import com.example.socailmedia.domain.model.User
import com.example.socailmedia.ui.theme.SocailMediaTheme
import ir.kaaveh.sdpcompose.sdp

@Composable
fun FriendCard(
    friend: Friend,
    onCloseClicked: (Friend) -> Unit,
    onRemoveCloseClicked: (Friend) -> Unit,
    onRemoveFriendClicked: (Friend) -> Unit,
) {
    var isClose by remember {
        mutableStateOf(friend.isClose)
    }
    val listOption = remember { mutableListOf("Friends", "Close", "Remove Friend") }
    Column(
        Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(5.sdp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (friend.user.gender == "male") {
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
                    val editorText = if (friend.user.name.length > 17) friend.user.name.take(12)
                        .plus("...") else friend.user.name.take(15)
                    Text(
                        text = editorText,
                        color = if (!isClose) Color.Black else Color.Green,
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
                        text = friend.user.age.toString(),
                        color = if (!isClose) Color.Black else Color.Green,
                        modifier = Modifier.padding(start = 5.sdp),
                    )
                }
            }
        }
        DropDown(
            isError = false,
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.sdp),
            items = listOption
        ) { item ->
            when (item) {
                "Friends" -> {}
                "Close" -> {
                    isClose = true
                    listOption.removeFirst()
                    listOption[0] = "Remove Close"
                    onCloseClicked(friend)
                }

                "Remove Close" -> {
                    isClose = false
                    listOption.add(0, "Friends")
                    listOption[1] = "Close"
                    onRemoveCloseClicked(friend)
                }

                "Remove Friend" -> {
                    onRemoveFriendClicked(friend)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FriendPrev() {
    SocailMediaTheme {
        var isRemoved by remember {
            mutableStateOf(false)
        }
        Column(Modifier.fillMaxSize()) {
            AnimatedVisibility(
                visible = !isRemoved,
                enter = slideInHorizontally(),
                exit = slideOutVertically()
            ) {
                FriendCard(
                    friend = Friend(
                        user = User(name = "mohammed sabry", age = 21.5f),
                        statues = "Accept",
                        isClose = false,
                        dateOfAcceptFriend = "2002-05-19"
                    ),
                    onCloseClicked = {},
                    onRemoveCloseClicked = {}
                ) {
                    isRemoved = true
                }
            }
        }
    }
}