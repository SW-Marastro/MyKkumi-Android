package com.swmarastro.mykkumi.data.dto.request

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody

data class UpdateUserInfoRequestDTO(
    @SerializedName("nickname")
    val nickname: String?,

    @SerializedName("profileImage")
    val profileImage: MultipartBody.Part?,

    @SerializedName("introduction")
    val introduction: String?,

    @SerializedName("categoryIds")
    val categoryIds: List<Long>?
)