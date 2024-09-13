package com.example.socailmedia.data.remote

import com.example.socailmedia.data.remote.dto.CommentDto
import com.example.socailmedia.data.remote.dto.PostDto
import com.example.socailmedia.data.remote.dto.PostUpload
import com.example.socailmedia.data.remote.dto.SharePostDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface PostApi {

    @GET("/posts")
    suspend fun getAllPosts(
        @Query("email") email: String
    ): List<PostDto>

    @GET("/post/{id}")
    suspend fun getPostById(
        @Path("id") id: Int,
        @Query("isShared") isShared: Boolean
    ): PostDto

    @POST("/post")
    suspend fun sendPost(
        @Body postUpload: PostUpload
    )

    @PUT("/post/{id}")
    suspend fun updatePostById(
        @Path("id") id: Int,
        @Query("content") content: String
    )

    @DELETE("/post/{id}")
    suspend fun deletePostById(
        @Path("id") id: Int
    )

    @GET("/profile")
    suspend fun getMyPosts(
        @Query("email") email: String
    ): List<PostDto>

    @POST("/comment/{id}")
    suspend fun addComment(
        @Path("id") id: Int,
        @Query("comment") comment: String,
        @Query("email") email: String,
        @Query("isShared") isShared: Boolean,
    )

    @GET("/comment/{id}")
    suspend fun getComments(
        @Path("id") id: Int,
    ): List<CommentDto>

    @PUT("/comment/{id}")
    suspend fun updateComment(
        @Path("id") id: Int,
        @Query("comment") comment: String,
        @Query("email") email: String,
    )

    @DELETE("/comment/{id}")
    suspend fun deleteComment(
        @Path("id") id: Int,
        @Query("email") email: String,
    )

    @POST("/share")
    suspend fun sharePost(
        @Body sharePostDto: SharePostDto
    )

    @PUT("/share")
    suspend fun updateShare(
        @Body sharePostDto: SharePostDto
    )

    @DELETE("/share")
    suspend fun deleteShare(
        @Body sharePostDto: SharePostDto
    )

    @PUT("/like/{id}")
    suspend fun updatePostLikeStatues(
        @Path("id") id: Int,
        @Query("value") value: Int,
        @Query("email") email: String,
        @Query("isShared") isShared: Boolean
    )

    companion object {
        const val API_URL = "http://10.0.2.2:8080/"
    }
}