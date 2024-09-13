package com.example.socailmedia.util

import com.example.socailmedia.domain.model.TypeOfPost

fun String.toPost(): TypeOfPost {
    return when (this) {
        ConstObject.PRIVATE -> TypeOfPost.PRIVATE
        ConstObject.ONLY_FRIENDS -> TypeOfPost.ONLY_FRIENDS
        else -> TypeOfPost.PUBLIC
    }
}