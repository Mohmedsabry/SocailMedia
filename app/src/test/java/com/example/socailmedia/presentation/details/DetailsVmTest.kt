@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.socailmedia.presentation.details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.example.socailmedia.MainCoroutineRule
import com.example.socailmedia.data.repositories.FakeAuthRepoImplTest
import com.example.socailmedia.data.repositories.FakeRepositoryImplTest
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class DetailsVmTest {
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var detailsVm: DetailsVm
    private lateinit var fakeRepositoryImplTest: FakeRepositoryImplTest
    private lateinit var fakeAuthRepoImplTest: FakeAuthRepoImplTest

    @Before
    fun setup() {
        fakeRepositoryImplTest = FakeRepositoryImplTest()
        fakeAuthRepoImplTest = FakeAuthRepoImplTest()
        detailsVm = DetailsVm(
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "user" to "mohammed",
                    "postId" to -1,
                    "isShared" to false
                )
            ),
            repository = fakeRepositoryImplTest,
            authRepository = fakeAuthRepoImplTest
        )
    }

    @Test
    fun `should pass and get post,baseUser,comments and active user`() {
        assertThat(detailsVm.state.error).isNull()
        assertThat(detailsVm.state.post.postId).isEqualTo(-1)
        assertThat(detailsVm.state.baseUser.name).isEqualTo("Active")
        assertThat(detailsVm.state.comments[0].user.age).isEqualTo(0f)
    }

    @Test
    fun `test like post fails`() {
        fakeRepositoryImplTest.setError(true)
        detailsVm.setRepository(fakeRepositoryImplTest)
        detailsVm.event(DetailsEvents.OnLiked(true))
        assertThat(detailsVm.state.error).isNotNull()
    }

    @Test
    fun `test like post pass`() {
        detailsVm.event(DetailsEvents.OnLiked(true))
        assertThat(detailsVm.state.error).isNull()
    }
    @Test
    fun `test comment fails`(){
        fakeRepositoryImplTest.setError(true)
        detailsVm.setRepository(fakeRepositoryImplTest)
        detailsVm.event(DetailsEvents.OnSendComment)
        assertThat(detailsVm.state.error).isNotNull()
    }
    @Test
    fun `test comment pass`(){
        fakeRepositoryImplTest.setError(true)
        detailsVm.setRepository(fakeRepositoryImplTest)
        detailsVm.event(DetailsEvents.OnSendComment)
        assertThat(detailsVm.state.error).isNotNull()
    }
    @Test
    fun `test share post fails`(){
        fakeRepositoryImplTest.setError(true)
        detailsVm.setRepository(fakeRepositoryImplTest)
        detailsVm.event(DetailsEvents.OnSharePost)
        assertThat(detailsVm.state.error).isNotNull()
    }
    @Test
    fun `test share post pass`(){
        detailsVm.event(DetailsEvents.OnSharePost)
        assertThat(detailsVm.state.error).isNull()
    }
}