package com.swmarastro.mykkumi.feature.post

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PostEditViewModel  @Inject constructor(
) : ViewModel() {
    private val _postEditUiState = MutableLiveData<MutableList<Uri>>(mutableListOf())
    val postEditUiState: LiveData<MutableList<Uri>> get() = _postEditUiState

    // 카메라로 촬영할 이미지를 저장할 path
    private val _cameraImagePath = MutableStateFlow<Uri?>(null)
    val cameraImagePath : StateFlow<Uri?> get() = _cameraImagePath

    fun selectPostImage(uri: Uri) {
        val addPostImages = _postEditUiState.value
        addPostImages?.add(uri)
        _postEditUiState.value = addPostImages
        postEditUiState.value?.let { Log.d("trst", it.joinToString()) }
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

    fun openImagePicker(navController: NavController?) {
        val imagePickerDeepLink = "mykkumi://image.picker"
        navController?.navigate(deepLink = imagePickerDeepLink.toUri())
    }
}