package com.swmarastro.mykkumi.domain.entity

// 포스트 등록
data class PostImageVO(
    var imageId: Long? = null,     // 이미지 id
    var imageUri: String, // S3에 업로드된 경로
    var imageLocalUri: String, // 이미지 디바이스 경로
    var isSelect: Boolean = false, // 선택 유무
    var pinList: MutableList<PostEditPinVO> = mutableListOf() // pin
)
data class PostEditResponseVO(
    val postId: Int
)

// ----------------------------------

data class PostEditPinVO(
    var pinId: Long?,
    var positionX: Float,
    var positionY: Float,
    var product: PostEditPinProductVO
)

data class PostEditPinProductVO(
    var productName: String,
    var productUrl: String? = null,
)