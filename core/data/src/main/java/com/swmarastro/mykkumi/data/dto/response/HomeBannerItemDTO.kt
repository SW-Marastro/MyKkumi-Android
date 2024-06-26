package com.swmarastro.mykkumi.data.dto.response

import com.google.gson.annotations.SerializedName
import com.swmarastro.mykkumi.domain.entity.HomeBannerVO

data class HomeBannerItemDTO(
    @SerializedName("id")
    val id: Int,

    @SerializedName("imageUrl")
    val imageUrl: String
) {
    fun toEntity(): HomeBannerVO = HomeBannerVO(
        id = id,
        imageUrl = imageUrl
    )
}
