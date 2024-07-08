package com.swmarastro.mykkumi.feature.home.banner

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swmarastro.mykkumi.domain.entity.BannerDetailVO
import com.swmarastro.mykkumi.domain.entity.BannerItemVO
import com.swmarastro.mykkumi.domain.entity.BannerListVO
import com.swmarastro.mykkumi.domain.usecase.GetBannerDetailUseCase
import com.swmarastro.mykkumi.domain.usecase.GetBannerListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class HomeBannerViewModel @Inject constructor(
    private val getBannerListUseCase: GetBannerListUseCase,
    private val getBannerDetailUseCase: GetBannerDetailUseCase,
) : ViewModel() {
    // 홈 > 배너 캐러셀
    private val _bannerListUiState = MutableStateFlow<BannerListVO>(BannerListVO())
    val bannerListUiState: StateFlow<BannerListVO> get() = _bannerListUiState

    // 배너 상세
    private val _bannerDetailUiState = MutableStateFlow<BannerDetailVO>(BannerDetailVO())
    val bannerDetailUiState: StateFlow<BannerDetailVO> get() = _bannerDetailUiState

    // 선택된 배너
    private val _selectBannerId = MutableLiveData<Int>()
    val selectBannerId: LiveData<Int> get() = _selectBannerId

    // 홈 > 배너 캐러셀
    fun setHomeBanner() {
        viewModelScope.launch {
            try {
                val homeBanner = withContext(Dispatchers.IO) {
                    getBannerListUseCase()
                }
                _bannerListUiState.value = homeBanner
            } catch (e: Exception) {
                _bannerListUiState.value = BannerListVO()
            }
        }
    }

    // 홈 > 배너 캐러셀에서 배너 선택
    fun selectHomeBanner(bannerId: Int) {
        _selectBannerId.value = bannerId
    }

    // 배너 상세
    fun setBannerDetail(bannerId: Int) {
        viewModelScope.launch {
            try {
                val homeBannerDetail = withContext(Dispatchers.IO) {
                    getBannerDetailUseCase(bannerId)
                }
                _bannerDetailUiState.value = homeBannerDetail
            } catch (e: Exception) {
                _bannerDetailUiState.value = BannerDetailVO()
            }
        }
    }
}