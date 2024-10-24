package com.marastro.mykkumi.feature.auth

sealed interface LoginUiState {
    object KakaoLoginSuccess : LoginUiState
    object KakaoLoginFail : LoginUiState
    object IDle : LoginUiState
    object KakaoLogin : LoginUiState
    object MykkumiLoginSuccess : LoginUiState
    object MykkumiLoginFail : LoginUiState
    object DoneProcess : LoginUiState
}