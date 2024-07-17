package com.swmarastro.mykkumi.feature.auth

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(

): ViewModel() {

    private val _loginUiState = MutableStateFlow<LoginUiState>(LoginUiState.IDle)
    val loginUiState: StateFlow<LoginUiState> get() = _loginUiState

    lateinit var kakaoCallback: (OAuthToken?, Throwable?) -> Unit

    fun kakaoLoginSuccess() {
        _loginUiState.tryEmit(LoginUiState.LoginSuccess)
    }

    fun kakaoLoginFail() {
        _loginUiState.tryEmit(LoginUiState.LoginFail)
    }

    fun setUiStateIdle() {
        _loginUiState.tryEmit(LoginUiState.IDle)
    }

    // 카카오 로그인 callback 세팅
    fun setKakaoCallback(showToast : (message: String) -> Unit) {
        kakaoCallback = { token, error ->
            if (error != null) { // 에러가 있는 경우
                when {
                    error.toString() == AuthErrorCause.AccessDenied.toString() -> {
                        showToast("카카오 정보 제공 동의가 취소되었습니다.") // 접근이 거부 됨(동의 취소)
                    }
                    error.toString() == AuthErrorCause.InvalidClient.toString() -> {
                        showToast("유효하지 않은 앱입니다.") // 유효하지 않은 앱
                    }
                    error.toString() == AuthErrorCause.InvalidGrant.toString() -> {
                        showToast("인증 수단이 유효하지 않아 인증할 수 없습니다.") // 인증 수단이 유효하지 않아 인증할 수 없는 상태
                    }
                    error.toString() == AuthErrorCause.InvalidRequest.toString() -> {
                        showToast("서비스 에러가 발생했습니다.") // 요청 파라미터 오류
                    }
                    error.toString() == AuthErrorCause.InvalidScope.toString() -> {
                        showToast("서비스 에러가 발생했습니다.") // 유효하지 않은 scope ID
                    }
                    error.toString() == AuthErrorCause.Misconfigured.toString() -> {
                        showToast("서비스 에러가 발생했습니다.") // 설정이 올바르지 않음(android key hash)
                    }
                    error.toString() == AuthErrorCause.ServerError.toString() -> {
                        showToast("카카오 서버 내부 에러가 발생했습니다.") // 서버 내부 에러
                    }
                    error.toString() == AuthErrorCause.Unauthorized.toString() -> {
                        showToast("요청 권한이 없는 앱입니다.") // 앱이 요청 권한이 없음
                    }
                    else -> { // Unknown
                        showToast("서비스 에러가 발생했습니다.") // 기타 에러
                    }
                }
                Log.e(TAG, "카카오계정으로 로그인 실패", error)
                kakaoLoginFail()
            }
            else if (token != null) { // 토큰을 받아온 경우
                Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken} / ${token.refreshToken} / ${token.idToken}")
                kakaoLoginSuccess()
            }
        }
    }
}