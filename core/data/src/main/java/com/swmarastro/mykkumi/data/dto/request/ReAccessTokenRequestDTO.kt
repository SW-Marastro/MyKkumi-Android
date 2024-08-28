package com.swmarastro.mykkumi.data.dto.request

import com.google.gson.annotations.SerializedName

data class ReAccessTokenRequestDTO (
    @SerializedName("refreshToken")
    val refreshToken: String,
)