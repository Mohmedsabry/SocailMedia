package com.example.socailmedia.presentation.compenent

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.socailmedia.R
import com.example.socailmedia.domain.model.Post
import com.example.socailmedia.domain.model.TypeOfPost
import com.example.socailmedia.domain.model.User
import com.example.socailmedia.ui.theme.SocailMediaTheme
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp


@Composable
fun SharedPost(
    modifier: Modifier = Modifier,
    user: User,
    post: Post,
    maxLines: Int = Int.MAX_VALUE,
    onPostClicked: (Post) -> Unit,
    onLikedClicked: (Boolean) -> Unit,
    onCommentClicked: () -> Unit,
    onShareClicked: () -> Unit,
    onProfileClicked:(Post)->Unit
) {
    var liked by remember {
        mutableStateOf(post.isLiked)
    }
    OutlinedCard(
        onClick = { onPostClicked(post) },
        modifier = Modifier
            .padding(5.sdp)
            .shadow(40.dp, shape = RoundedCornerShape(10.dp)),
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(12.sdp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier.padding(bottom = 5.dp)
            ) {
                if (user.gender == "male") {
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
                val editorText = if (user.name.length > 17) user.name.take(12)
                    .plus("...") else user.name.take(15)
                Text(
                    text = editorText.replaceFirstChar { it.uppercase() },
                    fontSize = 16.ssp,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.weight(1f))
                when (post.type) {
                    TypeOfPost.PUBLIC -> {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_public_24),
                            contentDescription = null
                        )
                        Text(
                            text = post.type.title, fontSize =
                            14.ssp
                        )
                    }

                    TypeOfPost.PRIVATE -> {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_lock_outline_24),
                            contentDescription = null
                        )
                        Text(
                            text = post.type.title, fontSize =
                            14.ssp
                        )
                    }

                    TypeOfPost.ONLY_FRIENDS -> {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_person_outline_24),
                            contentDescription = null
                        )
                        Text(
                            text = post.type.title, fontSize =
                            14.ssp
                        )
                    }
                }
            }
            Text(
                text = post.sharedContent,
                fontSize = 15.ssp,
                fontWeight = FontWeight.W400,
                modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                overflow = TextOverflow.Ellipsis,
                maxLines = maxLines,
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp, bottom = 5.dp)
                    .shadow(2.dp, shape = RoundedCornerShape(10.dp))
                    .clip(RoundedCornerShape(10.dp)),
            ) {
                Column(
                    Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        modifier = Modifier.padding(top = 10.dp, start = 10.dp, end = 10.dp)
                    ) {
                        if (post.editor.gender == "male") {
                            Image(
                                painter = painterResource(id = R.drawable.avatar_boy),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(40.sdp)
                                    .clip(CircleShape)
                                    .background(Color.LightGray)
                                    .clickable { onProfileClicked(post) }
                            )
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.avatar_girl),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(40.sdp)
                                    .clip(CircleShape)
                                    .background(Color.LightGray)
                                    .clickable { onProfileClicked(post) }
                            )
                        }
                        val editorText = if (post.editor.name.length > 17) post.editor.name.take(12)
                            .plus("...") else post.editor.name.take(15)
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = editorText.replaceFirstChar { it.uppercase() },
                            fontSize = 16.ssp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    Text(
                        text = post.content,
                        fontSize = 18.ssp,
                        fontWeight = FontWeight.W500,
                        modifier = Modifier.padding(10.dp),
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .background(Color(0xFFE9E7E7).copy(0.4f)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = "${post.likes}  likes",
                    fontSize = 11.ssp,
                    fontWeight = FontWeight.W500
                )
                Text(
                    text = "${post.comments}  comments",
                    fontSize = 11.ssp,
                    fontWeight = FontWeight.W500
                )
                Text(
                    text = "${post.shares}  shares",
                    fontSize = 11.ssp,
                    fontWeight = FontWeight.W500
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = modifier
                    .padding(top = 5.dp)
                    .fillMaxWidth()
            ) {
                if (liked) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        modifier = Modifier.clickable {
                            liked = false
                            onLikedClicked(false)
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.like),
                            contentDescription = null,
                            tint = Color(0xffE1204D),
                            modifier = Modifier.size(18.sdp)
                        )
                        Text(
                            text = "Like",
                            fontWeight = FontWeight.W500,
                            fontSize = 15.ssp
                        )
                    }
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        modifier = Modifier.clickable {
                            liked = true
                            onLikedClicked(true)
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.un_like),
                            contentDescription = null,
                            modifier = Modifier
                                .size(18.sdp)
                        )
                        Text(
                            text = "Like",
                            fontWeight = FontWeight.W500,
                            fontSize = 15.ssp
                        )
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier.clickable {
                        onCommentClicked()
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.comment),
                        contentDescription = null,
                        modifier = Modifier
                            .size(18.sdp)
                    )
                    Text(
                        text = "Comment",
                        fontWeight = FontWeight.W500,
                        fontSize = 15.ssp
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier.clickable {
                        onShareClicked()
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.share),
                        contentDescription = null,
                        modifier = Modifier
                            .size(18.sdp)
                    )
                    Text(
                        text = "Share",
                        fontWeight = FontWeight.W500,
                        fontSize = 15.ssp
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SharedPrev() {
    SocailMediaTheme {
        SharedPost(
            user = User(name = "ali", gender = "female"),
            post = Post(
                1,
                User(name = "mohammed Sabri", gender = "male"),
                10,
                10,
                10,
                "hi there\nI'am mohammed sabry from Alex\nniceMeetU\nple\nplakcankacncnaiacnlacnlcnaolaconpa\npla",
                TypeOfPost.PUBLIC,
                isShared = false,
                isLiked = true,
                "hi there\nali\npla\npla\npla"
            ),
            maxLines = 2,
            onPostClicked = { _ -> },
            onLikedClicked = { _ -> },
            onShareClicked = {},
            onCommentClicked = {},
            onProfileClicked={}
        )
    }
}