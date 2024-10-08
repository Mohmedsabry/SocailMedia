package com.example.socailmedia.domain.util

import com.example.socailmedia.util.Error

sealed interface Result<D, E : Error> {
    data class Success<D, E : Error>(val data: D) : Result<D, E>
    data class Failure<D, E : Error>(val error: E) : Result<D, E>
}