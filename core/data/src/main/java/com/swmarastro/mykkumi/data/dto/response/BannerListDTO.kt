package com.swmarastro.mykkumi.data.dto.response

import com.google.gson.annotations.SerializedName
import com.swmarastro.mykkumi.domain.entity.BannerListVO

data class BannerListDTO(
    @SerializedName("banners")
    val banners: List<BannerItemDTO>
) {
    fun toEntity(): BannerListVO = BannerListVO(
        banners = banners.map { it.toEntity() }
    )
}
