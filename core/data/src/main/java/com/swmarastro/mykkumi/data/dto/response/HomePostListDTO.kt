package com.swmarastro.mykkumi.data.dto.response

import com.google.gson.annotations.SerializedName
import com.swmarastro.mykkumi.domain.entity.HomePostImageVO
import com.swmarastro.mykkumi.domain.entity.HomePostItemVO
import com.swmarastro.mykkumi.domain.entity.HomePostListVO
import com.swmarastro.mykkumi.domain.entity.HomePostPinVO
import com.swmarastro.mykkumi.domain.entity.HomePostProductVO
import com.swmarastro.mykkumi.domain.entity.HomePostWriterVO
import com.swmarastro.mykkumi.domain.entity.RichText

data class HomePostListDTO(
    @SerializedName("posts")
    val posts: List<HomePostItemDTO>,

    @SerializedName("cursor")
    val cursor: String?,
){
    fun toEntity(): HomePostListVO = HomePostListVO(
        posts = posts.map { it.toEntity() },
        cursor = cursor
    )

    data class HomePostItemDTO(
        @SerializedName("id")
        val id: Int,

        @SerializedName("images")
        val images: List<HomePostImageDTO>,

        @SerializedName("category")
        val category: String,

        @SerializedName("subCategory")
        val subCategory: String,

        @SerializedName("writer")
        val writer: HomePostWriterDTO,

        @SerializedName("content")
        val content: List<RichText>,
    ) {
        fun toEntity(): HomePostItemVO = HomePostItemVO(
            id = id,
            images = images.map { it.toEntity() },
            category = category,
            subCategory = subCategory,
            writer = writer.toEntity(),
            content = content
        )

        data class HomePostWriterDTO(
            @SerializedName("uuid")
            val uuid: String,

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

        data class HomePostImageDTO(
            @SerializedName("url")
            val url: String,

            @SerializedName("pins")
            val pins: List<HomePostPinDTO>,
        ) {
            fun toEntity(): HomePostImageVO = HomePostImageVO(
                url = url,
                pins = pins.map { it.toEntity() }
            )

            data class HomePostPinDTO(
                @SerializedName("positionX")
                val positionX: Float,

                @SerializedName("positionY")
                val positionY: Float,

                @SerializedName("productInfo")
                val productInfo: HomePostProductDTO,
            ) {
                fun toEntity(): HomePostPinVO = HomePostPinVO(
                    positionX = positionX,
                    positionY = positionY,
                    productInfo = productInfo.toEntity()
                )

                data class HomePostProductDTO(
                    @SerializedName("name")
                    val name: String,

                    @SerializedName("url")
                    val url: String?,
                ) {
                    fun toEntity(): HomePostProductVO = HomePostProductVO(
                        name = name,
                        url = url
                    )
                }
            }
        }
    }
}