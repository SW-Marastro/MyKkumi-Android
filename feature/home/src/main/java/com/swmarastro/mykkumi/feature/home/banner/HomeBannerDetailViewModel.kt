package com.swmarastro.mykkumi.feature.home.banner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swmarastro.mykkumi.domain.entity.BannerDetailVO
import com.swmarastro.mykkumi.domain.usecase.banner.GetBannerDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class HomeBannerDetailViewModel @Inject constructor(
    private val getBannerDetailUseCase: GetBannerDetailUseCase,
) : ViewModel() {
    // 배너 상세
    private val _bannerDetailUiState = MutableStateFlow<BannerDetailVO>(BannerDetailVO())
    val bannerDetailUiState: StateFlow<BannerDetailVO> get() = _bannerDetailUiState

    // 배너 상세
    fun setBannerDetail(bannerId: Int) {
        viewModelScope.launch {
            try {
                val homeBannerDetail = withContext(Dispatchers.IO) {
                    getBannerDetailUseCase(bannerId)
                }
                _bannerDetailUiState.emit( homeBannerDetail )
            } catch (e: Exception) {
                _bannerDetailUiState.emit( BannerDetailVO() )
            }
        }
    }
}