package com.marastro.mykkumi.feature.mypage.setting

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
) : ViewModel() {
    private val _settingUiState = MutableStateFlow<String>("")
    val settingUiState: StateFlow<String> get() = _settingUiState
}