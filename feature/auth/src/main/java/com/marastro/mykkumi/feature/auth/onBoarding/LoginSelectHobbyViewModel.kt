package com.marastro.mykkumi.feature.auth.onBoarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.gson.Gson
import com.marastro.mykkumi.domain.entity.HobbyCategoryItemVO
import com.marastro.mykkumi.domain.entity.HobbySubCategoryItemVO
import com.marastro.mykkumi.domain.usecase.post.GetHobbyCategoryListUseCase
import com.marastro.mykkumi.feature.auth.LoginScreens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class LoginSelectHobbyViewModel @Inject constructor(
    private val getHobbyCategoryListUseCase: GetHobbyCategoryListUseCase,
): ViewModel() {
    private val _hobbyCategoryUiState = MutableStateFlow<MutableList<HobbyCategoryItemVO>>(mutableListOf())
    val hobbyCategoryUiState: StateFlow<MutableList<HobbyCategoryItemVO>> get() = _hobbyCategoryUiState

    private val _selectedHobbies = MutableStateFlow<MutableSet<Long>>(mutableSetOf())
    val selectedHobbies: StateFlow<MutableSet<Long>> get() = _selectedHobbies

    // 관심 취미 데이터 세팅
    fun getHobbyCategoryList() {
        viewModelScope.launch {
            try {
                val hobbyCategories = withContext(Dispatchers.IO) {
                    getHobbyCategoryListUseCase()
                }
                _hobbyCategoryUiState.value.clear()
                _hobbyCategoryUiState.value.addAll(hobbyCategories.categories)
            } catch (e: Exception) {
                _hobbyCategoryUiState.value.clear()
            }
        }
    }

    // 관심 취미 선택
    fun setHobbySelected(selectHobby: HobbySubCategoryItemVO) {
        val updatedHobbies = _selectedHobbies.value

        if (updatedHobbies.contains(selectHobby.subCategoryId)) { // 선택 취소
            updatedHobbies.apply {
                remove(selectHobby.subCategoryId)
            }
        } else { // 선택
            updatedHobbies.apply {
                add(selectHobby.subCategoryId)
            }
        }
        _selectedHobbies.value = updatedHobbies
    }

    // 다음 화면으로 네비게이션 처리
    fun navigateToInputUserInfoScreen(navController: NavController) {
        val argument: List<Long> = selectedHobbies.value.toList()
        val argumentJson = Gson().toJson(argument)
        navController.navigate(route = LoginScreens.LoginInputUserScreen.name + "/${argumentJson}")
    }

    // 취미 선택 상태 확인
    fun isHobbySelected(hobby: HobbySubCategoryItemVO): Boolean {
        return selectedHobbies.value.contains(hobby.subCategoryId)
    }
}