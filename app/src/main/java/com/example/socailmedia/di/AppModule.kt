package com.example.socailmedia.di

import com.example.socailmedia.data.remote.AuthApi
import com.example.socailmedia.data.remote.ChatAPi
import com.example.socailmedia.data.remote.FriendsApi
import com.example.socailmedia.data.remote.PostApi
import com.example.socailmedia.domain.validation.UserValidation
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.websocket.WebSockets
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideAuthApi(): AuthApi = Retrofit.Builder().baseUrl(AuthApi.API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory()).build().create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideChatApi(): ChatAPi = Retrofit.Builder().baseUrl(ChatAPi.API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory()).build()
        .create(ChatAPi::class.java)

    @Provides
    @Singleton
    fun provideValidation(): UserValidation = UserValidation()

    @Provides
    @Singleton
    fun providePostApi(): PostApi = Retrofit.Builder().baseUrl(PostApi.API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory()).build().create(PostApi::class.java)

    @Provides
    @Singleton
    fun provideFriendsApi(): FriendsApi = Retrofit.Builder().baseUrl(PostApi.API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory()).build()
        .create(FriendsApi::class.java)

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient = HttpClient(CIO) {
        install(WebSockets)
        install(ContentNegotiation) {

        }
    }
}