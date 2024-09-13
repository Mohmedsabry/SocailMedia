package com.example.socailmedia.data.mapper

import com.example.socailmedia.data.remote.dto.RegisterDto
import com.example.socailmedia.data.remote.dto.UserDto
import com.example.socailmedia.domain.model.User
import com.example.socailmedia.presentation.navigation.NavUser

fun User.toDto() = RegisterDto(
    name = name,
    email = email,
    password = password,
    phoneNumber = phoneNumber,
    gender = gender,
    dateOfBirth = dateOfBirth.toString()
)

fun UserDto.toUser() = User(
    name = name,
    email = email,
    password = password,
    age = age,
    phoneNumber = phoneNumber,
    gender = gender,
    dateOfBirth = dateOfBirth
)

fun NavUser.toUser(): User = User(
    name, email, password, age, phoneNumber, gender, dateOfBirth
)

fun User.toNavUser(): NavUser =
    NavUser(
        name, email, password, age, phoneNumber, gender, dateOfBirth
    )