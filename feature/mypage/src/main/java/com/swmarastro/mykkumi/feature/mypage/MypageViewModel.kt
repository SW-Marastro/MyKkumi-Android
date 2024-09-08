package com.swmarastro.mykkumi.feature.mypage

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.swmarastro.mykkumi.domain.datastore.AuthTokenDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MypageViewModel @Inject constructor(
    private val authTokenDataStore: AuthTokenDataStore,
) : ViewModel() {
    private val _mypageUiState = MutableStateFlow<String>("")
    val mypageUiState: StateFlow<String> get() = _mypageUiState

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

    // 로그아웃 테스트
    fun logout() {
        authTokenDataStore.deleteToken()
    }
}