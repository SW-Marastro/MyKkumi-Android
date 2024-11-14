package com.marastro.mykkumi.feature.mypage.editProfile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marastro.mykkumi.domain.entity.HobbySubCategoryItemVO
import com.marastro.mykkumi.domain.entity.UserInfoVO
import com.marastro.mykkumi.domain.exception.ApiException
import com.marastro.mykkumi.domain.usecase.auth.GetUserInfoUseCase
import com.marastro.mykkumi.domain.usecase.post.GetHobbyCategoryListUseCase
import com.marastro.mykkumi.feature.mypage.editProfile.hobbyCategory.AccessAbleToHobbyCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val getHobbyCategoryListUseCase: GetHobbyCategoryListUseCase,
) : ViewModel(), AccessAbleToHobbyCategory {
    private lateinit var originProfileUiState : UserInfoVO

    private val _editProfileUiState = MutableLiveData<UserInfoVO>(null)
    val editProfileUiState: LiveData<UserInfoVO> get() = _editProfileUiState

    private val _hobbyCategoryUiState = MutableLiveData<MutableList<HobbySubCategoryItemVO>>(mutableListOf())
    val hobbyCategoryUiState: LiveData<MutableList<HobbySubCategoryItemVO>> get() = _hobbyCategoryUiState

    private val _selectHobbyCategories = MutableLiveData<MutableSet<Long>>(mutableSetOf())
    override val selectHobbyCategories : LiveData<MutableSet<Long>> get() = _selectHobbyCategories

    private val _isChangeInfo = MutableLiveData<Boolean>(false)
    val isChangeInfo : LiveData<Boolean> get() = _isChangeInfo

    fun getLoginUser(showToast : (message: String) -> Unit) {
        viewModelScope.launch {
            try {
                val userInfo = getUserInfoUseCase()
                originProfileUiState = userInfo
                _editProfileUiState.setValue(userInfo)
            }
            catch (e: ApiException.UnknownApiException) {
                showToast("서비스 오류가 발생했습니다.")
            }
            catch (e: Exception) {
                showToast("서비스 오류가 발생했습니다.")
            }
        }
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
                _selectHobbyCategories.setValue(mutableSetOf())
            } catch (e: java.lang.Exception) {
                _hobbyCategoryUiState.value?.clear()
            }
        }
    }

    // 관심 취미 선택
    override fun setHobbySelected(selectHobbyId: Long) {
        var changeSelected = _selectHobbyCategories.value
        if(_selectHobbyCategories.value.isNullOrEmpty())
            changeSelected = mutableSetOf()

        if(_selectHobbyCategories.value!!.contains((selectHobbyId)))
            changeSelected!!.remove(selectHobbyId)
        else
            changeSelected!!.add(selectHobbyId)

        _selectHobbyCategories.setValue(changeSelected ?: mutableSetOf())
    }

    fun checkChangeInfo() : Boolean {
        // 변경된 내용이 있는지
        if(originProfileUiState.nickname != editProfileUiState.value?.nickname
            || originProfileUiState.introduction != editProfileUiState.value?.introduction
            || originProfileUiState.profileImage != editProfileUiState.value?.profileImage
            || !selectHobbyCategories.value.isNullOrEmpty()) { // TODO: 이전 카테고리 선택 정보 알게 되면 이전이랑 비교하는 작업으로 변경 필
            _isChangeInfo.setValue(true)
            return true
        }

        else {
            _isChangeInfo.setValue(false)
            return false
        }
    }
}