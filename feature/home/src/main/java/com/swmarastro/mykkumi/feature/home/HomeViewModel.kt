package com.swmarastro.mykkumi.feature.home

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swmarastro.mykkumi.domain.entity.BannerListVO
import com.swmarastro.mykkumi.domain.entity.BannerItemVO
import com.swmarastro.mykkumi.domain.usecase.GetBannerDetailUseCase
import com.swmarastro.mykkumi.domain.usecase.GetBannerListUseCase
import com.swmarastro.mykkumi.domain.usecase.LoadImageUseCase
import com.swmarastro.mykkumi.util.ImageLoader
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val loadImageUseCase: LoadImageUseCase,
    private val getBannerListUseCase: GetBannerListUseCase,
    private val getBannerDetailUseCase: GetBannerDetailUseCase
) : ViewModel() {
    // 홈 > 배너 > 이미지 로드 (이미지 로더 라이브러리로 대체해서 삭제 고민 중)
    private val _bannerImageUiState = MutableStateFlow<MutableList<Bitmap?>>(mutableListOf())
    val bannerImageUiState: StateFlow<MutableList<Bitmap?>> get() = _bannerImageUiState

    // 홈 > 배너 캐러셀
    private val _bannerListUiState = MutableStateFlow<BannerListVO>(BannerListVO())
    val bannerListUiState: StateFlow<BannerListVO> get() = _bannerListUiState

    // 배너 상세
    private val _bannerDetailUiState = MutableStateFlow<BannerItemVO>(BannerItemVO())
    val bannerDetailUiState: StateFlow<BannerItemVO> get() = _bannerDetailUiState


    // 선택된 배너
    private val _selectBannerId = MutableLiveData<Int>()
    val selectBannerId: LiveData<Int> get() = _selectBannerId

    fun loadImages(imageUrls: List<BannerItemVO>) {
        viewModelScope.launch {
            val bitmaps = imageUrls.map { homeBannerVO ->
                val byteArray = withContext(Dispatchers.IO) {
                    loadImageUseCase(homeBannerVO.imageUrl)
                }
                byteArray?.let { ImageLoader.byteArrayToBitmap(it) }
            }.toMutableList()
            _bannerImageUiState.value = bitmaps
        }
    }

    // 홈 > 배너 캐러셀
    fun setHomeBanner() {
        viewModelScope.launch {
            try {
                val homeBanner = withContext(Dispatchers.IO) {
                    getBannerListUseCase()
                }
                _bannerListUiState.value = homeBanner
            } catch (e: Exception) {
                _bannerListUiState.value = BannerListVO(listOf())
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
                _bannerDetailUiState.value = BannerItemVO()
            }
        }
    }
}