package com.swmarastro.mykkumi.domain.entity

data class PostListVO(
    val posts: List<PostItemVO> = listOf()
)

data class PostItemVO(
    val id: Int = -1,
    val image: List<String> = listOf(),
    val category: String = "",
    val subCategory: String = "",
    val writer: PostWriterVO = PostWriterVO("", ""),
    val content: String = "",
)

data class PostWriterVO(
    val profileImage: String = "",
    val nickname: String = "",
)