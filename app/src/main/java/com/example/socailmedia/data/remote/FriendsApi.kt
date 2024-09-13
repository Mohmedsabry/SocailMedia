package com.example.socailmedia.data.remote

import com.example.socailmedia.data.remote.dto.FriendsDto
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface FriendsApi {
    @GET("/friends")
    suspend fun getAllFriends(
        @Query("email") email: String
    ): List<FriendsDto>
    @GET("/request")
    suspend fun getFriendsRequests(
        @Query("email") email: String
    ): List<FriendsDto>

    @POST("/friends")
    suspend fun addFriend(
        @Query("user1") user1: String,
        @Query("user2") user2: String
    )

    @PUT("/friends")
    suspend fun updateFriendShip(
        @Query("user1") user1: String,
        @Query("user2") user2: String,
        @Query("statues") statues: String,
    ):String

    @PUT("/close")
    suspend fun addToClose(
        @Query("user1") user1: String,
        @Query("user2") user2: String,
        @Query("close") statues: Boolean,
    )

}