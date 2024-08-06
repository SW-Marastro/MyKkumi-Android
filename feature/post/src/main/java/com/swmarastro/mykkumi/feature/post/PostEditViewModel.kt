package com.swmarastro.mykkumi.feature.post

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.swmarastro.mykkumi.domain.entity.PostEditPinVO
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

    private val _currentPinList = MutableLiveData<MutableList<PostEditPinVO>>(mutableListOf())
    val currentPinList : LiveData<MutableList<PostEditPinVO>> get() = _currentPinList

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
        Log.d("test", "viewmodel 왔다감")
        if(position >= 0) {
            Log.d("test22", "viewmodel 왔다감22")
            selectImagePosition.value?.let {
                _postEditUiState.value?.get(it)?.apply {
                    pinList = currentPinList.value ?: mutableListOf()
                }
            }
            _selectImagePosition.value = position

            selectImagePosition.value?.let {
                _currentPinList.value = _postEditUiState.value?.get(position)?.pinList ?: mutableListOf()
            }
        }
        else {
            _currentPinList.value = mutableListOf()
        }
    }

    fun addPinOfImage() {
        Log.d("Test-----", "pin 추가")
        Log.d("Test-----", selectImagePosition.value.toString())

        selectImagePosition.value?.let {
            val addPinList = _currentPinList.value ?: mutableListOf()
            addPinList.apply {
                add(
                    PostEditPinVO(
                        null,
                        50f,
                        50f
                    )
                )
            }
            _currentPinList.value = addPinList
        }

        Log.d("Test-----", _currentPinList.value.toString())
    }
}