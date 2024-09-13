package com.example.socailmedia.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socailmedia.domain.repositories.AuthRepository
import com.example.socailmedia.domain.util.Result
import com.example.socailmedia.util.AuthErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginVM @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    var state by mutableStateOf(LoginState())
    fun event(event: LoginEvent) {
        when (event) {
            LoginEvent.OnLoginClicked -> {
                state = state.copy(isLoading = true)
                viewModelScope.launch {
                    if ((state.email.isNotBlank() && state.email.isNotEmpty()) || (state.password.isNotBlank() && state.password.isNotEmpty()))
                        when (val res = authRepository.login(state.email, state.password)) {
                            is Result.Failure -> {
                                state = state.copy(
                                    errorType = res.error
                                )
                                when (res.error) {
                                    AuthErrors.UN_KNOWN -> {
                                        state =
                                            state.copy(error = "UnKnown error", isLoading = false)
                                    }

                                    AuthErrors.EMAIL_IS_WRONG -> {
                                        state =
                                            state.copy(error = "email is wrong", isLoading = false)
                                    }

                                    AuthErrors.PASSWORD_IS_WRONG -> state =
                                        state.copy(error = "password is wrong", isLoading = false)

                                    AuthErrors.INTERNAL_SERVER_ERROR -> state =
                                        state.copy(
                                            error = "there are error in server please try again",
                                            isLoading = false
                                        )

                                    AuthErrors.BAD_REQUEST -> state =
                                        state.copy(
                                            error = "please try again with check of date that may be email is already exist",
                                            isLoading = false
                                        )
                                }
                            }

                            is Result.Success -> {
                                state = state.copy(
                                    error = null,
                                    loginSuccessfully = true,
                                    isLoading = false,
                                    user = res.data,
                                    errorType = null
                                )
                            }
                        }
                    else state = state.copy(
                        error = "please fill all fields",
                        isLoading = false
                    )
                }
            }

            is LoginEvent.OnTypingEmail -> state = state.copy(email = event.email)
            is LoginEvent.OnTypingPassword -> state = state.copy(password = event.password)
        }
    }
}