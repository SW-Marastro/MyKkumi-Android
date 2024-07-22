package com.swmarastro.mykkumi.data.util

import android.net.Uri
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

object FormDataUtil {
    // Uri를 Multipart/form-data로 변환
    fun convertUriToMultipart(image: Any?): MultipartBody.Part? {
        val imageUri = anyToUri(image)

        if(imageUri == null) return null

        val file = File(imageUri.path)
        val requestFile = file.asRequestBody("image/*".toMediaType())
        return MultipartBody.Part.createFormData("image", file.name, requestFile)
    }

    fun getBody(value: String?): RequestBody? {
        return value?.toRequestBody("text/plain".toMediaType())
    }

    fun getListLongBody(key: String, value: List<Long>?): List<MultipartBody.Part>? {
        return value?.map { MultipartBody.Part.createFormData(key, it.toString())}
    }
}


fun anyToUri(imageUri: Any?): Uri? {
    return if(imageUri is Uri) {
        imageUri
    } else {
        null
    }
}