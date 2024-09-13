package com.example.socailmedia.presentation.compenent

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.example.socailmedia.R
import com.example.socailmedia.ui.theme.SocailMediaTheme
import com.example.socailmedia.util.ConstObject.ONLY_FRIENDS
import com.example.socailmedia.util.ConstObject.PRIVATE
import com.example.socailmedia.util.ConstObject.PUBLIC
import ir.kaaveh.sdpcompose.sdp

@Composable
fun ShareDialog(
    text: String = "",
    onSend: () -> Unit,
    onTextChange: (String) -> Unit,
    onTypeChange: (String) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Column(
            Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(10.sdp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DropDown(
                isError = false,
                items = listOf(PUBLIC, PRIVATE, ONLY_FRIENDS),
            ) {
                onTypeChange(it)
            }
            Row(
                Modifier.fillMaxWidth().padding(5.sdp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.sdp)
            ) {
                OutlinedTextField(
                    value = text,
                    onValueChange = {
                        onTextChange(it)
                    },
                    modifier = Modifier.weight(1f),
                    label = {
                        Text(text = "add comment")
                    },
                )
                Icon(
                    painter = painterResource(id = R.drawable.baseline_send_24),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable {
                            onSend()
                        }
                        .size(30.sdp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DialogPrev() {
    SocailMediaTheme {
        ShareDialog(
            onSend = {},
            onTextChange = {},
            onTypeChange = {},
            onDismiss = {}
        )
    }
}