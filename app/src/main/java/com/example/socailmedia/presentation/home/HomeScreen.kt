package com.example.socailmedia.presentation.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.socailmedia.R
import com.example.socailmedia.domain.model.Post
import com.example.socailmedia.presentation.compenent.AppBar
import com.example.socailmedia.presentation.compenent.DrawerHeader
import com.example.socailmedia.presentation.compenent.NavigationBarItems
import com.example.socailmedia.presentation.compenent.PostDesign
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun HomeScreen(
    state: HomeState,
    onProfileClicked: (Post) -> Unit,
    onLogout: () -> Unit,
    onEvent: (HomeEvents) -> Unit
) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current.density
    val screenWidth = remember {
        derivedStateOf { (configuration.screenWidthDp * density).roundToInt() }
    }
    val offsetValue by remember {
        derivedStateOf { (screenWidth.value / 4.5).dp }
    }
    var drawerIsOpen by remember {
        mutableStateOf(false)
    }
    val animationOffset by animateDpAsState(
        targetValue = if (drawerIsOpen) offsetValue else 0.dp,
        label = "animate offset",
    )
    val animateScale by animateFloatAsState(
        targetValue = if (drawerIsOpen) 0.9f else 1f,
        label = "animate scale",
    )
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        bottomBar = {
            NavigationBarItems { item ->
                onEvent(HomeEvents.OnNavigate(item))
            }
        },
        floatingActionButton = {
            OutlinedIconButton(onClick = {
                onEvent(HomeEvents.OnAddPost)
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_post_add_24),
                    contentDescription = null
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        topBar = {
            AppBar {
                coroutineScope.launch {
                    drawerIsOpen = !drawerIsOpen
                }
            }
        }
    ) { innerPadding ->
        val swipeState = rememberSwipeRefreshState(isRefreshing = false)
        SwipeRefresh(state = swipeState, onRefresh = {
            onEvent(HomeEvents.OnRefresh)
        }) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                AnimatedVisibility(
                    visible = drawerIsOpen,
                    enter = slideInHorizontally(
                        initialOffsetX = { -it },
                        animationSpec = tween(500)
                    ),
                    exit = slideOutHorizontally(
                        targetOffsetX = { -it },
                        animationSpec = tween(500)
                    )
                ) {
                    DrawerHeader(user = state.user) {
                        onLogout()
                    }
                }
                LazyColumn(
                    Modifier
                        .fillMaxSize()
                        .scale(animateScale)
                        .offset(x = animationOffset)
                        .shadow(if (drawerIsOpen) 10.dp else 0.dp)
                ) {
                    items(state.posts, key = { it.postId }) { post ->
                        PostDesign(
                            post = post,
                            onPostClicked = {
                                onEvent(HomeEvents.OnNavigateIntoScreen(post))
                            },
                            onLikedClicked = {
                                onEvent(
                                    HomeEvents.OnLikeClicked(
                                        post.postId,
                                        it,
                                        isShared = post.isShared
                                    )
                                )
                            },
                            onCommentClicked = {
                                onEvent(HomeEvents.OnCommentClicked(post.postId))
                            },
                            onShareClicked = {
                                onEvent(HomeEvents.OnShareClicked(post.postId))
                            },
                            onProfileClicked = {
                                onProfileClicked(it)
                            }
                        )
                    }
                }
            }
        }
    }
}

