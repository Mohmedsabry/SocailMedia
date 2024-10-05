package com.example.socailmedia.presentation.friends

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.example.socailmedia.MainCoroutineRule
import com.example.socailmedia.data.repositories.FakeRepositoryImplTest
import com.example.socailmedia.domain.model.User
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class FriendsVMTest {
    private lateinit var vm: FriendsVM
    private lateinit var fakeRepositoryImplTest: FakeRepositoryImplTest

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        fakeRepositoryImplTest = FakeRepositoryImplTest()
        vm = FriendsVM(
            fakeRepositoryImplTest,
            savedStateHandle = SavedStateHandle(
                mapOf(
                    pair = "email" to "mohammed"
                )
            )
        )
    }

    @Test
    fun `return user with email`() {
        val result = vm.state.users
        assertThat(result).contains(User(email = "mohammed"))
    }

    @Test
    fun `test accept pass`() {
        vm.event(FriendsEvents.Accept(User(email = "mohammed")))
        assertThat(vm.state.users).isEmpty()
        assertThat(vm.state.error).isNull()
    }

    @Test
    fun `test accept fails`() {
        fakeRepositoryImplTest.setError(true)
        vm.setRepository(fakeRepositoryImplTest)
        vm.event(FriendsEvents.Accept(User(email = "mohammed")))
        assertThat(vm.state.users).isEmpty()
        assertThat(vm.state.error).isNotNull()
    }

    @Test
    fun `test refuse pass`() {
        vm.event(FriendsEvents.Reject(User(email = "mohammed")))
        assertThat(vm.state.users).isEmpty()
        assertThat(vm.state.error).isNull()
    }

    @Test
    fun `test refuse fails`() {
        fakeRepositoryImplTest.setError(true)
        vm.setRepository(fakeRepositoryImplTest)
        vm.event(FriendsEvents.Reject(User(email = "mohammed")))
        assertThat(vm.state.users).isEmpty()
        assertThat(vm.state.error).isNotNull()
    }

    @Test
    fun `refresh return true`() {
        vm.event(FriendsEvents.Refresh)
        val res = vm.state.error
        assertThat(res).isNull()
    }

    @Test
    fun `refresh return fails`() {
        fakeRepositoryImplTest.setError(true)
        vm.setRepository(fakeRepositoryImplTest)
        vm.event(FriendsEvents.Refresh)
        val res = vm.state.error
        assertThat(res).isNotNull()
    }
}