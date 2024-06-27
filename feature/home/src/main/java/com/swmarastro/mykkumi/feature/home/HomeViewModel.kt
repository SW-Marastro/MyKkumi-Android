package com.swmarastro.mykkumi.feature.home

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swmarastro.mykkumi.domain.entity.HomeBannerListVO
import com.swmarastro.mykkumi.domain.entity.HomeBannerVO
import com.swmarastro.mykkumi.domain.repository.ImageRepository
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
    private val getHomeBannerUseCase: GetHomeBannerUseCase
) : ViewModel() {
    private val _bannerImageUiState = MutableStateFlow<MutableList<Bitmap?>>(mutableListOf())
    val bannerImageUiState: StateFlow<MutableList<Bitmap?>> get() = _bannerImageUiState

    private val _homeBannerUiState = MutableStateFlow<HomeBannerListVO>(HomeBannerListVO(listOf()))
    val homeBannerUiState: StateFlow<HomeBannerListVO> get() = _homeBannerUiState

    fun loadImages(imageUrls: List<HomeBannerVO>) {
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
}