package com.example.socailmedia.presentation.home

import com.example.socailmedia.R

enum class Screens(val title: String) {
    HOME("Home"), FRIENDS("Friends"), PROFILE("Profile")
}

data class NavigationItem(
    val title: Screens,
    val icon: Int
)

val navigationList = listOf(
    NavigationItem(
        Screens.HOME,
        R.drawable.baseline_home_24
    ),
    NavigationItem(
        Screens.FRIENDS,
        R.drawable.friends
    ),
    NavigationItem(
        Screens.PROFILE,
        R.drawable.profile
    ),
)