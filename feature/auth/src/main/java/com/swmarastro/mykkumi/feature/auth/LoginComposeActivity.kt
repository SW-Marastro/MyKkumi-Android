package com.swmarastro.mykkumi.feature.auth

import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.gson.Gson
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.swmarastro.mykkumi.feature.auth.onBoarding.LoginInputUserScreen
import com.swmarastro.mykkumi.feature.auth.onBoarding.LoginSelectHobbyScreen
import com.swmarastro.mykkumi.feature.auth.ui.theme.MyKkumi_AOSTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginComposeActivity : ComponentActivity() {

    private val viewModel: LoginViewModel by viewModels()
    private val activityContext = this

    @ExperimentalPermissionsApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 로그인 종료 상태 체크
        viewModel.finishLoginUiState.observe(this, Observer {
            finish()
        })

        viewModel.setKakaoCallback(
            showToast = {
                showToast(it)
            }
        )

        // 카카오 계정으로 로그인 handling 필요
        viewModel.needKakaoAccount.observe(this, Observer {
            if(it) {
                handleKakaoAccount()
            }
        })

        enableEdgeToEdge()
        setContent {
            MyApp {
                LoginNavigation(this)
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
    private fun LoginNavigation(
        activity: ComponentActivity
    ) {
        val navController = rememberNavController()
        NavHost(navController = navController,
            startDestination = LoginScreens.KakaoLoginScreen.name) {
            composable(LoginScreens.KakaoLoginScreen.name) {
                KakaoLoginScreen(navController = navController)
            }
            composable(LoginScreens.LoginSelectHobbyScreen.name) {
                LoginSelectHobbyScreen(
                    navController = navController,
                )
            }
            composable(
                route = LoginScreens.LoginInputUserScreen.name + "/{selectedHobbies}",
                arguments = listOf(
                    navArgument("selectedHobbies") {
                        type = NavType.StringType
                    }
                )
            ) { backstackEntry ->
                val selectedHobbiesJson = backstackEntry.arguments?.getString("selectedHobbies")
                val selectedHobbies: List<Long>? = Gson().fromJson(selectedHobbiesJson, List::class.java) as? List<Long>

                LoginInputUserScreen(
                    activity = activity,
                    selectedHobbies = selectedHobbies,
                )
            }
        }
    }

    @SuppressLint("CoroutineCreationDuringComposition")
    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    private fun KakaoLoginScreen(navController: NavController) {
        // 로그인 완료되면 화면 이동
        viewModel.loginUiState
            .filter { it == LoginUiState.MykkumiLoginSuccess } // 로그인 성공으로 바뀌었을 때
            .onEach {
                viewModel.navigateToNextScreen(
                    navController = navController,
                    showToast = {
                        showToast(it)
                    }
                )
            }
            .launchIn(lifecycleScope)

        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = com.swmarastro.mykkumi.common_ui.R.color.white))
        ) {
            Text(
                text = getString(R.string.notice_login_page),
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(com.swmarastro.mykkumi.common_ui.R.font.gmarket_sans_bold)),
                color = colorResource(id = com.swmarastro.mykkumi.common_ui.R.color.primary_color),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(
                modifier = Modifier.height(12.dp)
            )
            Image(
                painter = painterResource(id = com.swmarastro.mykkumi.common_ui.R.drawable.ic_mykkumi_character_notice),
                contentDescription = "mykkumi logo",
                modifier = Modifier
                    .width(160.dp)
                    .align(Alignment.CenterHorizontally),
                contentScale = ContentScale.Fit
            )

            Image(
                painter = painterResource(id = com.swmarastro.mykkumi.common_ui.R.drawable.img_mykkumi_typo),
                contentDescription = "mykkumi logo typo",
                modifier = Modifier
                    .width(128.dp)
                    .align(Alignment.CenterHorizontally),
                contentScale = ContentScale.Fit
            )

            Spacer(
                modifier = Modifier.height(80.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .align(Alignment.CenterHorizontally)
                    .clip(RoundedCornerShape(10.dp))
                    .background(colorResource(id = com.swmarastro.mykkumi.common_ui.R.color.kakao_background))
                    .pointerInteropFilter {
                        when (it.action) {
                            MotionEvent.ACTION_DOWN -> handleKakaoLogin()
                            else -> false
                        }
                        true
                    }
                    .padding(vertical = 17.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_kakao_logo),
                    contentDescription = "kakao login btn",
                    modifier = Modifier
                        .size(20.dp),
                    contentScale = ContentScale.Fit
                )
                
                Text(
                    text = getString(R.string.btn_kakao_login),
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily(Font(com.swmarastro.mykkumi.common_ui.R.font.pretendard_semibold)),
                    color = colorResource(id = com.swmarastro.mykkumi.common_ui.R.color.neutral_900),
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(start = 8.dp)
                )
            }

            Spacer(
                modifier = Modifier.height(52.dp)
            )
        }
    }

    // 카카오 로그인
    private fun handleKakaoLogin() {
        viewModel.kakaoLogin()

        lifecycleScope.launch {
            try {
                if (UserApiClient.instance.isKakaoTalkLoginAvailable(activityContext)) {
                    // 카카오톡 앱이 설치되어 있고, 연결된 계정이 있는 경우 카카오톡 앱으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoTalk(activityContext, callback = viewModel.kakaoCallback)
                    Log.d(TAG, "카카오톡으로 로그인")
                } else {
                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(activityContext, callback = viewModel.kakaoCallback)
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

    // 카카오 계정으로 로그인
    private fun handleKakaoAccount() {
        viewModel.kakaoLogin()

        lifecycleScope.launch {
            try {
                viewModel.doneKakaoAccount()

                // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                UserApiClient.instance.loginWithKakaoAccount(activityContext, callback = viewModel.kakaoCallback)
                Log.d(TAG, "카카오 계정으로 로그인")
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
    }
}