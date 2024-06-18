package com.swmarastro.mykkumi.feature.home.hello

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swmarastro.mykkumi.domain.usecase.GetHelloWorldUseCase
import com.swmarastro.mykkumi.feature.home.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HelloWorldViewModel @Inject constructor(
    private val getHelloWorldUseCase: GetHelloWorldUseCase
) : ViewModel() {

    private val _helloWorldDataUiState = MutableStateFlow<String?>(null)
    val helloWorldUiState: StateFlow<String?> get() = _helloWorldDataUiState

    fun setHelloWorld() {
        viewModelScope.launch {
            getHelloWorldUseCase.invoke()
        }
    }
}