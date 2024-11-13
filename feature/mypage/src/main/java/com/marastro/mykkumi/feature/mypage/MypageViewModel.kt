package com.marastro.mykkumi.feature.mypage

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marastro.mykkumi.domain.datastore.AuthTokenDataStore
import com.marastro.mykkumi.domain.entity.UserInfoVO
import com.marastro.mykkumi.domain.exception.ApiException
import com.marastro.mykkumi.domain.usecase.auth.GetUserInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MypageViewModel @Inject constructor(
    private val authTokenDataStore: AuthTokenDataStore,
    private val getUserInfoUseCase: GetUserInfoUseCase,
) : ViewModel() {
    private val _mypageUiState = MutableStateFlow<String>("")
    val mypageUiState: StateFlow<String> get() = _mypageUiState

    private val _userInfoUiState = MutableLiveData<UserInfoVO>(null)
    val userInfoUiState: LiveData<UserInfoVO> get() = _userInfoUiState


    fun isLogin(): Boolean {
        return authTokenDataStore.isLogin()
    }

    // 로그인
    fun navigateLogin() : Intent? {
        if(authTokenDataStore.isLogin()) return null

        val loginDeepLink = "mykkumi://mykkumi.signin"

        val intent = Intent()
        intent.setAction(Intent.ACTION_VIEW)
        intent.setData(Uri.parse(loginDeepLink))

        return intent
    }

    fun getLoginUser(showToast : (message: String) -> Unit) {
        viewModelScope.launch {
            try {
                val userInfo = getUserInfoUseCase()
                _userInfoUiState.value = userInfo
            }
            catch (e: ApiException.UnknownApiException) {
                showToast("서비스 오류가 발생했습니다.")
            }
            catch (e: Exception) {
                showToast("서비스 오류가 발생했습니다.")
            }
        }
    }

    // 로그아웃 테스트
    fun logout() {
        authTokenDataStore.deleteToken()
    }
}