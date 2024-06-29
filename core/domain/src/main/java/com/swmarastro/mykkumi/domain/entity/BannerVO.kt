package com.swmarastro.mykkumi.domain.entity

class HomeBannerListResponse : BaseResponse<BannerListVO>()
data class BannerListVO(
    val banners: List<BannerItemVO> = listOf()
)

data class BannerItemVO(
    val id: Int = -1,
    val imageUrl: String = ""
)
