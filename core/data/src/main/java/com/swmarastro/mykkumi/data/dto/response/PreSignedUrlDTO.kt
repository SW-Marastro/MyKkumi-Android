package com.swmarastro.mykkumi.data.dto.response

import com.google.gson.annotations.SerializedName

data class PreSignedUrlDTO(
    @SerializedName("presignedUrl")
    val presignedUrl: String,

    @SerializedName("cdnUrl")
    val cdnUrl: String
)
