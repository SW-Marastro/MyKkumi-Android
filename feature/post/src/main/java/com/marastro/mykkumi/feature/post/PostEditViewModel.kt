package com.marastro.mykkumi.feature.post

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.marastro.mykkumi.domain.entity.HobbySubCategoryItemVO
import com.marastro.mykkumi.domain.entity.PostEditPinProductVO
import com.marastro.mykkumi.domain.entity.PostEditPinVO
import com.marastro.mykkumi.domain.entity.PostImageVO
import com.marastro.mykkumi.domain.repository.PreSignedUrlRepository
import com.marastro.mykkumi.domain.usecase.post.GetHobbyCategoryListUseCase
import com.marastro.mykkumi.domain.usecase.post.UploadPostUseCase
import com.marastro.mykkumi.feature.post.hobbyCategory.AccessAbleToHobbyCategory
import com.marastro.mykkumi.feature.post.imageWithPin.InputProductInfoBottomSheet
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PostEditViewModel  @Inject constructor(
    private val preSignedUrlRepository: PreSignedUrlRepository,
    private val uploadPostUseCase: UploadPostUseCase,
    private val getHobbyCategoryListUseCase: GetHobbyCategoryListUseCase,
) : ViewModel(), AccessAbleToHobbyCategory { // , AccessableToCurrentPinList , PostEditItemClickListener
    final val MAX_IMAGE_COUNT = 10
    final val MAX_PIN_COUNT = 10

    private val _postEditUiState = MutableLiveData<MutableList<PostImageVO>>(mutableListOf())
    val postEditUiState: LiveData<MutableList<PostImageVO>> get() = _postEditUiState

    private val _hobbyCategoryUiState = MutableLiveData<MutableList<HobbySubCategoryItemVO>>(mutableListOf())
    val hobbyCategoryUiState: LiveData<MutableList<HobbySubCategoryItemVO>> get() = _hobbyCategoryUiState

    private val _checkCreateView = MutableStateFlow<Boolean>(true)
    val checkCreateView : StateFlow<Boolean> get() = _checkCreateView

    private val _selectImagePosition = MutableLiveData<Int>(-1)
    val selectImagePosition : LiveData<Int> get() = _selectImagePosition

    private val _currentPinList = MutableLiveData<MutableList<PostEditPinVO>>(mutableListOf())
    val currentPinList : LiveData<MutableList<PostEditPinVO>> get() = _currentPinList

    // 새롭게 추가되거나 수정된 핀
    private val _newEditPin =  MutableLiveData<Int>(-1)
    val newEditPin : LiveData<Int> get() = _newEditPin

    private val _isDeleteImageState = MutableLiveData<Boolean>(false)
    val isDeleteImageState : LiveData<Boolean> get() = _isDeleteImageState

    private val _selectHobbyCategory = MutableLiveData<Long>(-1L)
    override val selectHobbyCategory : LiveData<Long> get() = _selectHobbyCategory

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
//                                imageLocalUri = uri.toString()
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

        _newEditPin.value = -1
    }

    // 핀 추가를 위한 제품명 입력 요청
    fun requestProductInfoForPin(position: Int?): Bundle {
        val bundle = Bundle()
        if (position != null) { // 핀 수정
            bundle.putInt("position", position)
            bundle.putString("productName", currentPinList.value?.get(position)?.product?.productName)
            bundle.putString("productUrl", currentPinList.value?.get(position)?.product?.productUrl)
        }
        else {
            bundle.putInt("position", -1)
        }

        return bundle
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
            _currentPinList.value = addPinList

            // 추가된 핀 표시를 위한
            _newEditPin.value = currentPinList.value?.size!! - 1
        }
    }

    // 핀 내용 수정
    fun updateProductInfoForPin(position: Int, productName: String, productUrl: String?) {
        _currentPinList.value?.get(position)?.product!!.productName = productName
        _currentPinList.value?.get(position)?.product!!.productUrl = productUrl

        // 수정된 핀 표시를 위한
        _newEditPin.value = position
    }

    // 새로운 핀 강조 끝남
    fun doneHighlightNewPin() {
        _newEditPin.value = -1
    }

    // 핀 삭제
    fun deletePinOfImage(
        position: Int,
        message: String,
        showToast: (message: String) -> Unit,
    ) {
        val deletePinList = _currentPinList.value!!
        deletePinList.removeAt(position)
        _currentPinList.postValue(
            deletePinList
        )

        showToast(message)
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

    // 관심 취미 데이터 세팅
    fun getHobbyCategoryList() {
        viewModelScope.launch {
            try {
                val hobbyCategories = withContext(Dispatchers.IO) {
                    getHobbyCategoryListUseCase()
                }
                val currentHobbyList = mutableListOf<HobbySubCategoryItemVO>()
                for(category in hobbyCategories.categories) {
                    currentHobbyList.addAll(category.subCategories)
                }
                _hobbyCategoryUiState.setValue(currentHobbyList)
            } catch (e: java.lang.Exception) {
                _hobbyCategoryUiState.value?.clear()
            }
        }
    }

    // 관심 취미 선택
    override fun setHobbySelected(selectHobbyId: Long) {
        _selectHobbyCategory.value = selectHobbyId
    }

    // 포스트 등록 시도
    fun doneEditPost(
        content: String,
        noticeEmptyImage: String,
        noticeEmptyCategory: String,
        showToast: (message: String) -> Unit,
        navController: NavController?
    ) {
        // 이미지가 하나 이상인지 확인
        if (postEditUiState.value.isNullOrEmpty()) {
            showToast(noticeEmptyImage)
        }
        // 카테고리가 선택된 상태인지 확인
        else if(selectHobbyCategory.value == -1L) {
            showToast(noticeEmptyCategory)
        }
        else {
            if(selectImagePosition.value!! >= 0) {
                _postEditUiState.value?.get(selectImagePosition.value!!)?.apply {
                    pinList = currentPinList.value ?: mutableListOf()
                }
            }

            uploadPost(
                content,
                showToast = {
                    showToast(it)
                },
                navController
            )
        }
    }

    // 포스트 등록 가능한지 확인
    fun isPossibleUploadPost(): Boolean {
        // 이미지가 하나 이상인지 확인
        if (postEditUiState.value.isNullOrEmpty()) {
            return false
        }
        // 카테고리가 선택된 상태인지 확인
        else if(selectHobbyCategory.value == -1L) {
            return false
        }
        else return true
    }

    // 포스트 등록
    fun uploadPost(
        content: String,
        showToast: (message: String) -> Unit,
        navController: NavController?
    ) {
        viewModelScope.launch {
            try {
                // TODO: 포스트 조회 API 완성되면, 아래 id로 조회해서 상세 페이지로 이동
                val uploadPostId = uploadPostUseCase(
                    subCategory = selectHobbyCategory.value!!, // 카테고리 선택됐는지 확인하고 넘어옴
                    content = content,
                    postImages = postEditUiState.value!! // 이미지 존재하는지 확인하고 넘어옴
                )
                navController?.popBackStack()
                showToast("포스트가 등록되었습니다!")
            } catch (e: Exception) {
                showToast("포스트 등록에 실패했습니다.")
            }
        }
    }
}