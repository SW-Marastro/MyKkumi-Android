package com.swmarastro.mykkumi.feature.post

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PostEditViewModel  @Inject constructor(
) : ViewModel() {
    private val _postEditUiState = MutableStateFlow<String>("")
    val postEditUiState: StateFlow<String> get() = _postEditUiState
}