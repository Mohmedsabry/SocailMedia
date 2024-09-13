package com.example.socailmedia.domain.repositories

import com.example.socailmedia.domain.model.User
import com.example.socailmedia.domain.util.Result
import com.example.socailmedia.util.AuthErrors

interface AuthRepository {
    suspend fun register(user: User): Result<Unit, AuthErrors>
    suspend fun login(email: String, password: String): Result<User, AuthErrors>
    suspend fun getAllEmailsInSystem(): Result<List<String>, AuthErrors>
    suspend fun getUserByEmail(email: String): Result<User, AuthErrors>
    suspend fun getActiveUser(): Result<User, AuthErrors>
}