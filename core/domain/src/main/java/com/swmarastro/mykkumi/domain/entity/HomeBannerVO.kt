package com.swmarastro.mykkumi.domain.entity

data class HomeBannerListVO(
    val banners: List<HomeBannerVO>
)

data class HomeBannerVO(
    val id: Int,
    val imageUrl: String
)
