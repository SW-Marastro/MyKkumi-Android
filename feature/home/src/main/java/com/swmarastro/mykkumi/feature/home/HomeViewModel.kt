package com.swmarastro.mykkumi.feature.home

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swmarastro.mykkumi.domain.entity.HomeBannerListVO
import com.swmarastro.mykkumi.domain.entity.HomeBannerItemVO
import com.swmarastro.mykkumi.domain.usecase.GetHomeBannerDetailUseCase
import com.swmarastro.mykkumi.domain.usecase.GetHomeBannerUseCase
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
    private val getHomeBannerUseCase: GetHomeBannerUseCase,
    private val getHomeBannerDetailUseCase: GetHomeBannerDetailUseCase
) : ViewModel() {
    // 홈 > 배너 > 이미지 로드 (이미지 로더 라이브러리로 대체해서 삭제 고민 중)
    private val _bannerImageUiState = MutableStateFlow<MutableList<Bitmap?>>(mutableListOf())
    val bannerImageUiState: StateFlow<MutableList<Bitmap?>> get() = _bannerImageUiState

    // 홈 > 배너 캐러셀
    private val _homeBannerUiState = MutableStateFlow<HomeBannerListVO>(HomeBannerListVO())
    val homeBannerUiState: StateFlow<HomeBannerListVO> get() = _homeBannerUiState

    // 선택된 배너
    private val _selectBannerId = MutableLiveData<Int>()
    val selectBannerId: LiveData<Int> get() = _selectBannerId

    // 배너 상세
    private val _homeBannerDetailUiState = MutableStateFlow<HomeBannerItemVO>(HomeBannerItemVO())
    val homeBannerDetailUiState: StateFlow<HomeBannerItemVO> get() = _homeBannerDetailUiState

    fun loadImages(imageUrls: List<HomeBannerItemVO>) {
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
                    getHomeBannerUseCase()
                }
                _homeBannerUiState.value = homeBanner
            } catch (e: Exception) {
                _homeBannerUiState.value = HomeBannerListVO(listOf())
            }
        }
    }

    // 홈 > 배너 캐러셀에서 배너 선택
    fun selectHomeBanner(bannerId: Int) {
        _selectBannerId.value = bannerId
        Log.d("---viewModel1", selectBannerId.value.toString())
    }

    // 배너 상세
    fun setBannerDetail(bannerId: Int) {
        viewModelScope.launch {
            try {
                Log.d("---viewModel2-1", selectBannerId.value.toString())
                val homeBannerDetail = withContext(Dispatchers.IO) {
                    getHomeBannerDetailUseCase(bannerId)
                }
                _homeBannerDetailUiState.value = homeBannerDetail
                Log.d("---viewModel2-2", _homeBannerDetailUiState.value.toString())
            } catch (e: Exception) {
                _homeBannerDetailUiState.value = HomeBannerItemVO()
            }
        }
    }
}