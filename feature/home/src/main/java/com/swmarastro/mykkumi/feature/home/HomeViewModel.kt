package com.swmarastro.mykkumi.feature.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
) : ViewModel() {
    private val _homeUiState = MutableStateFlow<String>("")
    val homeUiState: StateFlow<String> get() = _homeUiState
}