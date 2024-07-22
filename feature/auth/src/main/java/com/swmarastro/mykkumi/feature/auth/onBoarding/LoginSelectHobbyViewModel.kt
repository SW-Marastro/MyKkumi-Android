package com.swmarastro.mykkumi.feature.auth.onBoarding

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.swmarastro.mykkumi.feature.auth.LoginScreens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class LoginSelectHobbyViewModel @Inject constructor(

): ViewModel() {
    private val _hobbyCategoryUiState = MutableStateFlow<MutableList<TestHobby>>(mutableListOf())
    val hobbyCategoryUiState: StateFlow<MutableList<TestHobby>> get() = _hobbyCategoryUiState

    private val _selectedHobbies = MutableStateFlow<MutableList<String>>(mutableListOf())
    val selectedHobbies: StateFlow<MutableList<String>> get() = _selectedHobbies

    // 관심 취미 데이터 세팅
    fun getHobbyCategoryList() {
        _hobbyCategoryUiState.value.addAll(mutableListOf(
            TestHobby("공예/DIY", mutableListOf("다이어리 꾸미기", "포토카드 꾸미기", "뜨개질")),
            TestHobby("스포츠", mutableListOf("야구", "테니스", "골프")),
            TestHobby("뷰티", mutableListOf("메이크업", "헤어"))
        ))
    }

    // 관심 취미 선택
    fun setHobbySelected(selectHobby: String) {
        val updatedHobbies = _selectedHobbies.value

        if (updatedHobbies.contains(selectHobby)) { // 선택 취소
            updatedHobbies.remove(selectHobby)
        } else { // 선택
            updatedHobbies.add(selectHobby)
        }
        _selectedHobbies.value = updatedHobbies
    }

    // 다음 화면으로 네비게이션 처리
    fun navigateToNextScreen(navController: NavController) {
        navController.navigate(route = LoginScreens.LoginInputUserScreen.name)
    }

    // 취미 선택 상태 확인
    fun isHobbySelected(hobby: String): Boolean {
        return _selectedHobbies.value.contains(hobby)
    }
}