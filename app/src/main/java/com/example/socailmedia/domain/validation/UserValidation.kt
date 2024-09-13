package com.example.socailmedia.domain.validation


import com.example.socailmedia.domain.util.Result
import com.example.socailmedia.util.CommonError
import com.example.socailmedia.util.EmailError
import com.example.socailmedia.util.PasswordError
import java.util.regex.Pattern

class UserValidation {
    fun validatePassword(password: String): Result<Unit, PasswordError> {
        if (password.isEmpty()) return Result.Failure(PasswordError.EMPTY)
        if (password.length < 8) return Result.Failure(PasswordError.TOO_SHORT)
        val hasUpper = password.any { it.isUpperCase() }
        if (!hasUpper) return Result.Failure(PasswordError.NO_UPPERCASE)
        val hasDigit = password.any { it.isDigit() }
        if (!hasDigit) return Result.Failure(PasswordError.NO_DIGIT)
        return Result.Success(Unit)
    }

    fun validateEmail(
        email: String,
        remoteEmails: List<String>
    ): Result<Unit, EmailError> {
        if (email.isEmpty()) return Result.Failure(EmailError.EMPTY)
        if (remoteEmails.contains(email)) return Result.Failure(EmailError.EXIST)
        val matches = Pattern.matches(
            "^[a-zA-Z][a-zA-Z0-9.]*@gmail\\.com\$", email
        )
        if (matches.not()) return Result.Failure(EmailError.NOT_MATCH_PATTERN)
        return Result.Success(Unit)
    }

    fun validatePhoneNumber(
        phone: String
    ): Result<Unit, CommonError> {
        if (phone.isEmpty()) return Result.Failure(CommonError.EMPTY)
        val pattern = Pattern.compile("01[0512][0-9]{8}")
        if (!pattern.matcher(phone).matches()) return Result.Failure(CommonError.NOT_MATCH_PATTERN)
        return Result.Success(Unit)
    }
}