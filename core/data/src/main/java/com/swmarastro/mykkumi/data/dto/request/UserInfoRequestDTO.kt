package com.swmarastro.mykkumi.data.dto.request

import com.swmarastro.mykkumi.domain.entity.UpdateUserInfoRequestVO
import okhttp3.MultipartBody

data class UpdateUserInfoRequestDTO(
    val nickname: String?,
    val profileImage: MultipartBody.Part?,
    val introduction: String?,
    val categoryId: List<Long>?
) {
    fun toEntity(): UpdateUserInfoRequestVO = UpdateUserInfoRequestVO(
        nickname = nickname,
        profileImage = profileImage,
        introduction = introduction,
        categoryId = categoryId
    )
}