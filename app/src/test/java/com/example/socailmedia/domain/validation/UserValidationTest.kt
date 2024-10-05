package com.example.socailmedia.domain.validation

import com.example.socailmedia.domain.util.Result
import com.example.socailmedia.util.CommonError
import com.example.socailmedia.util.EmailError
import com.example.socailmedia.util.PasswordError
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class UserValidationTest {
    private lateinit var userValidation: UserValidation
    private lateinit var remoteList: List<String>

    @Before
    fun setUp() {
        userValidation = UserValidation()
        remoteList = listOf("ali", "alaa")
    }

    @Test
    fun `check password is empty fail`() {
        val result =
            (userValidation.validatePassword("")) as Result.Failure
        assertThat(result.error).isEqualTo(PasswordError.EMPTY)
    }

    @Test
    fun `check password is less than 8 length fail`() {
        val result =
            (userValidation.validatePassword("1234567")) as Result.Failure
        assertThat(result.error).isEqualTo(PasswordError.TOO_SHORT)
    }

    @Test
    fun `check password is no uppercase`() {
        val result =
            (userValidation.validatePassword("mohmed12")) as Result.Failure
        assertThat(result.error).isEqualTo(PasswordError.NO_UPPERCASE)
    }

    @Test
    fun `check password is no digit`() {
        val result =
            (userValidation.validatePassword("Mohmedsabry")) as Result.Failure
        assertThat(result.error).isEqualTo(PasswordError.NO_DIGIT)
    }

    @Test
    fun `check password is valid`() {
        val result =
            (userValidation.validatePassword("Mohmed19"))
        assertThat(result).isEqualTo(Result.Success<Unit, PasswordError>(Unit))
    }

    @Test
    fun `check email if is empty not valid`() {
        val result = userValidation.validateEmail(
            "",
            remoteList
        ) as Result.Failure
        assertThat(result.error).isEqualTo(EmailError.EMPTY)
    }

    @Test
    fun `check email if is exist not valid`() {
        val result = userValidation.validateEmail(
            "ali",
            remoteList
        ) as Result.Failure
        assertThat(result.error).isEqualTo(EmailError.EXIST)
    }

    @Test
    fun `check email if is not match pattern not valid`() {
        val result = userValidation.validateEmail(
            "mohmed",
            remoteList
        ) as Result.Failure
        assertThat(result.error).isEqualTo(EmailError.NOT_MATCH_PATTERN)
    }

    @Test
    fun `check email if is valid`() {
        val result = userValidation.validateEmail(
            "mohmed@gmail.com",
            remoteList
        )
        assertThat(result).isEqualTo(Result.Success<Unit, EmailError>(Unit))
    }

    @Test
    fun `check phoneNumber if is empty not valid`() {
        val result = userValidation.validatePhoneNumber(
            "",
        ) as Result.Failure
        assertThat(result.error).isEqualTo(CommonError.EMPTY)
    }

    @Test
    fun `check phoneNumber if is not match pattern not valid`() {
        val result = userValidation.validatePhoneNumber(
            "2651200",
        ) as Result.Failure
        assertThat(result.error).isEqualTo(CommonError.NOT_MATCH_PATTERN)
    }

    @Test
    fun `check phoneNumber if is not match pattern2 not valid`() {
        val result = userValidation.validatePhoneNumber(
            "0128614855",
        ) as Result.Failure
        assertThat(result.error).isEqualTo(CommonError.NOT_MATCH_PATTERN)
    }

    @Test
    fun `check phoneNumber if is valid`() {
        val result = userValidation.validatePhoneNumber(
            "01286148550",
        )
        assertThat(result).isEqualTo(Result.Success<Unit, CommonError>(Unit))
    }
}