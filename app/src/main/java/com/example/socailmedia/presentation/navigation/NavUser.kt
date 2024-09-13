package com.example.socailmedia.presentation.navigation

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
@Parcelize
data class NavUser(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val age: Float = 0.0f,
    val phoneNumber: String = "",
    val gender: String = "",
    val dateOfBirth: String = ""
) : Parcelable

val userNavInfo = object : NavType<NavUser>(false) {
    override fun get(bundle: Bundle, key: String): NavUser? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(key, NavUser::class.java)
        } else {
            bundle.getParcelable(key)
        }
    }

    override fun parseValue(value: String): NavUser {
        return Json.decodeFromString(value)
    }

    override fun put(bundle: Bundle, key: String, value: NavUser) {
        bundle.putParcelable(key, value)
    }

    override fun serializeAsValue(value: NavUser): String {
        Json.encodeToString(value)
        return super.serializeAsValue(value)
    }
}