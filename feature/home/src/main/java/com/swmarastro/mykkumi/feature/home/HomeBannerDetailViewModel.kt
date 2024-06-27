package com.swmarastro.mykkumi.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swmarastro.mykkumi.domain.entity.HomeBannerItemVO
import com.swmarastro.mykkumi.domain.entity.HomeBannerListVO
import com.swmarastro.mykkumi.domain.usecase.GetHomeBannerDetailUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class HomeBannerDetailViewModel @Inject constructor(
    private val getHomeBannerDetailUseCase: GetHomeBannerDetailUseCase
) : ViewModel() {
    private val _homeBannerDetailUiState = MutableStateFlow<HomeBannerItemVO>(HomeBannerItemVO())
    val homeBannerDetailUiState: StateFlow<HomeBannerItemVO> get() = _homeBannerDetailUiState

    fun setBannerDetail(bannerId: Int) {
        viewModelScope.launch {
            try {
                val homeBanner = withContext(Dispatchers.IO) {
                    getHomeBannerDetailUseCase(bannerId)
                }
                _homeBannerDetailUiState.value = homeBanner
            } catch (e: Exception) {
                _homeBannerDetailUiState.value = HomeBannerItemVO()
            }
        }
    }
}