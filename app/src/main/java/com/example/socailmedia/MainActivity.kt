package com.example.socailmedia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.socailmedia.presentation.navigation.NavigationController
import com.example.socailmedia.ui.theme.SocailMediaTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SocailMediaTheme {
                val navController = rememberNavController()
                Scaffold(Modifier.fillMaxSize()) {padding->
                    NavigationController(navController = navController)
                }
            }
        }
    }

    @Serializable
    object UserNameScreen

    @Serializable
    data class ChatScreen(val sender: String, val receiver: String)
    /*
    * composable<UserNameScreen> {
                        val userNameVm = hiltViewModel<UserNameVm>()
                        val state = userNameVm.state
                        UserNameScreen(state = state) { event ->
                            when (event) {
                                UserNameEvents.OnJoinChat -> {
                                    navController.navigate(
                                        ChatScreen(
                                            state.userName
                                        )
                                    ) {
                                        launchSingleTop = true
                                    }
                                    userNameVm.navigate()
                                }

                                else -> userNameVm.event(event)
                            }
                        }
                    }
    * */
}