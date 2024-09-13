package com.example.socailmedia.di

import com.example.socailmedia.data.repositories.AuthRepoImpl
import com.example.socailmedia.data.repositories.RepositoryImpl
import com.example.socailmedia.domain.repositories.AuthRepository
import com.example.socailmedia.domain.repositories.Repository
import com.example.socailmedia.domain.validation.UserValidation
import dagger.Binds
import dagger.Module
import dagger.Provides
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
    abstract fun bindRepo(repositoryImpl: RepositoryImpl):Repository
}