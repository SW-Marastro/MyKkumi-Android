package com.swmarastro.mykkumi.data.dto.response

import com.google.gson.annotations.SerializedName

data class HomeBannerItemDTO(
    @SerializedName("id")
    val id: Int,

    @SerializedName("imageUrl")
    val imageUrl: String
)
