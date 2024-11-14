package com.marastro.mykkumi.feature.mypage.setting

import androidx.lifecycle.ViewModel
import com.marastro.mykkumi.domain.datastore.AuthTokenDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val authTokenDataStore: AuthTokenDataStore,
) : ViewModel() {

    fun logout() {
        authTokenDataStore.deleteToken()
    }
}