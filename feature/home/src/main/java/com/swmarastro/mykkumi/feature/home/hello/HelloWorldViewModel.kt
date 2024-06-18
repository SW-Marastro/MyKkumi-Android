package com.swmarastro.mykkumi.feature.home.hello

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swmarastro.mykkumi.domain.usecase.GetHelloWorldUseCase
import com.swmarastro.mykkumi.feature.home.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class HelloWorldViewModel @Inject constructor(
    private val getHelloWorldUseCase: GetHelloWorldUseCase
) : ViewModel() {

    private val _helloWorldUiState = MutableStateFlow<String>("")
    val helloWorldUiState: StateFlow<String> get() = _helloWorldUiState

    fun setHelloWorld() {
        viewModelScope.launch {
            try {
                val helloWorld = withContext(Dispatchers.IO) {
                    getHelloWorldUseCase.invoke()
                }
                _helloWorldUiState.value = helloWorld.title
                Log.d("---viewmodel", _helloWorldUiState.value)
            } catch (e: Exception) {
                _helloWorldUiState.value = "API 통신 실패"
            }
        }
    }
}