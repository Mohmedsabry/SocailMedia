package com.example.socailmedia.presentation.login

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.socailmedia.R
import com.example.socailmedia.presentation.compenent.Loading
import com.example.socailmedia.ui.theme.SocailMediaTheme
import com.example.socailmedia.util.AuthErrors
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun LoginScreen(
    state: LoginState,
    onTypingEmail: (String) -> Unit,
    onTypingPassword: (String) -> Unit,
    onLoginClicked: () -> Unit,
    onSignUpClicked: () -> Unit
) {
    var showPassword by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    if (state.isLoading) Loading()
    LaunchedEffect(state.error != null) {
        println("error ${state.error}")
        state.error?.let {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.sdp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = state.email,
                onValueChange = { onTypingEmail(it) },
                enabled = state.isLoading.not(),
                placeholder = { Text(text = "Email", fontSize = 16.ssp) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        modifier = Modifier.size(25.sdp)
                    )
                },
                isError = (state.errorType?.equals(AuthErrors.EMAIL_IS_WRONG)
                    ?: false) || (state.error == "please fill all fields"),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.sdp)
            )
            OutlinedTextField(
                value = state.password,
                onValueChange = { onTypingPassword(it) },
                enabled = state.isLoading.not(),
                placeholder = { Text(text = "Password", fontSize = 16.ssp) },
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
                isError = state.errorType?.equals(AuthErrors.PASSWORD_IS_WRONG) ?: false || (state.error == "please fill all fields"),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.sdp)
            )
            OutlinedButton(
                onClick = { onLoginClicked() },
                enabled = state.isLoading.not()
            ) {
                Text(text = "Login", fontSize = 16.ssp)
            }
            AnimatedVisibility(visible = state.error != null) {
                Text(
                    text = state.error.toString(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.ssp,
                    color = Color.Red
                )
            }
            Row {
                Text(
                    text = "don't have account?",
                    fontSize = 18.ssp,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "Signup",
                    fontSize = 17.ssp,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable { onSignUpClicked() }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginPrev() {
    SocailMediaTheme {
        LoginScreen(state = LoginState(), {}, {}, {}) {}
    }
}