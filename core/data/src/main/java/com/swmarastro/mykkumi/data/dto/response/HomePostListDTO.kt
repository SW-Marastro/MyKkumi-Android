package com.swmarastro.mykkumi.data.dto.response

import com.google.gson.annotations.SerializedName
import com.swmarastro.mykkumi.domain.entity.HomePostItemVO
import com.swmarastro.mykkumi.domain.entity.HomePostListVO
import com.swmarastro.mykkumi.domain.entity.HomePostWriterVO

data class 정(
    @SerializedName("posts")
    val posts: List<HomePostItemDTO>
){
    fun toEntity(): HomePostListVO = HomePostListVO(
        posts = posts.map { it.toEntity() }
    )

    data class HomePostItemDTO(
        @SerializedName("id")
        val id: Int,

        @SerializedName("images")
        val images: List<String>,

        @SerializedName("category")
        val category: String,

        @SerializedName("subCategory")
        val subCategory: String,

        @SerializedName("writer")
        val writer: HomePostWriterDTO,

        @SerializedName("content")
        val content: String,
    ) {
        fun toEntity(): HomePostItemVO = HomePostItemVO(
            id = id,
            images = images,
            category = category,
            subCategory = subCategory,
            writer = writer.toEntity(),
            content = content
        )

        data class HomePostWriterDTO(
            @SerializedName("profileImage")
            val profileImage: String,

            @SerializedName("nickname")
            val nickname: String,
        ) {
            fun toEntity(): HomePostWriterVO = HomePostWriterVO(
                profileImage = profileImage,
                nickname = nickname
            )
        }
    }
}