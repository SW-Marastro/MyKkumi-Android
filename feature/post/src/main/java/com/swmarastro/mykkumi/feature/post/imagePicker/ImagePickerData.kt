package com.swmarastro.mykkumi.feature.post.imagePicker

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

interface ImagePickerItem

data class ImagePickerData(
    var localUri: Uri,            // 디바이스에서의 경로
    var presignedUri: Uri?,       // S3에 업로드된 경로
    var isSelect: Boolean = false, // 선택 유무
    var selectNum: Int = -1        // 선택 순서
) : ImagePickerItem

data class CameraBtn(
    var isSelect: Boolean = false
) : ImagePickerItem


@Parcelize
data class ImagePickerArgument(
    var selectImages: MutableList<Uri>,
) : Parcelable