package com.example.socailmedia.presentation.register

sealed interface RegisterEvents {
    data class OnTyping(val typing: Typing, val text: String) : RegisterEvents
    data object OnRegisterClicked : RegisterEvents
    data class OnSelectGender(val gender: String) : RegisterEvents
    data class OnSelectDate(val date: String)
}