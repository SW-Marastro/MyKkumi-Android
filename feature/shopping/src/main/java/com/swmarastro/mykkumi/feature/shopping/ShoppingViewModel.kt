package com.swmarastro.mykkumi.feature.shopping

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swmarastro.mykkumi.domain.usecase.GetHelloWorldUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ShoppingViewModel @Inject constructor(
    private val getHelloWorldUseCase: GetHelloWorldUseCase
) : ViewModel() {
    private val _shoppingUiState = MutableStateFlow<String>("")
    val shoppingUiState: StateFlow<String> get() = _shoppingUiState

    fun setHelloWorld() {
        viewModelScope.launch {
            try {
                val helloWorld = withContext(Dispatchers.IO) {
                    getHelloWorldUseCase() // invoke()
                }
                _shoppingUiState.value = helloWorld.title
            } catch (e: Exception) {
                _shoppingUiState.value = "API 통신 실패"
            }
        }
    }
}