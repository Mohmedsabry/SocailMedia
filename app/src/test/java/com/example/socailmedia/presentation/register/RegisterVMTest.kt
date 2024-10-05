package com.example.socailmedia.presentation.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.socailmedia.MainCoroutineRule
import com.example.socailmedia.data.repositories.FakeAuthRepoImplTest
import com.example.socailmedia.domain.validation.UserValidation
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class RegisterVMTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()
    private lateinit var vm: RegisterVM

    @Before
    fun setup() {
        vm = RegisterVM(FakeAuthRepoImplTest(), UserValidation())
    }

    @Test
    fun `test if networkError and get all emails return error`() {
        vm = RegisterVM(FakeAuthRepoImplTest(true), UserValidation())
        val result = vm.state.error
        assertThat(result).isNotNull()
    }

    @Test
    fun `test return emails success`() {
        val result = vm.state.emails
        assertThat(result).isNotEmpty()
    }

    @Test
    fun `test register gender fails if empty`() {
        vm.event(RegisterEvents.OnTyping(Typing.EMAIL,"mohamed@gmail.com"))
        vm.event(RegisterEvents.OnTyping(Typing.PASSWORD,"Mohammed19"))
        vm.event(RegisterEvents.OnTyping(Typing.CONFIRM_PASSWORD,"Mohammed19"))
        vm.event(RegisterEvents.OnTyping(Typing.PHONE,"01286148550"))
        vm.event(RegisterEvents.OnTyping(Typing.DATE,"2002-05-19"))
        vm.event(RegisterEvents.OnRegisterClicked)
        val result = vm.state.error
        assertThat(result).isNotNull()
    }

    @Test
    fun `test register birthDay fails if empty`() {
        vm.event(RegisterEvents.OnTyping(Typing.EMAIL,"mohamed@gmail.com"))
        vm.event(RegisterEvents.OnTyping(Typing.PASSWORD,"Mohammed19"))
        vm.event(RegisterEvents.OnTyping(Typing.CONFIRM_PASSWORD,"Mohammed19"))
        vm.event(RegisterEvents.OnTyping(Typing.PHONE,"01286148550"))
        vm.event(RegisterEvents.OnTyping(Typing.GENDER,"male"))
        vm.event(RegisterEvents.OnRegisterClicked)
        val result = vm.state.error
        assertThat(result).isNotNull()
    }

    @Test
    fun `test register pass`() {
        vm.event(RegisterEvents.OnTyping(Typing.EMAIL,"mohamed@gmail.com"))
        vm.event(RegisterEvents.OnTyping(Typing.PASSWORD,"Mohammed19"))
        vm.event(RegisterEvents.OnTyping(Typing.CONFIRM_PASSWORD,"Mohammed19"))
        vm.event(RegisterEvents.OnTyping(Typing.PHONE,"01286148550"))
        vm.event(RegisterEvents.OnTyping(Typing.GENDER,"male"))
        vm.event(RegisterEvents.OnTyping(Typing.DATE,"2002-05-19"))
        vm.event(RegisterEvents.OnRegisterClicked)
        val result = vm.state.registered
        assertThat(result).isTrue()
    }

}