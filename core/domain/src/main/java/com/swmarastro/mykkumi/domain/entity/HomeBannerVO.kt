package com.swmarastro.mykkumi.domain.entity

data class HomeBannerListVO(
    val banners: List<HomeBannerItemVO> = listOf()
)

data class HomeBannerItemVO(
    val id: Int = -1,
    val imageUrl: String = ""
)
