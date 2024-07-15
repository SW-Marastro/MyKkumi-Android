package com.swmarastro.mykkumi.domain.entity

data class HomePostListVO(
    val posts: List<HomePostItemVO> = listOf(),
    val cursor: String = ""
)

data class HomePostItemVO(
    val id: Int = -1,
    val images: List<String> = listOf(),
    val category: String = "",
    val subCategory: String = "",
    val writer: HomePostWriterVO = HomePostWriterVO("", ""),
    val content: String = "",
)

data class HomePostWriterVO(
    val profileImage: String = "",
    val nickname: String = "",
)