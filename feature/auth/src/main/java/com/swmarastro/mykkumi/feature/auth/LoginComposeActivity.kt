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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.swmarastro.mykkumi.feature.auth.onBoarding.LoginInputUserScreen
import com.swmarastro.mykkumi.feature.auth.onBoarding.LoginSelectHobbyScreen
import com.swmarastro.mykkumi.feature.auth.ui.theme.MyKkumi_AOSTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginComposeActivity : ComponentActivity() {

    private val viewModel by viewModels<LoginViewModel>()
    private lateinit var kakaoCallback: (OAuthToken?, Throwable?) -> Unit

    @ExperimentalPermissionsApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setKakaoCallback()

        enableEdgeToEdge()
        setContent {
            MyApp {
                LoginNavigation()
            }
        }
    }

    @Composable
    private fun MyApp(content: @Composable (modifier: Modifier) -> Unit) {
        MyKkumi_AOSTheme {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
            ) { innerPadding ->
                content(
                    Modifier.padding(innerPadding)
                )
            }
        }
    }

    @ExperimentalPermissionsApi
    @Composable
    private fun LoginNavigation() {
        val navController = rememberNavController()
        NavHost(navController = navController,
            startDestination = LoginScreens.KakaoLoginScreen.name) {
            composable(LoginScreens.KakaoLoginScreen.name) {
                KakaoLoginScreen(navController = navController)
            }
            composable(LoginScreens.LoginSelectHobbyScreen.name) {
                LoginSelectHobbyScreen(navController = navController)
            }
            composable(LoginScreens.LoginInputUserScreen.name) {
                LoginInputUserScreen(navController = navController)
            }
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    private fun KakaoLoginScreen(navController: NavController) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {

            Image(
                painter = painterResource(id = R.drawable.kakao_login_large_wide_kor),
                contentDescription = "kakao login btn",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(30.dp)
                    .align(Alignment.Center)
                    .pointerInteropFilter {
                        when (it.action) {
                            MotionEvent.ACTION_DOWN -> handleKakaoLogin()
                            else -> false
                        }
                        true
                    }
            )

            // 추가 정보 입력 테스트용 버튼
            Button(
                onClick = {
                    navController.navigate(route = LoginScreens.LoginSelectHobbyScreen.name)
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
            ) {
                Text(text = "테스트용 버튼")
            }
        }

    }

    // 카카오 로그인 callback 세팅
    private fun setKakaoCallback() {
        kakaoCallback = { token, error ->
            if (error != null) { // 에러가 있는 경우
                when {
                    error.toString() == AuthErrorCause.AccessDenied.toString() -> {
                        showToast("접근이 거부 됨(동의 취소)")
                    }
                    error.toString() == AuthErrorCause.InvalidClient.toString() -> {
                        showToast("유효하지 않은 앱")
                    }
                    error.toString() == AuthErrorCause.InvalidGrant.toString() -> {
                        showToast("인증 수단이 유효하지 않아 인증할 수 없는 상태")
                    }
                    error.toString() == AuthErrorCause.InvalidRequest.toString() -> {
                        showToast("요청 파라미터 오류")
                    }
                    error.toString() == AuthErrorCause.InvalidScope.toString() -> {
                        showToast("유효하지 않은 scope ID")
                    }
                    error.toString() == AuthErrorCause.Misconfigured.toString() -> {
                        showToast("설정이 올바르지 않음(android key hash)")
                    }
                    error.toString() == AuthErrorCause.ServerError.toString() -> {
                        showToast("서버 내부 에러")
                    }
                    error.toString() == AuthErrorCause.Unauthorized.toString() -> {
                        showToast("앱이 요청 권한이 없음")
                    }
                    else -> { // Unknown
                        showToast("기타 에러")
                    }
                }
                Log.e(TAG, "카카오계정으로 로그인 실패", error)
                viewModel.kakaoLoginFail()
            }
            else if (token != null) { // 토큰을 받아온 경우
                Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken} / ${token.refreshToken} / ${token.idToken}")
                viewModel.kakaoLoginSuccess()
                viewModel.setKakaoToken(accessToken = token.accessToken, refreshToken = token.refreshToken)
            }
        }
    }

    // 카카오 로그인
    private fun handleKakaoLogin() {
        val activityContext = this
        lifecycleScope.launch {
            try {
                if (UserApiClient.instance.isKakaoTalkLoginAvailable(activityContext)) {
                    // 카카오톡 앱이 설치되어 있고, 연결된 계정이 있는 경우 카카오톡 앱으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoTalk(activityContext, callback = kakaoCallback)
                    Log.d(TAG, "카카오톡으로 로그인")
                } else {
                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(activityContext, callback = kakaoCallback)
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