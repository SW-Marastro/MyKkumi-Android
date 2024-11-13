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
    private val _editProfileUiState = MutableLiveData<UserInfoVO>(null)
    val editProfileUiState: LiveData<UserInfoVO> get() = _editProfileUiState

    private val _hobbyCategoryUiState = MutableLiveData<MutableList<HobbySubCategoryItemVO>>(mutableListOf())
    val hobbyCategoryUiState: LiveData<MutableList<HobbySubCategoryItemVO>> get() = _hobbyCategoryUiState

    private val _selectHobbyCategories = MutableLiveData<MutableSet<Long>>(mutableSetOf())
    override val selectHobbyCategories : LiveData<MutableSet<Long>> get() = _selectHobbyCategories

    fun getLoginUser(showToast : (message: String) -> Unit) {
        viewModelScope.launch {
            try {
                val userInfo = getUserInfoUseCase()
                _editProfileUiState.value = userInfo
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
        if(_selectHobbyCategories.value.isNullOrEmpty())
            _selectHobbyCategories.setValue(mutableSetOf())

        if(_selectHobbyCategories.value!!.contains((selectHobbyId)))
            _selectHobbyCategories.value!!.remove(selectHobbyId)
        else
            _selectHobbyCategories.value!!.add(selectHobbyId)
    }
}