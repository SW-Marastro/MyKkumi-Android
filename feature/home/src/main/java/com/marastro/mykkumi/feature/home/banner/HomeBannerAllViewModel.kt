package com.marastro.mykkumi.feature.home.banner

import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.marastro.mykkumi.domain.entity.BannerListVO
import com.marastro.mykkumi.domain.usecase.banner.GetBannerListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class HomeBannerAllViewModel @Inject constructor(
    private val getBannerListUseCase: GetBannerListUseCase,
) : ViewModel() {
    // 홈 > 배너 캐러셀
    private val _bannerListUiState = MutableStateFlow<BannerListVO>(BannerListVO())
    val bannerListUiState: StateFlow<BannerListVO> get() = _bannerListUiState

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
                _bannerListUiState.emit( homeBanner )
            } catch (e: Exception) {
                _bannerListUiState.emit( BannerListVO() )
            }
        }
    }

    // 홈 > 배너 캐러셀에서 배너 선택
    fun selectHomeBanner(bannerId: Int) {
        _selectBannerId.value = bannerId
    }

    // 배너 상세 페이지로 이동
    fun navigateBannerDetail(navController: NavController?) {
        val navigateDeepLink = "mykkumi://banner.detail?bannerId=${selectBannerId.value}"
        navController?.navigate(deepLink = navigateDeepLink.toUri())
    }
}