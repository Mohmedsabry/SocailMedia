package com.example.socailmedia.presentation.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.socailmedia.R
import com.example.socailmedia.domain.model.Post
import com.example.socailmedia.presentation.compenent.DropDown
import com.example.socailmedia.presentation.compenent.PostDesign
import com.example.socailmedia.presentation.compenent.SharedPost
import com.example.socailmedia.ui.theme.SocailMediaTheme
import com.example.socailmedia.util.ConstObject.REMOVE_CLOSE
import com.example.socailmedia.util.ConstObject.REMOVE_FRIEND
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun ProfileScreen(
    state: ProfileState,
    addFriendClick: () -> Unit,
    addCloseClick: () -> Unit,
    removeFriendClick: (String) -> Unit,
    onPostClicked: (Post) -> Unit,
    onLikeClicked: (Post, Boolean) -> Unit,
    onCommentClicked: (Post) -> Unit,
    onShareClicked: (Post) -> Unit,
    onProfileClicked: (Post) -> Unit,
    onUpdatingProfilePic: (Uri) -> Unit = {},
    onNavigateIntoChat: () -> Unit
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            println(uri)
            onUpdatingProfilePic(it)
        }
    }
    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(16.sdp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(5.sdp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (state.user.gender == "male") {
                    Image(
                        painter = painterResource(id = R.drawable.avatar_boy),
                        contentDescription = null,
                        modifier = Modifier
                            .size(80.sdp)
                            .clip(CircleShape)
                            .background(Color.LightGray)
                            .clickable {
                                if (state.hisAccount) {
                                    launcher.launch(
                                        "image/*"
                                    )
                                }
                            }
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.avatar_girl),
                        contentDescription = null,
                        modifier = Modifier
                            .size(80.sdp)
                            .clip(CircleShape)
                            .background(Color.LightGray)
                            .clickable {
                                if (state.hisAccount) {
                                    launcher.launch(
                                        "image/*"
                                    )
                                }
                            }
                    )
                }
                Text(
                    text = state.user.name.replaceFirstChar { it.uppercase() },
                    fontWeight = FontWeight.W900,
                    fontSize = 22.ssp,
                    modifier = Modifier.padding(5.sdp),
                    fontFamily = FontFamily.Cursive
                )
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(5.sdp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Text(
                        text = "Friends ${state.friends}",
                        fontSize = 18.ssp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Close ${state.close}",
                        fontSize = 18.ssp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Green
                    )
                }
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    if (!state.hisAccount) {
                        if (!state.isFriend) {
                            OutlinedButton(
                                onClick = { addFriendClick() },
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = Color.Blue,
                                    contentColor = Color.White
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(5.sdp),
                                enabled = state.isEnabled
                            ) {
                                if (state.isEnabled)
                                    Text(text = "Add Friend", fontSize = 15.ssp)
                                else
                                    Text(text = "Request Pending", fontSize = 15.ssp)
                            }
                        } else if (!state.isClose) {
                            OutlinedButton(
                                onClick = { addCloseClick() },
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = Color.Green,
                                    contentColor = Color.Black
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(5.sdp)
                            ) {
                                Text(
                                    text = "Add Close",
                                    fontSize = 15.ssp
                                )
                            }
                        } else {
                            DropDown(
                                isError = false,
                                modifier = Modifier
                                    .fillMaxWidth(.58f),
                                items = listOf("Friends", REMOVE_FRIEND, REMOVE_CLOSE)
                            ) {
                                removeFriendClick(it)
                            }
                        }
                        OutlinedButton(
                            onClick = {
                                onNavigateIntoChat()
                            },colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = Color.Blue
                            )
                        ) {
                            Row(
                                Modifier.fillMaxSize(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Email,
                                    contentDescription = "Chat Me!",
                                    tint = Color.White
                                )
                                Spacer(Modifier.width(5.dp))
                                Text(
                                    "Chat Me!",
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
                HorizontalDivider()
            }
        }
        items(state.posts) { post ->
            when (post.isShared) {
                false -> {
                    PostDesign(
                        post = post,
                        onPostClicked = { onPostClicked(it) },
                        onLikedClicked = { onLikeClicked(post, it) },
                        onCommentClicked = { onCommentClicked(post) },
                        onShareClicked = { onShareClicked(post) },
                        onProfileClicked = {
                            onProfileClicked(it)
                        }
                    )
                }

                true -> {
                    SharedPost(
                        user = state.user,
                        post = post,
                        onPostClicked = { onPostClicked(it) },
                        onLikedClicked = { onLikeClicked(post, it) },
                        onCommentClicked = { onCommentClicked(post) },
                        onShareClicked = { onShareClicked(post) },
                        onProfileClicked = { onProfileClicked(it) }
                    )
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(16.sdp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfilePrev() {
    SocailMediaTheme {

    }
}