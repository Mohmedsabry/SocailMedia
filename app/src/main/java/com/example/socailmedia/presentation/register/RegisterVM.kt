package com.example.socailmedia.presentation.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socailmedia.domain.model.User
import com.example.socailmedia.domain.repositories.AuthRepository
import com.example.socailmedia.domain.util.Result
import com.example.socailmedia.domain.validation.UserValidation
import com.example.socailmedia.util.CommonError
import com.example.socailmedia.util.EmailError
import com.example.socailmedia.util.PasswordError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterVM @Inject constructor(
    private val authRepository: AuthRepository,
    private val userValidation: UserValidation
) : ViewModel() {
    var state by mutableStateOf(RegisterState())
        private set
    init {
        viewModelScope.launch {
            state = when (val res = authRepository.getAllEmailsInSystem()) {
                is Result.Failure -> {
                    state.copy(
                        error = res.error.name,
                    )
                }

                is Result.Success -> {
                    state.copy(
                        error = null,
                        emails = res.data
                    )
                }
            }
            println(state.emails)
        }
    }

    fun validateEmail(): Result<Unit, EmailError> {
        return userValidation.validateEmail(state.email, state.emails)
    }

    fun validatePassword(): Result<Unit, PasswordError> {
        return userValidation.validatePassword(state.password)
    }

    fun validatePhone(): Result<Unit, CommonError> {
        return userValidation.validatePhoneNumber(state.phoneNumber)
    }

    fun event(events: RegisterEvents) {
        when (events) {
            RegisterEvents.OnRegisterClicked -> {
                state = state.copy(
                    isLoading = true,
                    registeredClicked = true,
                    error = null
                )
                if (validatePhone() is Result.Success && validateEmail() is Result.Success && validatePassword() is Result.Success && state.gender != "" && state.dateOfBirth != "") {
                    viewModelScope.launch {
                        val user = User(
                            name = state.name,
                            email = state.email,
                            password = state.password,
                            phoneNumber = state.phoneNumber,
                            gender = state.gender,
                            dateOfBirth = state.dateOfBirth
                        )
                        when (val res = authRepository.register(user)) {
                            is Result.Failure -> {
                                state = state.copy(
                                    error = res.error.name,
                                    isLoading = false,
                                )
                            }

                            is Result.Success -> {
                                state = state.copy(
                                    error = null,
                                    isLoading = false,
                                    registered = true
                                )
                            }
                        }
                    }
                } else {
                    if (state.gender.isEmpty() || state.gender.isBlank()) state = state.copy(
                        error = "please select your gender",
                        isLoading = false
                    )
                    if (state.dateOfBirth.isEmpty() || state.dateOfBirth.isBlank()) {
                        state = if (state.error != null)
                            state.copy(
                                error = state.error.plus("\nPlease select Your Date"),
                                isLoading = false
                            )
                        else state.copy(
                            error = "Please select Your Date",
                            isLoading = false
                        )
                    }
                }
                state = state.copy(
                    isLoading = false
                )
            }

            is RegisterEvents.OnSelectGender -> state = state.copy(gender = events.gender)
            is RegisterEvents.OnTyping -> {
                state = when (events.typing) {
                    Typing.NAME -> state.copy(name = events.text)
                    Typing.PASSWORD -> state.copy(password = events.text)
                    Typing.CONFIRM_PASSWORD -> state.copy(confirmPassword = events.text)
                    Typing.EMAIL -> state.copy(email = events.text)
                    Typing.PHONE -> state.copy(phoneNumber = events.text)
                    Typing.DATE -> state.copy(dateOfBirth = events.text)
                    Typing.GENDER -> state.copy(gender = events.text)
                }
            }
        }
    }
}