package com.swmarastro.mykkumi.feature.post.hobbyCategory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swmarastro.mykkumi.domain.entity.HobbyCategoryItemVO
import com.swmarastro.mykkumi.domain.entity.HobbySubCategoryItemVO
import com.swmarastro.mykkumi.domain.usecase.post.GetHobbyCategoryListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class SelectHobbyOfPostViewModel @Inject constructor(
    private val getHobbyCategoryListUseCase: GetHobbyCategoryListUseCase,
) : ViewModel() {
    private val _hobbyCategoryUiState = MutableLiveData<MutableList<HobbyCategoryItemVO>>(mutableListOf())
    val hobbyCategoryUiState: LiveData<MutableList<HobbyCategoryItemVO>> get() = _hobbyCategoryUiState

    private val _selectedHobby = MutableLiveData<Long>(-1L)
    val selectedHobby: LiveData<Long> get() = _selectedHobby

    // 관심 취미 데이터 세팅
    fun getHobbyCategoryList() {
        viewModelScope.launch {
            try {
                val hobbyCategories = withContext(Dispatchers.IO) {
                    getHobbyCategoryListUseCase()
                }
                _hobbyCategoryUiState.value?.clear()
                _hobbyCategoryUiState.value = hobbyCategories.categories.toMutableList()
            } catch (e: Exception) {
                _hobbyCategoryUiState.value?.clear()
            }
        }
    }

    // 관심 취미 선택
    fun setHobbySelected(selectHobbyId: Long) {
        _selectedHobby.value = selectHobbyId
    }
}