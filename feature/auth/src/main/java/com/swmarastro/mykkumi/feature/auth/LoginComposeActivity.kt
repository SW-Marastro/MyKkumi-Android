package com.swmarastro.mykkumi.feature.auth

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.swmarastro.mykkumi.feature.auth.ui.theme.MyKkumi_AOSTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginComposeActivity : ComponentActivity() {

    private val viewModel by viewModels<LoginViewModel>()
    private lateinit var kakaoCallback: (OAuthToken?, Throwable?) -> Unit
    private var kakaoScopes = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setKakaoCallback()

        enableEdgeToEdge()
        setContent {
            MyKkumi_AOSTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    ) {

                        KakaoLogin(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(30.dp)
                                .align(Alignment.Center)
                        )

                    }
                }
            }
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun KakaoLogin(modifier: Modifier = Modifier) {
        Image(
            painter = painterResource(id = R.drawable.kakao_login_large_wide_kor),
            contentDescription = "kakao login btn",
            modifier = modifier
                .fillMaxSize()
                .pointerInteropFilter {
                    when (it.action) {
                        MotionEvent.ACTION_DOWN -> {
                            handleKakaoLogin()
//                            viewModel.kakaoLogin()
//                            lifecycleScope.launch {
//                                repeatOnLifecycle(Lifecycle.State.STARTED) {
//                                    viewModel.loginUiState.collect { uiState ->
//                                        Log.d("---", "카카오 로그인 시도 1-2")
//                                        when (uiState) {
//                                            LoginUiState.KakaoLogin -> {
//                                                handleKakaoLogin()
//                                                Log.d("---", "카카오 로그인 시도 2")
//                                            }
//                                            LoginUiState.LoginSuccess -> {
//                                                showToast("Login Success")
//                                                Log.d("---", "카카오 로그인 시도 2-1")
//                                            }
//                                            LoginUiState.LoginFail -> {
//                                                showToast("Login Fail")
//                                                Log.d("---", "카카오 로그인 시도 2-2")
//                                            }
//                                            else -> {}
//                                        }
//                                    }
//                                }
//                            }
                        }

                        else -> false
                    }
                    true
                }
        )
    }

    // 카카오 로그인 callback 세팅
    private fun setKakaoCallback() {
        kakaoCallback = { token, error ->
            if (error != null) { //에러가 있는 경우
                when {
                    error.toString() == AuthErrorCause.AccessDenied.toString() -> {
                        Toast.makeText(this, "접근이 거부 됨(동의 취소)", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidClient.toString() -> {
                        Toast.makeText(this, "유효하지 않은 앱", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidGrant.toString() -> {
                        Toast.makeText(this, "인증 수단이 유효하지 않아 인증할 수 없는 상태", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidRequest.toString() -> {
                        Toast.makeText(this, "요청 파라미터 오류", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidScope.toString() -> {
                        Toast.makeText(this, "유효하지 않은 scope ID", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.Misconfigured.toString() -> {
                        Toast.makeText(this, "설정이 올바르지 않음(android key hash)", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.ServerError.toString() -> {
                        Toast.makeText(this, "서버 내부 에러", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.Unauthorized.toString() -> {
                        Toast.makeText(this, "앱이 요청 권한이 없음", Toast.LENGTH_SHORT).show()
                    }
                    else -> { // Unknown
                        Toast.makeText(this, "기타 에러", Toast.LENGTH_SHORT).show()
                    }
                }
                Log.e(TAG, "카카오계정으로 로그인 실패", error)
                viewModel.kakaoLoginFail()
            }
            else if (token != null) { // 토큰을 받아온 경우
                Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken} / ${token.refreshToken} / ${token.idToken}")
                viewModel.kakaoLoginSuccess()
            }
        }
    }

    // 카카오 로그인 - 추가 정보 수집 동의 scope 세팅
    private fun setKakaoScopes() {


    }

    // 카카오 로그인
    private fun handleKakaoLogin() {
        val content = this
        lifecycleScope.launch {
            try {
                if (UserApiClient.instance.isKakaoTalkLoginAvailable(content)) {
                    // 카카오톡 앱이 설치되어 있고, 연결된 계정이 있는 경우 카카오톡 앱으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoTalk(content, callback = kakaoCallback)
                    Log.d(TAG, "카카오톡으로 로그인")
                } else {
                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(content, callback = kakaoCallback)
                    Log.d(TAG, "카카오 계정으로 로그인")
                }
            } catch (error: Throwable) {
                // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                    Log.d(TAG, "사용자의 의도적인 로그인 취소")
                } else {
                    Log.e(TAG, "인증 에러 발생", error)
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        viewModel.setUiStateIdle()
    }
}