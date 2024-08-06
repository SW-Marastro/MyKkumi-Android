package com.swmarastro.mykkumi.domain.entity

//data class PostEditVO ()

data class PostEditPinVO(
    var pinId: Long?,
    var positionX: Float,
    var positionY: Float,
//    var product: PostEditPinProductVO?
)

data class PostEditPinProductVO(
    var productName: String,
)