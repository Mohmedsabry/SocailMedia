package test/.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import test/.usecase.testUseCase
import javax.inject.Inject

class testViewModel @Inject constructor(
    private val testUseCase: testUseCase
) : ViewModel() {
    val tests = liveData {
        val data = testUseCase()
        emit(data)
    }
}