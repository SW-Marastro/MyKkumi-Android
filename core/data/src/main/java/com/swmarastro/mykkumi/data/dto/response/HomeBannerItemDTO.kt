package com.swmarastro.mykkumi.data.dto.response

import com.google.gson.annotations.SerializedName
import com.swmarastro.mykkumi.domain.entity.HomeBannerItemVO

data class HomeBannerItemDTO(
    @SerializedName("id")
    val id: Int,

    @SerializedName("imageUrl")
    val imageUrl: String
) {
    fun toEntity(): HomeBannerItemVO = HomeBannerItemVO(
        id = id,
        imageUrl = imageUrl
    )
}
