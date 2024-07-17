package com.swmarastro.mykkumi.data.dto.request

import com.google.gson.annotations.SerializedName

data class KakaoLoginRequestDTO(
    @SerializedName("refreshToken")
    val refreshToken: String,

    @SerializedName("accessToken")
    val accessToken: String,
)
