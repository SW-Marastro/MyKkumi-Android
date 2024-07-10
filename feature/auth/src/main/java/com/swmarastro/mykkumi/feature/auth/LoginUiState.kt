package com.swmarastro.mykkumi.feature.auth

sealed interface LoginUiState {
    object LoginSuccess : LoginUiState
    object LoginFail : LoginUiState
    object IDle : LoginUiState
    object KakaoLogin : LoginUiState
}