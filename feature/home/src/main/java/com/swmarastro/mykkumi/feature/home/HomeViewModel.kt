package com.swmarastro.mykkumi.feature.home

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swmarastro.mykkumi.domain.entity.HomeBannerVO
import com.swmarastro.mykkumi.domain.repository.ImageRepository
import com.swmarastro.mykkumi.util.ImageLoader
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val imageRepository: ImageRepository
) : ViewModel() {
    private val _homeBannerUiState = MutableStateFlow<MutableList<Bitmap?>>(mutableListOf())
    val homeBannerUiState: StateFlow<MutableList<Bitmap?>> get() = _homeBannerUiState

    fun loadImages(imageUrls: MutableList<HomeBannerVO>) {
        viewModelScope.launch {
            val bitmaps = imageUrls.map { homeBannerVO ->
                val byteArray = withContext(Dispatchers.IO) {
                    imageRepository.loadImage(homeBannerVO.imageUrl)
                }
                byteArray?.let { ImageLoader.byteArrayToBitmap(it) }
            }.toMutableList()
            _homeBannerUiState.value = bitmaps
        }
    }
}