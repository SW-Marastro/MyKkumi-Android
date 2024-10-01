package com.marastro.mykkumi.android

import android.content.Intent
import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.marastro.mykkumi.domain.datastore.AuthTokenDataStore
import com.marastro.mykkumi.domain.exception.ApiException
import com.marastro.mykkumi.domain.usecase.auth.GetUserInfoUseCase
import com.marastro.mykkumi.feature.auth.LoginScreens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authTokenDataStore: AuthTokenDataStore,
    private val getUserInfoUseCase: GetUserInfoUseCase,
) : ViewModel() {
    private val _currentMenu = MutableStateFlow<Int?>(null)
    val currentMenu: StateFlow<Int?> get() = _currentMenu

    fun selectMenu(selectMenu: Int) {
        viewModelScope.launch {
            _currentMenu.emit(selectMenu)
        }
    }

    // token은 있지만, 회원가입 처리가 제대로 안 된 경우
    fun checkIsSignDone(
        navController: NavController,
        showToast : (message: String) -> Unit
    ) {
        if(authTokenDataStore.isLogin()) {
            viewModelScope.launch {
                try {
                    val userInfo = getUserInfoUseCase()

                    // 최초 가입자 -> 추가 정보 입력 페이지로
                    if (userInfo.nickname == null) {
                        navController.navigate(route = LoginScreens.LoginSelectHobbyScreen.name)
                    }
                } catch (e: ApiException.UnknownApiException) {
                    showToast("서비스 오류가 발생했습니다.")
                } catch (e: Exception) {
                    showToast("서비스 오류가 발생했습니다.")
                }
            }
        }
    }

    // 로그인 페이지 이동
    fun navigateLogin() : Intent? {
        if(authTokenDataStore.isLogin()) return null

        val loginDeepLink = "mykkumi://mykkumi.signin"

        val intent = Intent()
        intent.setAction(Intent.ACTION_VIEW)
        intent.setData(Uri.parse(loginDeepLink))

        return intent
    }

    // 포스트 작성 페이지로 이동
    fun navigatePostEdit(navController: NavController?) {
        val navigateDeepLink = "mykkumi://post.edit"
        navController?.navigate(deepLink = navigateDeepLink.toUri())
    }
}