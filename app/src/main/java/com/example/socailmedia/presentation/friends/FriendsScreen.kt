package com.example.socailmedia.presentation.friends

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.socailmedia.domain.model.User
import com.example.socailmedia.presentation.compenent.FriendRequest
import com.example.socailmedia.presentation.compenent.Loading
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import ir.kaaveh.sdpcompose.sdp

@Composable
fun FriendsScreen(
    state: FriendsState,
    onAcceptFriendShip: (User) -> Unit,
    onRejectFriendShip: (User) -> Unit,
    onRefresh: () -> Unit
) {
    if (state.isLoading) {
        Loading()
    }
    SwipeRefresh(
        rememberSwipeRefreshState(isRefreshing = false),
        onRefresh = { onRefresh() }
    ) {
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(16.sdp),
        ) {
            items(state.users, key = { it.email }) { user ->
                var isVisible by remember {
                    mutableStateOf(true)
                }
                AnimatedVisibility(
                    visible = isVisible,
                    exit = slideOutVertically { it },
                    enter = slideInHorizontally()
                ) {
                    FriendRequest(
                        friend = user,
                        onAcceptClicked = {
                            isVisible = false
                            onAcceptFriendShip(it)
                        },
                        onRejectClicked = {
                            isVisible = false
                            onRejectFriendShip(it)
                        }
                    )
                }
            }
        }
    }
}