package com.example.socailmedia.data.repositories

import com.example.socailmedia.domain.model.User
import com.example.socailmedia.domain.repositories.AuthRepository
import com.example.socailmedia.domain.util.Result
import com.example.socailmedia.util.AuthErrors


class FakeAuthRepoImplTest(
    private var shouldReturnNetworkError: Boolean = false
) : AuthRepository {

    fun setError(value: Boolean) {
        shouldReturnNetworkError = value
    }

    override suspend fun register(
        user: User
    ): Result<Unit, AuthErrors> {
        return if (shouldReturnNetworkError) {
            Result.Failure(AuthErrors.UN_KNOWN)
        } else {
            Result.Success(Unit)
        }
    }

    override suspend fun login(
        email: String,
        password: String
    ): Result<User, AuthErrors> {
        return if (shouldReturnNetworkError) {
            Result.Failure(AuthErrors.UN_KNOWN)
        } else {
            Result.Success(User(email = email, password = password))
        }
    }

    override suspend fun getAllEmailsInSystem(): Result<List<String>, AuthErrors> {
        return if (shouldReturnNetworkError) {
            Result.Failure(AuthErrors.UN_KNOWN)
        } else {
            Result.Success(listOf("ahmed"))
        }
    }

    override suspend fun getUserByEmail(
        email: String
    ): Result<User, AuthErrors> {
        return if (shouldReturnNetworkError) {
            Result.Failure(AuthErrors.UN_KNOWN)
        } else {
            Result.Success(User(email = email))
        }
    }

    override suspend fun getActiveUser(): Result<User, AuthErrors> {
        return if (shouldReturnNetworkError) {
            Result.Failure(AuthErrors.UN_KNOWN)
        } else {
            Result.Success(User("Active"))
        }
    }
}