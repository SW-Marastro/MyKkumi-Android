package com.swmarastro.mykkumi.feature.auth.onBoarding

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swmarastro.mykkumi.domain.entity.UpdateUserInfoRequestVO
import com.swmarastro.mykkumi.domain.usecase.auth.UpdateUserInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginInputUserViewModel @Inject constructor(
    private val updateUserInfoUseCase: UpdateUserInfoUseCase
): ViewModel() {
    private val MAX_NICKNAME_LENGTH = 16
    private val MIN_NICKNAME_LENGTH = 3
    private val NICKNAME_REGEX = Regex("^[a-zA-Z0-9._\\-ㄱ-ㅎ가-힣ㅏ-ㅣ]*$")

    private val _nickname = MutableStateFlow("")
    val nickname: StateFlow<String> get() = _nickname

    private val _profileImage = MutableStateFlow<Any>(com.swmarastro.mykkumi.common_ui.R.drawable.img_profile_default)
    val profileImage : StateFlow<Any> get() = _profileImage

    // 카메라로 촬영할 이미지를 저장할 path
    private val _cameraImagePath = MutableStateFlow<Uri?>(null)
    val cameraImagePath : StateFlow<Uri?> get() = _cameraImagePath

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
        resetCameraImagePath()
    }

    // 카메라로 촬영한 이미지가 저장될 경로
    fun setCameraImagePath(path: Uri) {
        _cameraImagePath.value = path
    }

    // 경로 사용하면 리셋
    fun resetCameraImagePath() {
        _cameraImagePath.value = null
    }

    // 사용자 정보 업데이트 후 가입 완료
    fun updateUserInfo() {
        val userInfo = UpdateUserInfoRequestVO(
            nickname = nickname.value,
            profileImage = profileImage.value,
            introduction = null,
            categoryId = null
        )

        viewModelScope.launch {
            try {
                val response = updateUserInfoUseCase(userInfo)
                Log.d("test update userInfo", response.toString())
            } catch (e: Exception) {

            }
        }
    }

    // 닉네임 형식 검사
    fun confirmNickname(showToast : (message: String) -> Unit) {
        // 최소길이 불만족
        if(nickname.value.length < MIN_NICKNAME_LENGTH) {
            showToast("닉네임은 3자 이상부터 가능합니다.")
        }
        // 형식 모두 만족 -> 정보 수정으로
        else if(nickname.value.length <= MAX_NICKNAME_LENGTH
            && nickname.value.matches(NICKNAME_REGEX)) {
            updateUserInfo()
        }
        // 형식 불만족
        else {
            showToast("닉네임 형식 또는 길이가 올바르지 않습니다.")
        }
    }
}