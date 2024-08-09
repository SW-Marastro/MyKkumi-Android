package com.swmarastro.mykkumi.feature.post

import android.net.Uri
import com.swmarastro.mykkumi.domain.entity.PostEditPinVO

data class PostImageData(
    var imageId: Long? = null,     // 이미지 id
    var imageUri: Uri?, // S3에 업로드된 경로
    var isSelect: Boolean = false, // 선택 유무
    var pinList: MutableList<PostEditPinVO>? = null // pin
)