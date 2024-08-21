package com.swmarastro.mykkumi.data.dto.request

import com.google.gson.annotations.SerializedName

data class ReportPostRequestDTO(
    @SerializedName("postId")
    val postId: Long,

    @SerializedName("reason")
    val reason: String = "ETC",

    @SerializedName("content")
    val content: String = "불편한 내용의 글입니다.",
)

data class ReportUserRequestDTO(
    @SerializedName("userUuid")
    val userUuid: String,

    @SerializedName("reason")
    val reason: String = "ETC",

    @SerializedName("content")
    val content: String = "",
)