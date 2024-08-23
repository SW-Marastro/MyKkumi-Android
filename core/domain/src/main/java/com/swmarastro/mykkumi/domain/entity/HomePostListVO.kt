package com.swmarastro.mykkumi.domain.entity

data class HomePostListVO(
    val posts: List<HomePostItemVO> = listOf(),
    val cursor: String? = ""
)

data class HomePostItemVO(
    val id: Int = -1,
    val images: List<HomePostImageVO> = listOf(),
    val category: String = "",
    val subCategory: String = "",
    val writer: HomePostWriterVO = HomePostWriterVO("", ""),
    val content: List<RichText>?,
)

data class HomePostWriterVO(
    val uuid: String = "",
    val profileImage: String? = "",
    val nickname: String = "",
)

// 본문 RichText
data class RichText(
    val type: String,
    val text: String,
    val color: String?,
    val linkUrl: String?
)

data class HomePostImageVO(
    val url: String,
    val pins: List<HomePostPinVO>,
)

data class HomePostPinVO(
    val positionX: Float,
    val positionY: Float,
    val productInfo: HomePostProductVO,
)

data class HomePostProductVO(
    val name: String,
    val url: String?,
)