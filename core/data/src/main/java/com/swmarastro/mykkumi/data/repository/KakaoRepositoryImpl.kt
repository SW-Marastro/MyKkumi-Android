package com.swmarastro.mykkumi.data.repository

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.user.UserApiClient
import com.swmarastro.mykkumi.domain.entity.KakaoToken
import com.swmarastro.mykkumi.domain.repository.KakaoRepository
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class KakaoRepositoryImpl @Inject constructor(
    private val context: Context,
) : KakaoRepository {

    override suspend fun kakaoLogin(): KakaoToken = suspendCoroutine { continuation ->
        val kakaoCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                handleLoginError(error)
                // Resume with exception if an error occurs
                continuation.resumeWithException(error)
            } else if (token != null) {
                // Resume with the token if successful
                continuation.resume(token.toDomain())
            }
        }

        Log.d("repo test", "kakao")
        Log.d("repo test", context.toString())

        try {
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
                Log.d("repo test", "kakao try 1-1")
                UserApiClient.instance.loginWithKakaoTalk(context, callback = kakaoCallback)
                Log.d("repo test", "kakao try 1-2")
            } else {
                Log.d("repo test", "kakao try 1-3")
                UserApiClient.instance.loginWithKakaoAccount(context, callback = kakaoCallback)
                Log.d("repo test", "kakao try 1-4")
            }
        } catch (e: Exception) {
            Log.e("repo test", "Exception during login attempt", e)
            continuation.resumeWithException(e)
        }
    }

    private fun handleLoginError(error: Throwable) {
        val message = when {
            error.toString() == AuthErrorCause.AccessDenied.toString() -> "접근이 거부 됨(동의 취소)"
            error.toString() == AuthErrorCause.InvalidClient.toString() -> "유효하지 않은 앱"
            error.toString() == AuthErrorCause.InvalidGrant.toString() -> "인증 수단이 유효하지 않아 인증할 수 없는 상태"
            error.toString() == AuthErrorCause.InvalidRequest.toString() -> "요청 파라미터 오류"
            error.toString() == AuthErrorCause.InvalidScope.toString() -> "유효하지 않은 scope ID"
            error.toString() == AuthErrorCause.Misconfigured.toString() -> "설정이 올바르지 않음(android key hash)"
            error.toString() == AuthErrorCause.ServerError.toString() -> "서버 내부 에러"
            error.toString() == AuthErrorCause.Unauthorized.toString() -> "앱이 요청 권한이 없음"
            else -> "기타 에러"
        }
        //Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        Log.e("KakaoRepositoryImpl", "카카오계정으로 로그인 실패 : " + message, error)
    }

    private fun OAuthToken.toDomain() = KakaoToken(
        accessToken = this.accessToken,
        accessTokenExpiresAt = this.accessTokenExpiresAt,
        refreshToken = this.refreshToken,
        refreshTokenExpiresAt = this.refreshTokenExpiresAt,
        idToken = this.idToken,
        scopes = this.scopes
    )
}