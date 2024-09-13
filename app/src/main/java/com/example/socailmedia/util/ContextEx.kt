package com.example.socailmedia.util

import android.content.Context

fun Context.savePref(login: Boolean, email: String) {
    val sharedPreferences = getSharedPreferences(
        "login",
        Context.MODE_PRIVATE
    )
    sharedPreferences.edit()
        .putBoolean("isLogin", login)
        .putString("email", email)
        .apply()
}

fun Context.isLogin(): Boolean {
    return getSharedPreferences("login", Context.MODE_PRIVATE).getBoolean(
        "isLogin",
        false
    )
}

fun Context.getEmail(): String {
    return getSharedPreferences("login", Context.MODE_PRIVATE).getString(
        "email",
        ""
    ) ?: ""
}
