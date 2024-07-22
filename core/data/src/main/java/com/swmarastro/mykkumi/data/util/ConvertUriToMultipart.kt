package com.swmarastro.mykkumi.data.util

import android.net.Uri
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

// Uri를 Multipart/form-data로 변환
fun convertUriToMultipart(image: Any?): MultipartBody.Part? {
    val imageUri = anyToUri(image)

    if(imageUri == null) return null

    val file = File(imageUri.path)
    val requestFile = file.asRequestBody("image/*".toMediaType())
    return MultipartBody.Part.createFormData("image", file.name, requestFile)
}

fun anyToUri(imageUri: Any?): Uri? {
    return if(imageUri is Uri) {
        imageUri
    } else {
        null
    }
}