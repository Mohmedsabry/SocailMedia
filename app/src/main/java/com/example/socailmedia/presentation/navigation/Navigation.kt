@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.socailmedia.presentation.navigation

import android.content.Context
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.socailmedia.domain.model.User
import com.example.socailmedia.presentation.compenent.BottomShow
import com.example.socailmedia.presentation.compenent.ShareDialog
import com.example.socailmedia.presentation.details.DetailsEvents
import com.example.socailmedia.presentation.details.DetailsScreen
import com.example.socailmedia.presentation.details.DetailsVm
import com.example.socailmedia.presentation.friends.FriendsEvents
import com.example.socailmedia.presentation.friends.FriendsScreen
import com.example.socailmedia.presentation.friends.FriendsVM
import com.example.socailmedia.presentation.home.HomeEvents
import com.example.socailmedia.presentation.home.HomeScreen
import com.example.socailmedia.presentation.home.HomeVM
import com.example.socailmedia.presentation.home.Screens
import com.example.socailmedia.presentation.login.LoginEvent
import com.example.socailmedia.presentation.login.LoginScreen
import com.example.socailmedia.presentation.login.LoginVM
import com.example.socailmedia.presentation.navigation.Navigation.LoginScreenDest
import com.example.socailmedia.presentation.profile.ProfileEvents
import com.example.socailmedia.presentation.profile.ProfileScreen
import com.example.socailmedia.presentation.profile.ProfileVM
import com.example.socailmedia.presentation.register.RegisterEvents
import com.example.socailmedia.presentation.register.RegisterScreen
import com.example.socailmedia.presentation.register.RegisterVM
import com.example.socailmedia.util.getEmail
import com.example.socailmedia.util.isLogin
import com.example.socailmedia.util.savePref
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Composable
fun NavigationController(navController: NavHostController) {
    val context = LocalContext.current
    val isLogin = context.isLogin()
    println(isLogin)
    val startDest: Any = if (isLogin) Navigation.Home() else LoginScreenDest
    NavHost(
        navController = navController,
        startDestination = startDest
    ) {
        composable<LoginScreenDest> {
            val loginVM = hiltViewModel<LoginVM>()
            val state = loginVM.state
            LoginScreen(
                state = state,
                onTypingEmail = { loginVM.event(LoginEvent.OnTypingEmail(it)) },
                onTypingPassword = { loginVM.event(LoginEvent.OnTypingPassword(it)) },
                onLoginClicked = { loginVM.event(LoginEvent.OnLoginClicked) },
                onSignUpClicked = {
                    navController.navigate(Navigation.RegisterDest) {
                        launchSingleTop = true
                    }
                }
            )
            LaunchedEffect(key1 = state.user) {
                println("from main ${state.user}")
                if (state.user.email.isNotEmpty() || state.user.email.isNotBlank()) {
                    println("save")
                    context.savePref(login = true, state.user.email)
                    navController.navigate(
                        Navigation.Home(
                            name = state.user.name,
                            email = state.user.email,
                            password = state.user.password,
                            age = state.user.age,
                            phoneNumber = state.user.phoneNumber,
                            gender = state.user.gender,
                            dateOfBirth = state.user.dateOfBirth
                        )
                    ) {
                        launchSingleTop = true
                        popUpTo<LoginScreenDest> {
                            inclusive = true
                        }
                    }
                }

            }
        }
        composable<Navigation.RegisterDest> {
            val registerVM = hiltViewModel<RegisterVM>()
            val state = registerVM.state
            val emailValidationResult = remember(state.email, state.registeredClicked) {
                println("email ${registerVM.validateEmail()}")
                registerVM.validateEmail()
            }
            val passwordValidationResult = remember(state.password, state.registeredClicked) {
                println("pass ${registerVM.validatePassword()}")
                registerVM.validatePassword()
            }
            val phoneNumberValidationResult = remember(state.phoneNumber, state.registeredClicked) {
                println("phone ${registerVM.validatePhone()}")
                registerVM.validatePhone()
            }
            RegisterScreen(
                registerState = state,
                isEmailError = emailValidationResult,
                isPassWordError = passwordValidationResult,
                isPhoneError = phoneNumberValidationResult,
                onValueChange = { type, text ->
                    registerVM.event(RegisterEvents.OnTyping(type, text))
                },
                onRegister = {
                    registerVM.event(RegisterEvents.OnRegisterClicked)
                }
            )
            LaunchedEffect(key1 = state.registered) {
                if (state.registered) {
                    // navigate to Home
                    navController.navigate(LoginScreenDest) {
                        launchSingleTop
                        popUpTo<Navigation.RegisterDest> {
                            inclusive = true
                        }
                    }
                }
            }
        }
        composable<Navigation.Home>(
            typeMap = mapOf(typeOf<NavUser>() to userNavInfo)
        ) { args ->
            val arg = args.toRoute<Navigation.Home>()
            val homeVM = hiltViewModel<HomeVM>()
            val user = getUser(arg, context)
            val sheetState = rememberModalBottomSheetState(
                skipPartiallyExpanded = true
            )
            val coroutine = rememberCoroutineScope()
            LaunchedEffect(Unit) {
                homeVM.event(HomeEvents.GetUserFromNav(user))
            }
            LaunchedEffect(homeVM.state.showBottomSheet) {
                if (homeVM.state.showBottomSheet)
                    coroutine.launch {
                        sheetState.show()
                    }
            }
            if (homeVM.state.showShareDialog) {
                ShareDialog(
                    text = homeVM.state.sharedContent,
                    onSend = {
                        homeVM.event(HomeEvents.OnShare)
                    },
                    onTextChange = {
                        homeVM.event(HomeEvents.OnContentChange(it))
                    }, onTypeChange = {
                        homeVM.event(HomeEvents.OnTypeChange(it))
                    },
                    onDismiss = {
                        homeVM.event(HomeEvents.OnDismissDialog)
                    }
                )
            }
            BottomShow(
                state = sheetState,
                comments = homeVM.state.comments,
                text = homeVM.state.comment,
                onSendComment = {
                    homeVM.event(HomeEvents.OnSendComment(homeVM.state.selectedPost))
                },
                onValueChange = {
                    homeVM.event(HomeEvents.OnTypingComment(it))
                },
                onDismiss = {
                    coroutine.launch {
                        sheetState.hide()
                    }
                    homeVM.event(HomeEvents.OnDismissBottomSheet)
                }
            )
            HomeScreen(
                state = homeVM.state,
                onLogout = {
                    context.savePref(
                        false,
                        ""
                    )
                    navController.navigate(
                        LoginScreenDest
                    ) {
                        launchSingleTop = true
                        popUpTo<Navigation.Home> {
                            inclusive = true
                        }
                    }
                },
                onProfileClicked = {
                    navController.navigate(
                        Navigation.Profile(
                            email = it.editor.email,
                            hisAccount = false,
                        )
                    )
                }
            ) {
                when (it) {
                    is HomeEvents.OnNavigateIntoScreen -> {
                        // navigate into details screen
                        navController.navigate(
                            Navigation.Details(
                                it.post.postId,
                                it.post.editor.email,
                                it.post.isShared,
                            )
                        )
                    }

                    is HomeEvents.OnNavigate -> {
                        when (it.navigationItem.title) {
                            Screens.HOME -> {}
                            Screens.FRIENDS -> {
                                navController.navigate(
                                    Navigation.Friends(
                                        homeVM.state.user.email,
                                    )
                                ) {
                                    launchSingleTop = true
                                }
                            }

                            Screens.PROFILE -> {
                                navController.navigate(
                                    Navigation.Profile(
                                        email = homeVM.state.user.email,
                                        hisAccount = true,
                                    )
                                ) {
                                    launchSingleTop = true
                                }
                            }
                        }
                    }

                    else -> {
                        homeVM.event(it)
                    }
                }
            }
        }
        composable<Navigation.Profile>(typeMap = mapOf(typeOf<NavUser>() to userNavInfo)) {
            val profileVm = hiltViewModel<ProfileVM>()
            val state = profileVm.state
            val sheetState = rememberModalBottomSheetState(
                skipPartiallyExpanded = true
            )
            val coroutine = rememberCoroutineScope()
            LaunchedEffect(profileVm.state.showComment) {
                if (profileVm.state.showComment)
                    coroutine.launch {
                        sheetState.show()
                    }
            }
            if (profileVm.state.showShareDialog) {
                ShareDialog(
                    text = state.sharedContent,
                    onSend = {
                        profileVm.event(ProfileEvents.OnShare)
                    },
                    onTextChange = {
                        profileVm.event(ProfileEvents.OnTypingSharedContent(it))
                    }, onTypeChange = {
                        profileVm.event(ProfileEvents.OnTypeChange(it))
                    },
                    onDismiss = {
                        profileVm.event(ProfileEvents.OnDismissDialog)
                    }
                )
            }
            BottomShow(
                state = sheetState,
                comments = state.comments,
                text = state.comment,
                onSendComment = {
                    profileVm.event(ProfileEvents.OnSendComment(state.selectedPost))
                },
                onValueChange = {
                    profileVm.event(ProfileEvents.OnTypingComment(it))
                },
                onDismiss = {
                    coroutine.launch {
                        sheetState.hide()
                    }
                    profileVm.event(ProfileEvents.OnDismissBottomSheet)
                }
            )
            ProfileScreen(
                state = state,
                addFriendClick = { profileVm.event(ProfileEvents.OnAddFriend) },
                addCloseClick = { profileVm.event(ProfileEvents.OnAddClose) },
                removeFriendClick = { profileVm.event(ProfileEvents.OnRemoveFriend(it)) },
                onPostClicked = {
                    // navigation
                    when(state.hisAccount){
                        true -> {
                            navController.navigate(
                                Navigation.Details(
                                    it.postId,
                                    state.baseUser.email,
                                    it.isShared,
                                )
                            )
                        }
                        false -> {
                            navController.navigate(
                                Navigation.Details(
                                    it.postId,
                                    state.user.email,
                                    it.isShared,
                                )
                            )
                        }
                    }
                },
                onLikeClicked = { post, isLiked ->
                    profileVm.event(ProfileEvents.OnLikeClicked(post, isLiked))
                },
                onCommentClicked = {
                    profileVm.event(ProfileEvents.OnCommentClicked(it))
                },
                onShareClicked = {
                    profileVm.event(ProfileEvents.OnShareClicked(it))
                }, onProfileClicked = {
                    navController.navigate(
                        Navigation.Profile(
                            email = it.editor.email,
                            hisAccount = false,
                        )
                    )
                }
            )
        }
        composable<Navigation.Friends>(typeMap = mapOf(typeOf<NavUser>() to userNavInfo)) {
            val vm = hiltViewModel<FriendsVM>()
            val state = vm.state
            FriendsScreen(
                state = state,
                onAcceptFriendShip = {
                    vm.event(FriendsEvents.Accept(it))
                },
                onRejectFriendShip = {
                    vm.event(FriendsEvents.Reject(it))
                },
                onRefresh = {
                    vm.event(FriendsEvents.Refresh)
                }
            )
        }
        composable<Navigation.Details>(typeMap = mapOf(typeOf<NavUser>() to userNavInfo)) {
            val detailsVM = hiltViewModel<DetailsVm>()
            val state = detailsVM.state
            if (state.showShare) {
                ShareDialog(
                    onSend = { detailsVM.event(DetailsEvents.OnSharePost) },
                    onTextChange = { detailsVM.event(DetailsEvents.OnContentChange(it)) },
                    onTypeChange = { detailsVM.event(DetailsEvents.OnTypeChange(it)) },
                    onDismiss = { detailsVM.event(DetailsEvents.OnDismiss) },
                    text = state.shareContent
                )
            }
            DetailsScreen(state = state) { event ->
                when (event) {
                    is DetailsEvents.OnProfileClicked -> {
                        navController.navigate(
                            Navigation.Profile(
                                email = if (state.post.isShared) state.user.email else state.post.editor.email,
                                hisAccount = true,
                            )
                        )
                    }

                    else -> {
                        detailsVM.event(event)
                    }
                }
            }
        }
    }
}

private fun getUser(
    arg: Navigation.Home,
    context: Context
) = if (arg.email != "")
    User(
        name = arg.name,
        email = arg.email,
        password = arg.password,
        age = arg.age,
        phoneNumber = arg.phoneNumber,
        gender = arg.gender,
        dateOfBirth = arg.dateOfBirth
    ) else User(
    name = arg.name,
    email = context.getEmail(),
    password = arg.password,
    age = -1f,
    phoneNumber = arg.phoneNumber,
    gender = arg.gender,
    dateOfBirth = arg.dateOfBirth
)


sealed class Navigation {
    @Serializable
    object LoginScreenDest

    @Serializable
    object RegisterDest

    @Serializable
    data class Home(
        val name: String = "",
        val email: String = "",
        val password: String = "",
        val age: Float = 0.0f,
        val phoneNumber: String = "",
        val gender: String = "",
        val dateOfBirth: String = ""
    )

    @Serializable
    data class Details(
        val postId: Int,
        val user: String,
        val isShared: Boolean,
    )

    @Serializable
    data class Profile(
        val email: String,
        val hisAccount: Boolean
    )

    @Serializable
    data class Friends(
        val email: String
    )
}