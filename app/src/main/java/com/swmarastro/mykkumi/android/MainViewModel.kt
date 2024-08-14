package com.swmarastro.mykkumi.android

import android.content.Intent
import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.swmarastro.mykkumi.domain.datastore.AuthTokenDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authTokenDataStore: AuthTokenDataStore,
) : ViewModel() {
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