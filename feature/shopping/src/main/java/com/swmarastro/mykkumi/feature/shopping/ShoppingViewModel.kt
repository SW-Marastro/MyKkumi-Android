package com.swmarastro.mykkumi.feature.shopping

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ShoppingViewModel @Inject constructor(
) : ViewModel() {
    private val _shoppingUiState = MutableStateFlow<String>("")
    val shoppingUiState: StateFlow<String> get() = _shoppingUiState
}