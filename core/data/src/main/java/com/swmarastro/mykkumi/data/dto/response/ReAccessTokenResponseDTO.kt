package com.swmarastro.mykkumi.data.dto.response

import com.google.gson.annotations.SerializedName

data class ReAccessTokenResponseDTO (
    @SerializedName("accessToken")
    val accessToken: String,
)