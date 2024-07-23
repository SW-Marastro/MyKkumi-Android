package com.swmarastro.mykkumi.feature.auth.onBoarding

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.swmarastro.mykkumi.domain.entity.ErrorResponse
import com.swmarastro.mykkumi.domain.entity.UpdateUserInfoRequestVO
import com.swmarastro.mykkumi.domain.usecase.auth.UpdateUserInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class LoginInputUserViewModel @Inject constructor(
    private val updateUserInfoUseCase: UpdateUserInfoUseCase
): ViewModel() {
    private val INVALID_TOKEN = "INVALID_TOKEN"
    private val DUPLICATE_VALUE = "DUPLICATE_VALUE"

    private val _finishLoginUiState = MutableLiveData<Unit>()
    val finishLoginUiState: LiveData<Unit> get() = _finishLoginUiState

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
    fun updateUserInfo(showToast : (message: String) -> Unit) {
        val userInfo = UpdateUserInfoRequestVO(
            nickname = nickname.value,
            profileImage = profileImage.value,
            introduction = null,
            categoryIds = null
        )

        viewModelScope.launch {
            try {
                val response = updateUserInfoUseCase(userInfo)
                finishLogin()
            } catch (e: HttpException){
                handleApiError(e, showToast)
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
        // 형식 모두 만족 -> 사용자 정보 업데이트
        else if(nickname.value.length <= MAX_NICKNAME_LENGTH
            && nickname.value.matches(NICKNAME_REGEX)) {
            updateUserInfo(showToast)
        }
        // 형식 불만족
        else {
            showToast("닉네임 형식 또는 길이가 올바르지 않습니다.")
        }
    }

    private fun handleApiError(exception: HttpException, showToast : (message: String) -> Unit) {
        try {
            val errorBody = exception.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)

            // 만료된 토큰
            if(errorResponse.errorCode == INVALID_TOKEN) {

            }

            // 중복된 닉네임
            else if(errorResponse.errorCode == DUPLICATE_VALUE) {
                showToast("중복된 닉네임입니다.")
            }
        } catch (e: Exception) {
            // _error.value = "An error occurred while processing the error response."
        }
    }

    // 로그인 종료
    fun finishLogin() {
        _finishLoginUiState.value = Unit
    }
}