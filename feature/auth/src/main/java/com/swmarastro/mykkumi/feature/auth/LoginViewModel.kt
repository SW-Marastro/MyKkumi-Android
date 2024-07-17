package com.swmarastro.mykkumi.feature.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swmarastro.mykkumi.domain.entity.KakaoToken
import com.swmarastro.mykkumi.domain.usecase.KakaoLoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val kakaoLoginUseCase: KakaoLoginUseCase
): ViewModel() {

    private val _loginUiState = MutableStateFlow<LoginUiState>(LoginUiState.IDle)
    val loginUiState: StateFlow<LoginUiState> get() = _loginUiState

//    private val _kakaoLoginToken = MutableLiveData<KakaoToken>()
//    val kakaoLoginToken: LiveData<KakaoToken> get() = _kakaoLoginToken

    fun kakaoLogin() {
        _loginUiState.value = LoginUiState.KakaoLogin
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

    // 카카오 로그인 token 값
    fun setKakaoToken(accessToken: String, refreshToken: String) {
        viewModelScope.launch {
            try {
                kakaoLoginUseCase(
                    KakaoToken(
                        accessToken = accessToken,
                        refreshToken = refreshToken
                    )
                )
            }
            catch (e: Exception) {

            }
        }
    }
}