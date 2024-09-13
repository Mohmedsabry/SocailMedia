@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.socailmedia.presentation.register

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.socailmedia.R
import com.example.socailmedia.domain.util.Result
import com.example.socailmedia.presentation.compenent.DropDown
import com.example.socailmedia.presentation.compenent.Loading
import com.example.socailmedia.ui.theme.SocailMediaTheme
import com.example.socailmedia.util.CommonError
import com.example.socailmedia.util.EmailError
import com.example.socailmedia.util.PasswordError
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp
import java.text.SimpleDateFormat
import java.util.Date

@SuppressLint("SimpleDateFormat")
@Composable
fun RegisterScreen(
    registerState: RegisterState,
    isEmailError: Result<Unit, EmailError>,
    isPassWordError: Result<Unit, PasswordError>,
    isPhoneError: Result<Unit, CommonError>,
    onValueChange: (Typing, String) -> Unit,
    onRegister: () -> Unit
) {
    var showPassword by remember {
        mutableStateOf(false)
    }
    var showDate by remember {
        mutableStateOf(false)
    }
    var showConfirmPassword by remember {
        mutableStateOf(false)
    }
    val date = rememberDatePickerState()
    if (registerState.isLoading) Loading()
    val focusRequester = remember { FocusRequester() }
    Scaffold { inner ->
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(inner),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                OutlinedTextField(
                    value = registerState.name,
                    onValueChange = { onValueChange(Typing.NAME, it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.sdp),
                    label = { Text("Name") },
                )
                Spacer(Modifier.height(5.sdp))
                OutlinedTextField(
                    value = registerState.email,
                    onValueChange = { onValueChange(Typing.EMAIL, it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.sdp),
                    label = { Text("Email") },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = null
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    isError = isEmailError is Result.Failure && registerState.registeredClicked,
                    readOnly = registerState.isLoading,
                )
                Spacer(Modifier.height(5.sdp))
                AnimatedVisibility(visible = isEmailError is Result.Failure && registerState.registeredClicked) {
                    when (isEmailError) {
                        is Result.Failure -> {
                            when (isEmailError.error) {
                                EmailError.EMPTY -> Text(
                                    text = "please fill all fields",
                                    color = Color.Red,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.ssp
                                )

                                EmailError.NOT_MATCH_PATTERN -> Text(
                                    text = "please end text with @gmail.com",
                                    color = Color.Red,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.ssp
                                )

                                EmailError.EXIST -> Text(
                                    text = "Email is Exist",
                                    color = Color.Red,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.ssp
                                )
                            }
                        }

                        is Result.Success -> {}
                    }
                }
                Spacer(Modifier.height(5.sdp))
                OutlinedTextField(
                    value = registerState.password,
                    onValueChange = { onValueChange(Typing.PASSWORD, it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.sdp),
                    label = { Text("Password") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    ), trailingIcon = {
                        Icon(
                            painter = if (showPassword) painterResource(id = R.drawable.bi_eye) else painterResource(
                                id = R.drawable.eye_invisible
                            ), contentDescription = null,
                            modifier = Modifier
                                .clickable {
                                    showPassword = showPassword.not()
                                }
                                .size(25.sdp)
                        )
                    },
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    isError = isPassWordError is Result.Failure && registerState.registeredClicked,
                    readOnly = registerState.isLoading
                )
                Spacer(Modifier.height(5.sdp))
                AnimatedVisibility(visible = isPassWordError is Result.Failure && registerState.registeredClicked) {
                    when (isPassWordError) {
                        is Result.Failure -> {
                            when (isPassWordError.error) {
                                PasswordError.EMPTY -> Text(
                                    text = "please fill all fields",
                                    color = Color.Red,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.ssp
                                )

                                PasswordError.TOO_SHORT -> Text(
                                    text = "please insure password contains 8 letters or more",
                                    color = Color.Red,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.ssp,
                                    textAlign = TextAlign.Center
                                )

                                PasswordError.NO_UPPERCASE -> Text(
                                    text = "please insure there is uppercase letter from A-Z",
                                    color = Color.Red,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.ssp
                                )

                                PasswordError.NO_DIGIT -> Text(
                                    text = "please insure there is digit from 0-9",
                                    color = Color.Red,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.ssp
                                )
                            }
                        }

                        is Result.Success -> {}
                    }
                }
                Spacer(Modifier.height(5.sdp))
                OutlinedTextField(
                    value = registerState.confirmPassword,
                    onValueChange = { onValueChange(Typing.CONFIRM_PASSWORD, it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.sdp),
                    label = { Text("ConfirmPassword") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    ), trailingIcon = {
                        Icon(
                            painter = if (showConfirmPassword) painterResource(id = R.drawable.bi_eye) else painterResource(
                                id = R.drawable.eye_invisible
                            ), contentDescription = null,
                            modifier = Modifier
                                .clickable {
                                    showConfirmPassword = showConfirmPassword.not()
                                }
                                .size(25.sdp)
                        )
                    },
                    visualTransformation = if (showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    isError = (registerState.confirmPassword != registerState.password && registerState.registeredClicked) || (registerState.confirmPassword.isBlank() && registerState.registeredClicked) || (registerState.confirmPassword.isEmpty() && registerState.registeredClicked),
                    readOnly = registerState.isLoading
                )
                Spacer(Modifier.height(5.sdp))
                AnimatedVisibility(visible = registerState.confirmPassword != registerState.password && registerState.registeredClicked) {
                    Text(
                        text = "please insure confirm password like password",
                        color = Color.Red,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.ssp,
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(Modifier.height(5.sdp))
                OutlinedTextField(
                    value = registerState.phoneNumber,
                    onValueChange = { onValueChange(Typing.PHONE, it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.sdp),
                    label = { Text("Phone") },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = null
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    isError = isPhoneError is Result.Failure && registerState.registeredClicked,
                    readOnly = registerState.isLoading
                )
                Spacer(Modifier.height(5.sdp))
                AnimatedVisibility(visible = isPhoneError is Result.Failure && registerState.registeredClicked) {
                    when (isPhoneError) {
                        is Result.Failure -> {
                            when (isPhoneError.error) {
                                CommonError.EMPTY -> Text(
                                    text = "please fill all fields",
                                    color = Color.Red,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.ssp
                                )

                                CommonError.NOT_MATCH_PATTERN -> Text(
                                    text = "please insure phone is 010/011/012/015 followed by 8 digits",
                                    color = Color.Red,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.ssp,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        is Result.Success -> {}
                    }
                }
                Spacer(Modifier.height(5.sdp))
                // gender
                DropDown(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.sdp),
                    isError = registerState.gender == "" && registerState.registeredClicked
                ) {
                    onValueChange(Typing.GENDER, it)
                }
                Spacer(Modifier.height(10.sdp))
                Text(
                    text = "Please select your Birth date ${registerState.dateOfBirth}",
                    modifier = Modifier.clickable {
                        showDate = true
                    })
                Spacer(Modifier.height(5.sdp))
                AnimatedVisibility(visible = showDate) {
                    DatePickerDialog(onDismissRequest = { showDate = false }, confirmButton = {
                        Button(onClick = {
                            showDate = false
                            date.selectedDateMillis?.let {
                                val dateFormat = SimpleDateFormat("yyyy-MM-dd")
                                onValueChange(Typing.DATE, dateFormat.format(Date(it)))
                            }
                        }) {
                            Text(text = "Confirm")
                        }
                    }) {
                        DatePicker(state = date, showModeToggle = false)
                    }
                }
                Spacer(Modifier.height(5.sdp))
                OutlinedButton(onClick = { onRegister() }) {
                    Text(text = "SignUp")
                }
                Spacer(Modifier.height(5.sdp))
                AnimatedVisibility(visible = registerState.error != null) {
                    registerState.error?.let {
                        Text(
                            text = it,
                            color = Color.Red,
                            overflow = TextOverflow.Ellipsis,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.ssp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RegisterPrev() {
    SocailMediaTheme {
        RegisterScreen(
            registerState = RegisterState(confirmPassword = "s", registered = true),
            isEmailError = Result.Failure(EmailError.EMPTY),
            isPassWordError = Result.Failure(PasswordError.NO_UPPERCASE),
            isPhoneError = Result.Failure(CommonError.NOT_MATCH_PATTERN),
            onValueChange = { _, _ ->

            }
        ) {

        }
    }
}