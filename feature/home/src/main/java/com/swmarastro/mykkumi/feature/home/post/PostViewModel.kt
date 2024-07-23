package com.swmarastro.mykkumi.feature.home.post

import androidx.lifecycle.ViewModel
import com.swmarastro.mykkumi.domain.entity.HomePostItemVO
import com.swmarastro.mykkumi.domain.usecase.post.GetHomePostListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val getHomePostListUseCase: GetHomePostListUseCase
) : ViewModel() {

    // 포스트 리스트
    private val _postListUiState = MutableStateFlow<MutableList<HomePostItemVO>>(mutableListOf())
    val postListUiState: StateFlow<MutableList<HomePostItemVO>> get() = _postListUiState

    // 포스트 리스트 cursor, limit
    private val postLimit: Int? = null
    private var postCursor: String? = null

    // 포스트 끝 도달
    private var isPostEnd = false

    // 포스트 리스트
    // isCursor의 역할: 처음으로 데이터를 조회해오는 것인지, cursor가 있는 상태로 다음 데이터를 불러오는 것인지
    suspend fun setPostList(isCursor: Boolean) {
        try {
            val homePostList = withContext(Dispatchers.IO) {
                getHomePostListUseCase(postCursor, postLimit)
            }
            if(homePostList.posts.size == 0) isPostEnd = true
            else {
                if (isCursor) _postListUiState.value.addAll(homePostList.posts)
                else _postListUiState.value = homePostList.posts.toMutableList()
                postCursor = homePostList.cursor
            }
        } catch (e: Exception) {
            _postListUiState.value = mutableListOf()
        }
    }

    // 더 조회할 포스트가 있는지
    fun getIsPostEnd() : Boolean {
        return isPostEnd
    }
}