package com.swmarastro.mykkumi.feature.auth.onBoarding

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swmarastro.mykkumi.domain.exception.ApiException
import com.swmarastro.mykkumi.domain.entity.UpdateUserInfoRequestVO
import com.swmarastro.mykkumi.domain.repository.PreSignedUrlRepository
import com.swmarastro.mykkumi.domain.usecase.auth.UpdateUserInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginInputUserViewModel @Inject constructor(
    private val updateUserInfoUseCase: UpdateUserInfoUseCase,
    private val preSignedUrlRepository: PreSignedUrlRepository,
): ViewModel() {
    private val _finishLoginUiState = MutableLiveData<Unit>()
    val finishLoginUiState: LiveData<Unit> get() = _finishLoginUiState

    private val MAX_NICKNAME_LENGTH = 16
    private val MIN_NICKNAME_LENGTH = 3
    private val NICKNAME_REGEX = Regex("^[a-zA-Z0-9._\\-ㄱ-ㅎ가-힣ㅏ-ㅣ]*$")

    private val MAX_RETRIES = 1

    private val _nickname = MutableStateFlow("")
    val nickname: StateFlow<String> get() = _nickname

    private val _profileImage = MutableStateFlow<Any?>(null)
    val profileImage : StateFlow<Any?> get() = _profileImage

    // 카메라로 촬영할 이미지를 저장할 path
    private val _cameraImagePath = MutableStateFlow<Uri?>(null)
    val cameraImagePath : StateFlow<Uri?> get() = _cameraImagePath

    fun onNicknameChange(
        newNickname: String,
        showToast : (message: String) -> Unit
    ) {
        viewModelScope.launch {
            // 입력 문자 제한 - 한글, 영문자, 숫자, _, -, .
            if (newNickname.matches(NICKNAME_REGEX)) {
                // 닉네임 최대 길이 제한
                if (newNickname.length <= MAX_NICKNAME_LENGTH)
                    _nickname.emit(newNickname)
                else {
                    _nickname.emit(newNickname.substring(0, MAX_NICKNAME_LENGTH))
                    showToast("닉네임은 최대 ${MAX_NICKNAME_LENGTH}자까지 입력 가능합니다.")
                }
            }
        }
    }

    fun deleteNickname() {
        viewModelScope.launch {
            _nickname.emit("")
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
        viewModelScope.launch {
            try {
                var imageUrl: String? = null
                if(profileImage.value != null)
                    imageUrl = preSignedUrlRepository.getPreSignedUrl(profileImage.value as Uri)

                val userInfo = UpdateUserInfoRequestVO(
                    nickname = nickname.value,
                    profileImage = imageUrl,
                    introduction = null,
                    categoryIds = null
                )

                viewModelScope.launch {
                    try { // 회원가입 완료
                        val response = updateUserInfoUseCase(userInfo)
                        finishLogin()
                    } catch (e: ApiException.DuplicateValueException) { // 중복된 닉네임
                        e.message?.let { showToast(it) }
                    } catch (e: ApiException.InvalidNickNameValue) { // 형식에 맞지 않는 닉네임
                        e.message?.let { showToast(it) }
                    } catch (e: ApiException.InvalidRefreshTokenException) { // RefreshToken 만료
                        e.message?.let { showToast(it) }
                    } catch (e: ApiException.UnknownApiException) {
                        showToast("서비스 오류가 발생했습니다.")
                    }  catch (e: Exception) {
                        showToast("서비스 오류가 발생했습니다.")
                    }
                }
            } catch (e: Exception) {
                showToast("서비스 오류가 발생했습니다.")
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

    // 로그인 종료
    fun finishLogin() {
        _finishLoginUiState.value = Unit
    }
}