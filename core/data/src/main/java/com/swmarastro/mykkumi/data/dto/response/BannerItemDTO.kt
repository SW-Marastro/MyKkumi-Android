package com.swmarastro.mykkumi.data.dto.response

import com.google.gson.annotations.SerializedName
import com.swmarastro.mykkumi.domain.entity.BannerItemVO

data class BannerItemDTO(
    @SerializedName("id")
    val id: Int,

    @SerializedName("imageUrl")
    val imageUrl: String
) {
    fun toEntity(): BannerItemVO = BannerItemVO(
        id = id,
        imageUrl = imageUrl
    )
}
