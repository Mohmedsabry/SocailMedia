package com.example.socailmedia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.socailmedia.presentation.navigation.NavigationController
import com.example.socailmedia.ui.theme.SocailMediaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SocailMediaTheme {
                val navController = rememberNavController()
                NavigationController(navController = navController)
            }
        }
    }
}
