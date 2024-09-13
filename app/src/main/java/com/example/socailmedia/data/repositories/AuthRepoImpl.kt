package com.example.socailmedia.data.repositories

import android.content.Context
import com.example.socailmedia.data.mapper.toDto
import com.example.socailmedia.data.mapper.toUser
import com.example.socailmedia.data.remote.AuthApi
import com.example.socailmedia.data.remote.dto.LoginDto
import com.example.socailmedia.domain.model.User
import com.example.socailmedia.domain.repositories.AuthRepository
import com.example.socailmedia.domain.util.Result
import com.example.socailmedia.util.AuthErrors
import com.example.socailmedia.util.getEmail
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

class AuthRepoImpl @Inject constructor(
    private val authApi: AuthApi,
    @ApplicationContext private val context: Context
) : AuthRepository {
    override suspend fun register(user: User): Result<Unit, AuthErrors> {
        return withContext(Dispatchers.IO) {
            try {
                val registerDto = user.toDto()
                println("register $registerDto")
                async { authApi.register(registerDto) }.await()
                Result.Success(Unit)
            } catch (e: HttpException) {
                e.printStackTrace()
                when (e.code()) {
                    501 -> {
                        return@withContext Result.Failure(AuthErrors.INTERNAL_SERVER_ERROR)
                    }

                    400 -> {
                        return@withContext Result.Failure(AuthErrors.BAD_REQUEST)
                    }

                    else -> return@withContext Result.Failure(AuthErrors.UN_KNOWN)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Failure(AuthErrors.UN_KNOWN)
            }
        }
    }

    override suspend fun getUserByEmail(email: String): Result<User, AuthErrors> {
        return withContext(Dispatchers.IO) {
            try {
                val remoteUser = authApi.getUserByEmail(email).toUser()
                Result.Success(remoteUser)
            } catch (e: HttpException) {
                e.printStackTrace()
                when (e.code()) {
                    501 -> {
                        return@withContext Result.Failure(AuthErrors.INTERNAL_SERVER_ERROR)
                    }

                    400 -> {
                        println(e.response()!!.headers()["error"])
                        if (e.response()!!
                                .headers()["error"] == "user is not found"
                        ) return@withContext Result.Failure(
                            AuthErrors.EMAIL_IS_WRONG
                        )
                        return@withContext Result.Failure(AuthErrors.BAD_REQUEST)
                    }

                    else -> return@withContext Result.Failure(AuthErrors.UN_KNOWN)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Failure(AuthErrors.UN_KNOWN)
            }
        }
    }

    override suspend fun getActiveUser(): Result<User, AuthErrors> {
        return withContext(Dispatchers.IO) {
            try {
                val remoteUser = authApi.getUserByEmail(
                    context.getEmail()
                ).toUser()
                Result.Success(remoteUser)
            } catch (e: HttpException) {
                e.printStackTrace()
                when (e.code()) {
                    501 -> {
                        return@withContext Result.Failure(AuthErrors.INTERNAL_SERVER_ERROR)
                    }

                    400 -> {
                        println(e.response()!!.headers()["error"])
                        if (e.response()!!
                                .headers()["error"] == "user is not found"
                        ) return@withContext Result.Failure(
                            AuthErrors.EMAIL_IS_WRONG
                        )
                        return@withContext Result.Failure(AuthErrors.BAD_REQUEST)
                    }

                    else -> return@withContext Result.Failure(AuthErrors.UN_KNOWN)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Failure(AuthErrors.UN_KNOWN)
            }
        }
    }

    override suspend fun login(
        email: String,
        password: String
    ): Result<User, AuthErrors> {
        return withContext(Dispatchers.IO) {
            try {
                val userDto = authApi.login(LoginDto(email, password))
                Result.Success(userDto.toUser())
            } catch (e: HttpException) {
                e.printStackTrace()
                when (e.code()) {
                    501 -> {
                        return@withContext Result.Failure(AuthErrors.INTERNAL_SERVER_ERROR)
                    }

                    400 -> {
                        println(e.response()!!.headers()["error"])
                        if (e.response()!!
                                .headers()["error"] == "user is not found"
                        ) return@withContext Result.Failure(
                            AuthErrors.EMAIL_IS_WRONG
                        )
                        if (e.response()!!
                                .headers()["error"] == "password is not correct"
                        ) return@withContext Result.Failure(
                            AuthErrors.PASSWORD_IS_WRONG
                        )
                        return@withContext Result.Failure(AuthErrors.BAD_REQUEST)
                    }

                    else -> return@withContext Result.Failure(AuthErrors.UN_KNOWN)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Failure(AuthErrors.UN_KNOWN)
            }
        }
    }

    override suspend fun getAllEmailsInSystem(): Result<List<String>, AuthErrors> {
        return withContext(Dispatchers.IO) {
            try {
                val list = authApi.getAllEmails()
                Result.Success(list)
            } catch (e: HttpException) {
                e.printStackTrace()
                when (e.code()) {
                    501 -> return@withContext Result.Failure(AuthErrors.INTERNAL_SERVER_ERROR)
                    400 -> return@withContext Result.Failure(AuthErrors.BAD_REQUEST)
                }
                Result.Failure(AuthErrors.UN_KNOWN)
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Failure(AuthErrors.UN_KNOWN)
            }
        }
    }
}