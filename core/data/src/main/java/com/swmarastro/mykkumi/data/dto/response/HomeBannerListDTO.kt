package com.swmarastro.mykkumi.data.dto.response

import com.google.gson.annotations.SerializedName

data class HomeBannerListDTO(
    @SerializedName("banners")
    val banners: MutableList<HomeBannerItemDTO>
)
