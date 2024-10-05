package com.example.socailmedia.di

import com.example.socailmedia.data.remote.ChatSocketApi
import com.example.socailmedia.data.remote.ChatSocketImpl
import com.example.socailmedia.data.repositories.AuthRepoImpl
import com.example.socailmedia.data.repositories.ChatRepoImpl
import com.example.socailmedia.data.repositories.RepositoryImpl
import com.example.socailmedia.domain.repositories.AuthRepository
import com.example.socailmedia.domain.repositories.ChatRepository
import com.example.socailmedia.domain.repositories.Repository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {
    @Binds
    @Singleton
    abstract fun provideAuthRepo(authRepoImpl: AuthRepoImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindRepo(repositoryImpl: RepositoryImpl): Repository

    @Binds
    @Singleton
    abstract fun bindChatSocketApi(chatSocketImpl: ChatSocketImpl): ChatSocketApi

    @Binds
    @Singleton
    abstract fun bindChatRepo(chatRepoImpl: ChatRepoImpl): ChatRepository
}