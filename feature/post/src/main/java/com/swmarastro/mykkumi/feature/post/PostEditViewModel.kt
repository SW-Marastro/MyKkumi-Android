package com.swmarastro.mykkumi.feature.post

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.swmarastro.mykkumi.domain.entity.PostEditPinProductVO
import com.swmarastro.mykkumi.domain.entity.PostEditPinVO
import com.swmarastro.mykkumi.domain.entity.PostImageVO
import com.swmarastro.mykkumi.domain.repository.PreSignedUrlRepository
import com.swmarastro.mykkumi.domain.usecase.post.UploadPostUseCase
import com.swmarastro.mykkumi.feature.post.confirm.PostConfirmBottomSheet
import com.swmarastro.mykkumi.feature.post.hobbyCategory.SelectHobbyOfPostBottomSheet
import com.swmarastro.mykkumi.feature.post.imageWithPin.InputProductInfoBottomSheet
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostEditViewModel  @Inject constructor(
    private val preSignedUrlRepository: PreSignedUrlRepository,
    private val uploadPostUseCase: UploadPostUseCase,
) : ViewModel() {
    final val MAX_IMAGE_COUNT = 10
    final val MAX_PIN_COUNT = 10

    private val _postEditUiState = MutableLiveData<MutableList<PostImageVO>>(mutableListOf())
    val postEditUiState: LiveData<MutableList<PostImageVO>> get() = _postEditUiState

    private val _checkCreateView = MutableStateFlow<Boolean>(true)
    val checkCreateView : StateFlow<Boolean> get() = _checkCreateView

    private val _selectImagePosition = MutableLiveData<Int>(-1)
    val selectImagePosition : LiveData<Int> get() = _selectImagePosition

    private val _currentPinList = MutableLiveData<MutableList<PostEditPinVO>>(mutableListOf())
    val currentPinList : LiveData<MutableList<PostEditPinVO>> get() = _currentPinList

    private val _isDeleteImageState = MutableLiveData<Boolean>(false)
    val isDeleteImageState : LiveData<Boolean> get() = _isDeleteImageState

    fun selectPostImage(uris: MutableList<Uri>) {
        viewModelScope.launch {
            val urisCopy = uris.toList()

            for (uri in urisCopy) {
                try {
                    val imageUrl = preSignedUrlRepository.getPreSignedPostUrl(uri)

                    if (imageUrl != null) {
                        // `_postEditUiState` 리스트를 안전하게 업데이트
                        val addPostImages = _postEditUiState.value?.toMutableList() ?: mutableListOf()
                        addPostImages.add(
                            PostImageVO(
                                imageUri = imageUrl,
                                imageLocalUri = uri.toString()
                            )
                        )
                        _postEditUiState.value = addPostImages
                    }
                } catch (e: Exception) {
                    // 예외 처리
                }
            }
        }
    }

    fun openImagePicker(navController: NavController?) {
        val maxImageCount: Int = MAX_IMAGE_COUNT - (_postEditUiState.value?.size ?: 0)

        val imagePickerDeepLink = "mykkumi://image.picker?maxImageCount=${maxImageCount}"

        navController?.navigate(deepLink = imagePickerDeepLink.toUri())
    }

    fun createViewDone() {
        viewModelScope.launch {
            _checkCreateView.emit(false)
        }
    }

    fun changeSelectImagePosition(position: Int) {
        if(position >= 0 && _postEditUiState.value!!.size > position) {
            _postEditUiState.apply {
                if(selectImagePosition.value!! >= 0) value!![selectImagePosition.value!!].isSelect = false // 이전 선택 해제
                value!![position].isSelect = true // 새로운 선택
            }

            if(selectImagePosition.value!! >= 0) {
                _postEditUiState.value?.get(selectImagePosition.value!!)?.apply {
                    pinList = currentPinList.value ?: mutableListOf()
                }
            }
            _selectImagePosition.value = position

            _currentPinList.value = mutableListOf()
            selectImagePosition.value?.let {
                _currentPinList.setValue(
                    _postEditUiState.value?.get(position)?.pinList ?: mutableListOf()
                )
            }
        }
        else {
            _currentPinList.setValue( mutableListOf() )
        }
    }

    // 핀 추가를 위한 제품명 입력 요청
    fun requestProductInfoForPin(fragment: PostEditFragment, position: Int?) {
        val bundle = Bundle()
        if (position != null) { // 핀 수정
            bundle.putInt("position", position)
            bundle.putString("productName", currentPinList.value?.get(position)?.product?.productName)
            bundle.putString("productUrl", currentPinList.value?.get(position)?.product?.productUrl)
        }
        else {
            bundle.putInt("position", -1)
        }
        val bottomSheet = InputProductInfoBottomSheet().apply { setListener(fragment) }
        bottomSheet.arguments = bundle
        bottomSheet.show(fragment.parentFragmentManager, bottomSheet.tag)
    }

    // 핀 추가
    fun addPinOfImage(productName: String, productUrl: String?) {
        selectImagePosition.value?.let {
            val addPinList = _currentPinList.value ?: mutableListOf()
            addPinList.apply {
                add(
                    PostEditPinVO(
                        null,
                        0.5f,
                        0.5f,
                        PostEditPinProductVO(productName, productUrl)
                    )
                )
            }
            _currentPinList.value = mutableListOf()
            _currentPinList.postValue( addPinList )
        }
    }

    // 핀 내용 수정
    fun updateProductInfoForPin(position: Int, productName: String, productUrl: String?) {
        _currentPinList.value?.get(position)?.product!!.productName = productName
        _currentPinList.value?.get(position)?.product!!.productUrl = productUrl
    }

    // 핀 삭제
    fun deletePinOfImage(position: Int) {
        val deletePinList = _currentPinList.value!!
        deletePinList.removeAt(position)
        _currentPinList.postValue(
            deletePinList
        )
    }

    // 이미지 삭제를 위한 확인용 bottomSheet
    fun confirmDeleteImage(fragment: PostEditFragment, message: String, position: Int) {
        _isDeleteImageState.value = true

        val bundle = Bundle()
        bundle.putString("message", message)
        bundle.putInt("position", position)
        val bottomSheet = PostConfirmBottomSheet().apply { setListener(fragment) }
        bottomSheet.arguments = bundle
        bottomSheet.show(fragment.parentFragmentManager, bottomSheet.tag)
    }

    // 이미지 삭제
    fun deleteImage(deleteImagePosition: Int) {
        if(deleteImagePosition == -1) return

        if(selectImagePosition.value!! >= postEditUiState.value!!.size - 1) { // 마지막 이미지가 선택되어 있는 상태
            _selectImagePosition.value = selectImagePosition.value!! - 1
            _postEditUiState.value!!.removeAt(deleteImagePosition)
            changeSelectImagePosition(selectImagePosition.value!!)
        }
        else if(selectImagePosition.value!! == deleteImagePosition) { // 선택한 이미지를 삭제
            _postEditUiState.value!!.removeAt(deleteImagePosition)
            changeSelectImagePosition(deleteImagePosition)
        }
        else if(selectImagePosition.value!! > deleteImagePosition){ // 선택한 이미지가 삭제 이미지보다 뒤에 있을 때
            _postEditUiState.value!!.removeAt(deleteImagePosition)
            changeSelectImagePosition(selectImagePosition.value!! - 1)
        }
        else {
            _postEditUiState.value!!.removeAt(deleteImagePosition)
        }
    }

    // 이미지 삭제 취소
    fun doneDeleteImage() {
        _isDeleteImageState.value = false
    }

    // 포스트 등록 시도
    fun doneEditPost(
        fragment: PostEditFragment,
        showToast: (message: String) -> Unit
    ) {
        // 이미지가 하나 이상인지 확인
        if (!postEditUiState.value.isNullOrEmpty()) {
            selectHobbyCategory(fragment)
        }
        else {
            showToast("포스트를 등록하려면 하나 이상의 이미지가 필요합니다.")
        }
    }

    // 카테고리 선택 BottomSheet
    fun selectHobbyCategory(fragment: PostEditFragment) {
        val bottomSheet = SelectHobbyOfPostBottomSheet().apply { setListener(fragment) }
        bottomSheet.show(fragment.parentFragmentManager, bottomSheet.tag)
    }

    // 포스트 등록
    fun uploadPost(
        subCategoryId: Long,
        content: String,
        showToast: (message: String) -> Unit,
        navController: NavController?
    ) {
        viewModelScope.launch {
            try {
                val uploadPostId = uploadPostUseCase(
                    subCategory = subCategoryId,
                    content = content,
                    postImages = postEditUiState.value!! // 애초에 이미지가 하나 이상이어야 카테고리 선택이 가능
                )
                navController?.popBackStack()
                showToast("포스트가 등록되었습니다!")
            } catch (e: Exception) {
                showToast("포스트 등록에 실패했습니다.")
            }
        }
    }
}