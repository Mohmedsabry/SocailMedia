package com.example.socailmedia.presentation.compenent

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.socailmedia.R
import com.example.socailmedia.domain.model.User
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun DrawerHeader(
    user: User,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(.6f)
            .background(Color.LightGray.copy(alpha = 0.1f)),
        verticalArrangement = Arrangement.spacedBy(5.dp),
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
            text = editorText,
            fontSize = 16.ssp,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = "Logout",
            fontSize = 16.ssp,
            modifier = Modifier.clickable {
                onLogout()
            }
        )
    }
}