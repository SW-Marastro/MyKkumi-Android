package com.swmarastro.mykkumi.data.dto.response

import com.google.gson.annotations.SerializedName

data class MykkumiLoginResponseDTO(
    @SerializedName("refreshToken")
    val refreshToken: String,

    @SerializedName("accessToken")
    val accessToken: String,
)
