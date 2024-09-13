package com.example.socailmedia.presentation.compenent

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.socailmedia.R
import ir.kaaveh.sdpcompose.sdp

@Composable
fun AppBar(
    navIconClicked: () -> Unit
) {
    Box(
        Modifier
            .fillMaxWidth()
            .background(Color(0x80F6F0EE))
            .padding(bottom = 25.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Icon(
            painter = painterResource(id = R.drawable.left_alignment),
            contentDescription = null,
            modifier = Modifier
                .size(25.sdp)
                .padding(start = 10.sdp, top = 10.sdp)
                .clickable {
                    navIconClicked.invoke()
                },
            tint = Color(0xff686868)
        )
    }
}