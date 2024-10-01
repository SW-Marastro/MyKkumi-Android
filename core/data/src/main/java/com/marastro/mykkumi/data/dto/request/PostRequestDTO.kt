package com.marastro.mykkumi.data.dto.request

import com.google.gson.annotations.SerializedName

data class PostEditRequestDTO(
    @SerializedName("subCategoryId")
    val subCategoryId: Long,

    @SerializedName("content")
    val content: String?,

    @SerializedName("images")
    val images: List<PostImageRequestDTO>
)

data class PostImageRequestDTO(
    @SerializedName("url")
    val url: String?,

    @SerializedName("pins")
    val pins: List<PostPinRequestDTO>
)

data class PostPinRequestDTO(
    @SerializedName("positionX")
    val positionX: Double,

    @SerializedName("positionY")
    val positionY: Double,

    @SerializedName("productInfo")
    val productInfo: PostProductRequestDTO
)

data class PostProductRequestDTO(
    @SerializedName("name")
    val name: String,

    @SerializedName("url")
    val url: String?
)