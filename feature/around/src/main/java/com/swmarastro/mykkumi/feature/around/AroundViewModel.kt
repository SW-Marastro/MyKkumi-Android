package com.swmarastro.mykkumi.feature.around

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AroundViewModel @Inject constructor(
) : ViewModel() {
    private val _aroundUiState = MutableStateFlow<String>("")
    val aroundUiState: StateFlow<String> get() = _aroundUiState
}