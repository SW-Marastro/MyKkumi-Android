package com.swmarastro.mykkumi.feature.auth.onBoarding

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class LoginInputUserViewModel @Inject constructor(

): ViewModel() {
    private val MAX_NICKNAME_LENGTH = 16
    private val MIN_NICKNAME_LENGTH = 3
    private val NICKNAME_REGEX = Regex("^[a-zA-Z0-9._\\-ㄱ-ㅎ가-힣ㅏ-ㅣ]*$")

    private val _nickname = MutableStateFlow("")
    val nickname: StateFlow<String> get() = _nickname

    private val _profileImage = MutableStateFlow<Any>(com.swmarastro.mykkumi.common_ui.R.drawable.img_profile_default)
    val profileImage : StateFlow<Any> get() = _profileImage

    fun onNicknameChange(newNickname: String) {
        // 입력 문자 제한 - 한글, 영문자, 숫자, _, -, .
        if(newNickname.matches(NICKNAME_REGEX)) {
            // 닉네임 최대 길이 제한
            if (newNickname.length <= MAX_NICKNAME_LENGTH) _nickname.value = newNickname
            else _nickname.value = newNickname.substring(0, MAX_NICKNAME_LENGTH)
        }
    }

    fun selectProfileImage(uri: Any) {
        _profileImage.value = uri
    }
}