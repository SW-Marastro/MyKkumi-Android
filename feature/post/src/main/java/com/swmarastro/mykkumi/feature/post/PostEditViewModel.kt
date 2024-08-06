package com.swmarastro.mykkumi.feature.post

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.swmarastro.mykkumi.feature.post.image.ImagePickerArgument
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PostEditViewModel  @Inject constructor(
) : ViewModel() {
    final val MAX_IMAGE_COUNT = 10

    private val _postEditUiState = MutableLiveData<MutableList<PostImageData>>(mutableListOf())
    val postEditUiState: LiveData<MutableList<PostImageData>> get() = _postEditUiState

    private val _checkCreateView = MutableStateFlow<Boolean>(true)
    val checkCreateView : StateFlow<Boolean> get() = _checkCreateView

    private val _selectImagePosition = MutableLiveData<Int>(0)
    val selectImagePosition : LiveData<Int> get() = _selectImagePosition

    fun selectPostImage(uri: Uri) {
        val addPostImages = _postEditUiState.value
        addPostImages?.add(PostImageData(localUri = uri))
        _postEditUiState.value = addPostImages!!
    }

    fun openImagePicker(navController: NavController?) {
        val maxImageCount: Int = MAX_IMAGE_COUNT - (_postEditUiState.value?.size ?: 0)

        val imagePickerDeepLink = "mykkumi://image.picker?maxImageCount=${maxImageCount}"

        navController?.navigate(deepLink = imagePickerDeepLink.toUri())
    }

    fun createViewDone() {
        _checkCreateView.value = false
    }

    fun changeSelectImagePosition(position: Int) {
        _selectImagePosition.value = position
    }
}