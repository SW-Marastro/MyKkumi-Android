package com.swmarastro.mykkumi.data.dto.response

import com.google.gson.annotations.SerializedName
import com.swmarastro.mykkumi.domain.entity.UpdateUserInfoResponseVO
import com.swmarastro.mykkumi.domain.entity.UserInfoVO

data class UserInfoDTO(
    @SerializedName("nickname")
    val nickname: String?,

    @SerializedName("email")
    val email: String?,

    @SerializedName("introduction")
    val introduction: String?,

    @SerializedName("profileImage")
    val profileImage: String?,
) {
    fun toEntity(): UserInfoVO = UserInfoVO(
        nickname = nickname,
        email = email,
        introduction = introduction,
        profileImage = profileImage
    )
}

data class UpdateUserInfoResponseDTO(
    @SerializedName("nickname")
    val nickname: String?,

    @SerializedName("email")
    val email: String,

    @SerializedName("introduction")
    val introduction: String?,

    @SerializedName("profileImage")
    val profileImage: String?
) {
    fun toEntity(): UpdateUserInfoResponseVO = UpdateUserInfoResponseVO(
        nickname = nickname,
        email = email,
        introduction = introduction,
        profileImage = profileImage
    )
}