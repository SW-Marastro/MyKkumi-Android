package com.swmarastro.mykkumi.feature.mypage

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MypageViewModel @Inject constructor(
) : ViewModel() {
    private val _mypageUiState = MutableStateFlow<String>("")
    val mypageUiState: StateFlow<String> get() = _mypageUiState
}