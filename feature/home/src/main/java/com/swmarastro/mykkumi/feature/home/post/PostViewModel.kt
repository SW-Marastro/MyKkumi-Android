package com.swmarastro.mykkumi.feature.home.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swmarastro.mykkumi.domain.entity.HomePostListVO
import com.swmarastro.mykkumi.domain.usecase.GetHomePostListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val getHomePostListUseCase: GetHomePostListUseCase
) : ViewModel() {

    // 포스트 리스트
    private val _postListUiState = MutableStateFlow<HomePostListVO>(HomePostListVO())
    val postListUiState: StateFlow<HomePostListVO> get() = _postListUiState

    // 포스트 리스트
    fun setPostList(cursor: String?, limit: Int?) {
        viewModelScope.launch {
            try {
                val homePostList = withContext(Dispatchers.IO) {
                    getHomePostListUseCase(cursor, limit)
                }
                _postListUiState.value = homePostList
            } catch (e: Exception) {
                _postListUiState.value = HomePostListVO()
            }
        }
    }
}