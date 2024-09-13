@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.socailmedia.presentation.compenent

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.socailmedia.R
import com.example.socailmedia.domain.model.Comment
import ir.kaaveh.sdpcompose.sdp

@Composable
fun BottomShow(
    state: SheetState,
    comments: List<Comment> = emptyList(),
    text: String = "",
    onSendComment: () -> Unit = {},
    onValueChange: (String) -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    if (state.isVisible) {
        ModalBottomSheet(
            onDismissRequest = {
                onDismiss()
            },
            sheetState = state,
            modifier = Modifier.fillMaxHeight(.5f)
        ) {
            Column(Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.sdp)
                        .weight(1f)
                ) {
                    items(comments) {
                        CommentCard(comment = it)
                    }
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(5.sdp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = text,
                        onValueChange = { onValueChange(it) },
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
                                onSendComment()
                            }
                            .size(30.sdp)
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun BottomSheetPrev() {
  //  BottomShow()
}