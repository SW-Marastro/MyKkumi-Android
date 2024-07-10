package com.swmarastro.mykkumi.feature.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(

): ViewModel() {

    private val _loginUiState = MutableStateFlow<LoginUiState>(LoginUiState.IDle)
    val loginUiState: StateFlow<LoginUiState> get() = _loginUiState

    fun kakaoLogin() {
        _loginUiState.value = LoginUiState.KakaoLogin
        Log.d("---", "카카오 로그인 시도 1")


    }

    fun kakaoLoginSuccess() {
        _loginUiState.tryEmit(LoginUiState.LoginSuccess)
    }

    fun kakaoLoginFail() {
        _loginUiState.tryEmit(LoginUiState.LoginFail)
    }

    fun setUiStateIdle() {
        _loginUiState.tryEmit(LoginUiState.IDle)
    }
}