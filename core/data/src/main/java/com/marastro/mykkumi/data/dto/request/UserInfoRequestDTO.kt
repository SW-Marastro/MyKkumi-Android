package com.marastro.mykkumi.data.dto.request

import com.google.gson.annotations.SerializedName

data class UpdateUserInfoRequestDTO(
    @SerializedName("nickname")
    val nickname: String?,

    @SerializedName("profileImage")
    val profileImage: String?,

    @SerializedName("introduction")
    val introduction: String?,

    @SerializedName("categoryIds")
    val categoryIds: List<Long>?
)