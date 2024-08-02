package com.swmarastro.mykkumi.feature.post.image

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class ImagePickerData(
    var localUri: Uri, // 디바이스에서의 경로
    var presignedUri: Uri?,
    var isSelect: Boolean = false
)

@Parcelize
data class ImagePickerArgument(
    var selectImages: MutableList<Uri>
) : Parcelable