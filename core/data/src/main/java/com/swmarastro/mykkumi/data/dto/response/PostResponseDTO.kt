package com.swmarastro.mykkumi.data.dto.response

import com.google.gson.annotations.SerializedName
import com.swmarastro.mykkumi.domain.entity.PostEditResponseVO

data class PostEditResponseDTO(
    @SerializedName("postId")
    val postId: Int
) {
    fun toEntity(): PostEditResponseVO = PostEditResponseVO(
        postId = postId
    )
}
