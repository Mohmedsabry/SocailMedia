package com.example.socailmedia.data.remote

import com.example.socailmedia.data.remote.dto.LoginDto
import com.example.socailmedia.data.remote.dto.RegisterDto
import com.example.socailmedia.data.remote.dto.UserDto
import com.example.socailmedia.domain.model.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {
    @POST("register")
    suspend fun register(
        @Body registerDto: RegisterDto
    )

    @POST("login")
    suspend fun login(
        @Body loginDto: LoginDto
    ): UserDto

    @GET("/emails")
    suspend fun getAllEmails(): List<String>

    @GET("/user")
    suspend fun getUserByEmail(@Query("email") email: String): UserDto

    companion object {
        const val API_URL = "http://10.0.2.2:8080/"
    }
}