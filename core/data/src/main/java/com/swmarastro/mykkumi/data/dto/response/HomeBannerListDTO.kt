package com.swmarastro.mykkumi.data.dto.response

import com.google.gson.annotations.SerializedName
import com.swmarastro.mykkumi.domain.entity.HomeBannerListVO

data class HomeBannerListDTO(
    @SerializedName("banners")
    val banners: List<HomeBannerItemDTO>
) {
    fun toEntity(): HomeBannerListVO = HomeBannerListVO(
        banners = banners.map { it.toEntity() }
    )
}
